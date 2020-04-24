package ru.javawebinar.topjava.repository.jdbc;

import java.time.LocalDateTime;

public abstract class AbstractJdbcMealRepository {

    abstract String dateConversion(LocalDateTime dateTime);
}
