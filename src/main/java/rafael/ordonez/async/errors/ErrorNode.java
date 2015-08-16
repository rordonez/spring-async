package rafael.ordonez.async.errors;

import java.io.Serializable;

/**
 * Created by rafa on 16/8/15.
 */
public class ErrorNode implements Serializable {

    private String message;

    public ErrorNode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}