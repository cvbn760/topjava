package ru.javawebinar.topjava.web.meal;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static ru.javawebinar.topjava.MealTestData.*;

class RootControllerTest extends AbstractControllerTest{

    @Test
    void delete() throws Exception {
        perform(get("/meals/delete?id=100004"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/meals"));
    }

    @Test
    void update() throws Exception {
        perform(get("/meals/update?id=100004"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("mealForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mealForm.jsp"))
                .andExpect(model().attribute("meal", new AssertionMatcher<Meal>() {
            @Override
            public void assertion(Meal actual) throws AssertionError {
                MEAL_MATCHER.assertMatch(actual, MEAL3);
            }
        }));
    }

    @Test
    void create() throws Exception {
        perform(get("/meals/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("mealForm"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mealForm.jsp"))
                .andExpect(model().attribute("meal", new AssertionMatcher<Meal>() {
            @Override
            public void assertion(Meal actual) throws AssertionError {
                MEAL_MATCHER.assertMatch(actual, CLEAR_MEAL);
            }
        }));
    }

    @Test
    void getBetween() throws Exception {
        perform(get("/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=08:00&endTime=14:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"));
    }
}