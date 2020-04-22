package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import java.time.LocalDateTime;
import static org.slf4j.LoggerFactory.getLogger;

public class PostgresJdbcMealRepository extends AbstractJdbcMealRepository{
    private static final Logger log = getLogger(PostgresJdbcMealRepository.class);

    @Override
    String dateConversion(LocalDateTime dateTime) {
        log.info(">>>> PostgresJdbcMealRepository <<<<");
        return String.valueOf(dateTime);
    }
}
