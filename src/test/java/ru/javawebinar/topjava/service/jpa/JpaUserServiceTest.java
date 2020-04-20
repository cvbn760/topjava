package ru.javawebinar.topjava.service.jpa;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.UserServiceTest;

import static org.slf4j.LoggerFactory.getLogger;
@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.JPA)
public class JpaUserServiceTest extends UserServiceTest {
    private static final Logger log = getLogger(JpaUserServiceTest.class);

    @Autowired
    @Override
    public void setRepository(UserRepository repository) {
        super.setRepository(repository);
        log.info("Name repository for JpaUserServiceTest: " + repository.getClass().getSimpleName());
    }
}
