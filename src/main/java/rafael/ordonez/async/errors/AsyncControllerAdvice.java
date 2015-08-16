package rafael.ordonez.async.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafa on 16/8/15.
 */
@ControllerAdvice
public class AsyncControllerAdvice  extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<ObjectError> bindingErrors = ex.getBindingResult().getAllErrors();
        List<ErrorNode> errors = new ArrayList<>();
        for (ObjectError bingdingError : bindingErrors) {
            errors.add(new ErrorNode("Invalid value for argument " + ((FieldError)bingdingError).getField()));
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errors, headers, HttpStatus.BAD_REQUEST);
    }
}