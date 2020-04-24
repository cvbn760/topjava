package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceTest;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    private static final Logger log = getLogger(DataJpaMealServiceTest.class);

    @Autowired
    @Override
    public void setRepository(MealRepository repository) {
        super.setRepository(repository);
        log.info("Name repository for DataJpaMealServiceTest: " + repository.getClass().getSimpleName());
    }

    @Test
    public void getMealWithUser(){
        Map<User, Meal> actual = getService().getMealWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMath(actual, MealTestData.MEAL_WITH_USER);
    }
}
