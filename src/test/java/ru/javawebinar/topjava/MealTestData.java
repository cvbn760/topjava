package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class MealTestData {

    public static final Meal meal = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "SaveTest", 500);

    public static void update(Meal meal){
        meal.setCalories(12345);
        meal.setDescription("update");
        meal.setDateTime(LocalDateTime.of(3030, Month.JUNE, 05, 12, 34));
    }

    public static void assertMath(Meal actual, Meal expected){
        assert (actual.equals(expected) && actual.hashCode() == expected.hashCode());
    }

    public static void assertMath(List<Meal> meals){
        assert(meals.size() == 7);
    }
}
