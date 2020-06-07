package ru.javawebinar.topjava.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealAjaxController;

import java.util.List;

@RestController
@RequestMapping("/ajax/admin/users")
public class AdminUIController extends AbstractUserController {
    private final Logger log = LoggerFactory.getLogger(AdminUIController.class);

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        log.info("AJAX get all users");
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("AJAX delete user id={}", id);
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestParam Integer id,
                               @RequestParam String name,
                               @RequestParam String email,
                               @RequestParam String password) {
        log.info("AJAX create or update user({}, {}, {}, {})", id, name, email, password);
        User user = new User(id, name, email, password, Role.ROLE_USER);
        if (user.isNew()) {
            super.create(user);
        }
    }

    @RequestMapping(value = "/{id}/{status}", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void checkUser(@PathVariable int id, @PathVariable boolean status){
        log.info("AJAX check user id={}, status={}", id, status);
        super.enabled(id, status);
    }
}
