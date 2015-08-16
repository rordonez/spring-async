package rafael.ordonez.async.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rafael.ordonez.async.model.operation.Operation;
import rafael.ordonez.async.model.operation.OperationResult;
import rafael.ordonez.async.services.heavy.Calculations;
import rafael.ordonez.async.services.operation.OperationService;

import javax.validation.Valid;
import java.util.concurrent.Callable;

/**
 * Created by rafa on 16/8/15.
 */
@RestController
@RequestMapping("/operation")
public class OperationController {

    private OperationService operationService;
    private Calculations calculations;

    @Autowired
    public OperationController(OperationService operationService, Calculations calculations) {
        this.operationService = operationService;
        this.calculations = calculations;
    }

    /**
     *
     * This method creates a new operation.
     *
     * @param operation
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Callable<ResponseEntity<OperationResult<Integer>>> add(@Valid @RequestBody Operation operation) {
        return doHeavyCalculations(operation);
    }


    /**
     *
     * This method simulates heavy calculations waiting 2 seconds!! ;)
     *
     * @param operation
     * @return
     */
    private Callable<ResponseEntity<OperationResult<Integer>>> doHeavyCalculations(@Valid @RequestBody final Operation operation) {
        return new Callable<ResponseEntity<OperationResult<Integer>>>() {
            @Override
            public ResponseEntity<OperationResult<Integer>> call() throws Exception {
                Thread.sleep(2000);
                saveOperationInDataBase(operation);
                return new ResponseEntity<>(composeResult(operation), HttpStatus.OK);
            }
        };
    }

    /**
     *
     * This method creates a new Result for the operation
     *
     * @param operation
     * @return the result for the specified operation
     */
    private OperationResult<Integer> composeResult(@Valid @RequestBody Operation operation) {
        return new OperationResult<>(operation.getId(), calculations.heavyCalculation(operation.getFirstOperand(), operation.getSecondOperand()));
    }


    /**
     *
     * Saves a new operation into the dataBase
     *
     * @param operation
     */
    private void saveOperationInDataBase(Operation operation) {
        operationService.saveOperation(operation);
    }

}
