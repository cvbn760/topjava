package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    private final TransactionTemplate transactionTemplate;

    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, TransactionTemplate transactionTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = false, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public Meal save(Meal meal, int userId) {
                    MapSqlParameterSource map = new MapSqlParameterSource()
                            .addValue("id", meal.getId())
                            .addValue("description", meal.getDescription())
                            .addValue("calories", meal.getCalories())
                            .addValue("date_time", meal.getDateTime())
                            .addValue("user_id", userId);
                    if (meal.isNew()) {
                        Number newId = insertMeal.executeAndReturnKey(map);
                        meal.setId(newId.intValue());
                    } else {
                        if (namedParameterJdbcTemplate.update("" +
                                        "UPDATE meals " +
                                        "   SET description=:description, calories=:calories, date_time=:date_time " +
                                        " WHERE id=:id AND user_id=:user_id"
                                , map) == 0) {
                            return null;
                        }
                    }
        return meal;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = false, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(
                "SELECT * FROM meals WHERE id = ? AND user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }


    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }


    // Декларативное описание транзакции
    @Transactional(propagation = Propagation.REQUIRED,
            isolation = Isolation.SERIALIZABLE,
            readOnly = true, timeout = 1,
            rollbackFor= Exception.class)
    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time >=  ? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, startDateTime, endDateTime);
    }
}
