package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;

@ContextConfiguration(locations = {"classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:test.db/testDataDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

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
}
