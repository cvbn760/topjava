package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import static org.slf4j.LoggerFactory.getLogger;


public class HsqldbJdbcMealRepository extends AbstractJdbcMealRepository{
    private static final Logger log = getLogger(HsqldbJdbcMealRepository.class);

    @Override
    String dateConversion(LocalDateTime dateTime) {
        log.info(">>>> HsqldbJdbcMealRepository <<<<");
        return String.valueOf(Timestamp.valueOf(dateTime));
    }
}
