package ru.javawebinar.topjava.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.user.AdminRestController;

@Configuration
@ComponentScan("ru.javawebinar.topjava.**")
public class TestContext {

    @Bean("InMemoryUserRepository")
    public InMemoryUserRepository getUserRepository(){
        return new InMemoryUserRepository();
    }
}
