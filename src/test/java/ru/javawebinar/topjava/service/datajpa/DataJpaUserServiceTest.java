package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    private static final Logger log = getLogger(DataJpaUserServiceTest.class);

    @Autowired
    @Override
    public void setRepository(UserRepository repository) {
        super.setRepository(repository);
        log.info("Name repository for DataJpaUserServiceTest: " + repository.getClass().getSimpleName());
    }

    @Test
    public void getUserWithMeal(){
        Map<User, Meal> actual = getService().getUserWithMeal(USER_ID);
        MEAL_MATCHER.assertMath(actual, UserTestData.getUserWithMeal());
    }
}
