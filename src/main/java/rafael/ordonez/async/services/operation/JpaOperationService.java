package rafael.ordonez.async.services.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rafael.ordonez.async.model.operation.Operation;
import rafael.ordonez.async.repositories.operation.OperationRepository;

import javax.transaction.Transactional;

/**
 * Created by rafa on 16/8/15.
 */
@Service
@Transactional
public class JpaOperationService implements OperationService {

    private OperationRepository operationRepository;

    @Autowired
    public JpaOperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Override
    public boolean exist(String id) {
        Operation oneOperation = this.operationRepository.findOne(id);
        return oneOperation != null;
    }

    @Override
    public void saveOperation(Operation operation) {
        this.operationRepository.save(operation);
    }
}
