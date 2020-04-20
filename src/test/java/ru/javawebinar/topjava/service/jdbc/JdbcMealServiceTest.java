package ru.javawebinar.topjava.service.jdbc;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.service.MealServiceTest;

import static org.slf4j.LoggerFactory.getLogger;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
    private static final Logger log = getLogger(JdbcMealServiceTest.class);

    @Autowired
    @Override
    public void setRepository(MealRepository repository) {
        super.setRepository(repository);
        log.info("Name repository for JdbcMealServiceTest: " + repository.getClass().getSimpleName());
    }
}
