package ru.javawebinar.topjava.service.datajpa;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.service.UserServiceTest;

import static org.slf4j.LoggerFactory.getLogger;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class, profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    private static final Logger log = getLogger(DataJpaUserServiceTest.class);

    @Autowired
    @Override
    public void setRepository(UserRepository repository) {
        super.setRepository(repository);
        log.info("Name repository for DataJpaUserServiceTest: " + repository.getClass().getSimpleName());
    }
}
