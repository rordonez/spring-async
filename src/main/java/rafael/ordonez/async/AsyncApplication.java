package rafael.ordonez.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import rafael.ordonez.async.services.heavy.Calculations;

/**
 * Created by rafa on 16/8/15.
 */
@SpringBootApplication
@EnableAsync
@EnableCaching
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class, args);
    }

    @Bean
    Calculations operations() {
        return new Calculations() {
            @Cacheable("addition")
            @Override
            public Integer heavyCalculation(Integer firstOperand, Integer secondOperand) {
                return firstOperand + secondOperand;
            }

        };
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("addition");
    }
}
