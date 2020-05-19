package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.MealsUtil.createTo;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/meals/get/100004"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL3));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/rest/meals/delete/100004"))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(100004, 100000));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/meals/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(MEALS, USER.getCaloriesPerDay())));
    }


    @Test
    void createMeal() throws Exception {
        Meal meal = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post("/rest/meals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)));

        Meal created = readFromJson(action, Meal.class);
        int newId = created.id();
        meal.setId(newId);
        MEAL_MATCHER.assertMatch(created, meal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), meal);
    }

    @Test
    void update() throws Exception {
        Meal meal = getUpdated();
        perform(MockMvcRequestBuilders.post("/rest/meals/update/100002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)));
        MEAL_MATCHER.assertMatch(meal, mealService.get(100002, 100000));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get("/rest/meals/between?startDateTime=2020-01-31T08:00&endDateTime=2020-01-31T15:00:00"))
                               .andExpect(status().isOk())
                               .andDo(print())
                               .andExpect(MEAL_TO_MATCHER.contentJson(createTo(MEAL6, true), createTo(MEAL5, true)));
    }
}
