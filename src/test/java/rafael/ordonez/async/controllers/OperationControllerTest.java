package rafael.ordonez.async.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;
import rafael.ordonez.async.AsyncApplication;
import rafael.ordonez.async.errors.AsyncControllerAdvice;
import rafael.ordonez.async.model.operation.Operation;
import rafael.ordonez.async.services.heavy.Calculations;
import rafael.ordonez.async.services.operation.OperationService;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by rafa on 16/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsyncApplication.class)
@WebAppConfiguration
public class OperationControllerTest {

    @Autowired
    ObjectMapper mapper;

    private MockMvc mockMvc;

    @Mock
    private Calculations calculations;

    @Mock
    private OperationService operationService;

    @InjectMocks
    private OperationController operationController;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders.standaloneSetup(operationController)
                .setHandlerExceptionResolvers(createExceptionResolver())
                .build();
    }

    @Test
    public void aGetRequestShouldReturnASumOfTwoNumbers() throws Exception {
        //Given
        Operation operation = new Operation("1", 1, 2);

        Mockito.when(calculations.heavyCalculation(operation.getFirstOperand(), operation.getSecondOperand())).thenReturn(3);

        MvcResult mvcResult = assertAsyncStarted(operation);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(3)));
    }


    @Test
    public void aGetRequestShouldHaveARequestAndAResponseInJsonFormat() throws Exception {
        //Given
        Operation operation = new Operation("1", 1, 2);

        MvcResult mvcResult = assertAsyncStarted(operation);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        Mockito.verify(calculations, Mockito.times(1)).heavyCalculation(Mockito.any(), Mockito.any());
    }

    @Test
    public void anIdentifierShouldBePresentInTheRequestAndResponse() throws Exception {
        //Given
        Operation operation = new Operation("1", 1, 2);

        MvcResult mvcResult = assertAsyncStarted(operation);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(operation.getId()));
    }

    @Test
    public void ifThereIsNoIdentifierInTheRequestShouldReturnAnException() throws Exception {
        //Given
        Operation operation = new Operation(null, 1, 2);

        mockMvc.perform(post("/operation").content(mapper.writeValueAsString(operation)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("Invalid value for argument id"));

        //Given
        operation = new Operation("1", null, 2);


        mockMvc.perform(post("/operation").content(mapper.writeValueAsString(operation)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("Invalid value for argument firstOperand"));

        //Given
        operation = new Operation("1", null, null);

        mockMvc.perform(post("/operation").content(mapper.writeValueAsString(operation)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message", containsString("Invalid value for argument")))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void whenAnIdentifierHasNotBeenUsedShouldReturnA200StatusCode() throws Exception {
        //Given
        Operation operation = new Operation("1", 1, 2);

        Mockito.when(operationService.exist(operation.getId())).thenReturn(false);

        MvcResult mvcResult = assertAsyncStarted(operation);

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(operation.getId()));

        Mockito.verify(operationService, Mockito.times(1)).saveOperation(Mockito.any());
    }


    /**
     * Assert that the Async configuration is working correctly
     *
     * @param operation
     * @return
     * @throws Exception
     */
    private MvcResult assertAsyncStarted(Operation operation) throws Exception {
        return this.mockMvc.perform(post("/operation").content(mapper.writeValueAsString(operation)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(request().asyncResult(instanceOf(ResponseEntity.class)))
                .andReturn();
    }

    /**
     * @see <a href="https://jira.spring.io/browse/SPR-12751">SPR-12751</a>
     * <p>
     * Spring MVC will support the configuration of ControllerAdvice for standalone configurations in Spring 4.2
     */
    private ExceptionHandlerExceptionResolver createExceptionResolver() {
        ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
            protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
                Method method = new ExceptionHandlerMethodResolver(AsyncControllerAdvice.class).resolveMethod(exception);
                return new ServletInvocableHandlerMethod(new AsyncControllerAdvice(), method);
            }
        };
        exceptionResolver.afterPropertiesSet();
        exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return exceptionResolver;
    }
}
