package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.UserTestData.*;

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
    public void getWithMeals() throws Exception {
        User user = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, USER);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.MEALS);
    }

    @Test
    public void getWithMealsNotFound() throws Exception {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }
}
