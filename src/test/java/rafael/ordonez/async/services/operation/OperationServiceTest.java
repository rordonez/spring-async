package rafael.ordonez.async.services.operation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rafael.ordonez.async.AsyncApplication;
import rafael.ordonez.async.model.operation.Operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by rafa on 16/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsyncApplication.class)
@DirtiesContext
public class OperationServiceTest {

    @Autowired
    OperationService operationService;

    @Test
    public void whenThereIsNoIdentifiersInDataBaseShouldReturnFalse() throws Exception {
        //When
        boolean existIdentifier = this.operationService.exist("d");
        //Then
        assertFalse(existIdentifier);
    }

    @Test
    public void WhenExistAnIdentifierItShouldBeFound() throws Exception {
        //Given
        Operation operation = new Operation("1", 1, 2);
        this.operationService.saveOperation(operation);

        //When
        boolean existentIdentifier = this.operationService.exist(operation.getId());
        //Then
        assertTrue(existentIdentifier);
    }
}
