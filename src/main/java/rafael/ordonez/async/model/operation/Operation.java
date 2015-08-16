package rafael.ordonez.async.model.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by rafa on 16/8/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Operation implements Identifiable<String>, Serializable {

    private static final long serialVersionUID = 8520931837010119598L;

    @Id
    @NotNull
    private String id;

    @Column
    @NotNull
    private Integer secondOperand;

    @Column
    @NotNull
    private Integer firstOperand;

    public Operation() {}

    public Operation(String id, Integer firsOperand, Integer secondOperand) {
        this.id = id;
        this.firstOperand = firsOperand;
        this.secondOperand = secondOperand;
    }

    public Integer getSecondOperand() {
        return secondOperand;
    }

    public String getId() {
        return id;
    }

    public Integer getFirstOperand() {
        return firstOperand;
    }
}
