package rafael.ordonez.async.repositories.operation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rafael.ordonez.async.model.operation.Operation;

/**
 * Created by rafa on 16/8/15.
 */
@Repository
public interface OperationRepository extends CrudRepository<Operation, String> {

}