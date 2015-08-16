package rafael.ordonez.async.services.heavy;

/**
 * Created by rafa on 16/8/15.
 *
 *
 * This interface represents methods with heavy calculations. The purpose is to store the calculation into a Cache
 *
 */
public interface Calculations {
    Integer heavyCalculation(Integer firstParam, Integer secondParam);
}
