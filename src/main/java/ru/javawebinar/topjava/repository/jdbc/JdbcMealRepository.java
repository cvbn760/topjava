package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcMealRepository.class);

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate).withTableName("meals").usingGeneratedKeyColumns("id");

    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()){
            log.info("save meal {} for user {}", meal, userId);
            String sql = String.format("INSERT INTO meals (userid, datetime, description, calories)" +
                            "VALUES (%d, '%s', '%s', '%s');",userId, DateTimeUtil.toString(meal.getDateTime()),
                    meal.getDescription(), meal.getCalories());
            return jdbcTemplate.update(sql) == 0 ? null : meal;
        }
        else if (!meal.isNew()){
            log.info("update meal {} for user {}", meal, userId);
            String sql = String.format("UPDATE meals SET id=%d, userid=%d, datetime='%s', description='%s'," +
                            "calories=%d WHERE id=%d;",
                    meal.getId(), userId, DateTimeUtil.toString(meal.getDateTime()), meal.getDescription(),
                    meal.getCalories(), meal.getId());
            return jdbcTemplate.update(sql) == 0 ? null : meal;
        }
        else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND userId=?;", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT id, datetime, description, calories FROM" +
                " meals WHERE id=? AND userId=?;", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll meal for user {}", userId);
        return jdbcTemplate.query("SELECT id, datetime, description, calories FROM meals WHERE userId=? ORDER BY datetime DESC;", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        String sql = String.format("SELECT id, datetime, description, calories FROM meals \n" +
                "WHERE userId = %d AND datetime BETWEEN '%s' AND '%s' ORDER BY datetime DESC;",
                userId, DateTimeUtil.toString(startDate), DateTimeUtil.toString(endDate));
        log.info("getBetweenHalfOpen sql {}", sql);
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }
}
