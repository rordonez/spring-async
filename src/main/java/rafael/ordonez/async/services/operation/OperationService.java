package rafael.ordonez.async.services.operation;

import rafael.ordonez.async.model.operation.Operation;

/**
 * Created by rafa on 16/8/15.
 */
public interface OperationService {
    boolean exist(String id);
    void saveOperation(Operation operation);
}
