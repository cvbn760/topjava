package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/ajax/meals/")
public class MealAjaxController extends AbstractMealController {
    private final Logger log = LoggerFactory.getLogger(MealAjaxController.class);

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("AJAX delete meal id = {}", id);
        super.delete(id);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        log.info("AJAX get all");
        return super.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createOrUpdate(@RequestParam(required = true) String id,
                               @RequestParam(required = true) String dateTime,
                               @RequestParam(required = true) String description,
                               @RequestParam(required = true) String calories){
        log.info("AJAX createOrUpdate meal({}, {}, {}, {})",id, dateTime, description, calories);
        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, Integer.parseInt(calories));
        if (Integer.parseInt(id) == 0){
            super.create(meal);
        }
        else {
            super.update(meal, Integer.parseInt(id));
        }
    }

    @Override
    @RequestMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public Meal get(@PathVariable int id) {
        log.info("AJAX get meal id = {}", id);
        return super.get(id);
    }

    @Override
    @RequestMapping(value = "/{startDate}/{startTime}/{endDate}/{endTime}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getBetween(@PathVariable LocalDate startDate,
                                   @PathVariable LocalTime startTime,
                                   @PathVariable LocalDate endDate,
                                   @PathVariable LocalTime endTime) {
        log.info("AJAX filter (after {}:{} and before {}:{}", startDate, startTime, endDate, endTime);
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
