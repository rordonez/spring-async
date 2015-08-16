package rafael.ordonez.async.model.operation;

import java.io.Serializable;

/**
 * Created by rafa on 16/8/15.
 */
public class OperationResult<T> implements Serializable {
    private T value;
    private String id;

    public OperationResult(String id, T result) {
        this.id = id;
        this.value = result;
    }

    public T getValue() {
        return value;
    }

    public String getId() {
        return id;
    }
}
