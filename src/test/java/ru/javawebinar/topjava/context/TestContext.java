package ru.javawebinar.topjava.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.javawebinar.topjava.repository.inmemory.InMemoryUserRepository;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.user.AdminRestController;

@Configuration
public class TestContext {

    @Bean("InMemoryUserRepository")
    public InMemoryUserRepository getUserRepository(){
        return new InMemoryUserRepository();
    }

    @Bean("AdminRestController")
    public AdminRestController getAdminRestController(){
        return new AdminRestController();
    }

    @Bean("UserService")
    public UserService getUserService(){
        return new UserService(getUserRepository());
    }
}
