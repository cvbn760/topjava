package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate).withTableName("meals").usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("id", meal.getId());
        map.addValue("datetime", meal.getDateTime());
        map.addValue("description", meal.getDescription());
        map.addValue("calories", meal.getCalories());
        map.addValue("userId", userId);

        if (meal.isNew()){
            Number newId = insertMeal.executeAndReturnKey(map);
            meal.setId(newId.intValue());
        }
        else if(namedParameterJdbcTemplate.update("UPDATE meals " +
                "SET description=:description, calories=:calories, datetime=:datetime " +
                "WHERE id=:id AND userId=:userId;", map) == 0){
            return null;
        }
        return meal;
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
