package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Autowired
    private MealRepository mealRepository;

    @Test
    public void delete() throws Exception {
        mealService.delete(MEAL1_ID, USER_ID);
        Assert.assertNull(mealRepository.get(MEAL1_ID, USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        mealService.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotOwn() throws Exception {
        mealService.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = mealService.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal actual = mealService.get(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(actual, ADMIN_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        mealService.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotOwn() throws Exception {
        mealService.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        mealService.update(updated, USER_ID);
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        mealService.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getBetweenHalfOpen(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID),
                MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenWithNullDates() throws Exception {
        MEAL_MATCHER.assertMatch(mealService.getBetweenHalfOpen(null, null, USER_ID), MEALS);
    }
    /*
    @Test
    public void save(){
        Meal meal = MealTestData.meal;
        mealService.create(meal,110000);
        assert (mealService.create(meal, 110000) == meal);
    }

    @Test
    public void update() {
          Meal meal = mealService.get(100007, 110000);
          MealTestData.update(meal);
          mealService.update(meal,110000);
          Meal meal1 = mealService.get(meal.getId(),110000);
          MealTestData.assertMath(meal1, meal);
    }

    @Test
    public void getAll() {
        MealTestData.assertMath(mealService.getAll(110000));
    }

    @Test(expected = NotFoundException.class)
    public void getNotMyMeal() {
        mealService.get(100008,110000);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        mealService.delete(-10, 110000);
    }

    @Test(expected = Exception.class)
    public void deleteNotMyMeal() {
        mealService.delete(100008,110000);
    }

    @Test(expected = NotFoundException.class)
    public void delete() {
        mealService.delete(100001, 110000);
        mealService.get(100001,110000);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotMyMeal(){
        Meal meal = mealService.get(100009, 110000);
        MealTestData.update(meal);
        mealService.update(meal,110000);
        Meal meal1 = mealService.get(meal.getId(),110000);
        MealTestData.assertMath(meal1, meal);
    }

     */
}
