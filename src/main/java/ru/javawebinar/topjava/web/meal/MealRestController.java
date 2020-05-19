package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_MEAL_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_MEAL_URL = "/rest/meals";

    @Override
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_MEAL_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal,@PathVariable int id) {
        super.update(meal, id);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public List<MealTo> getBetweenTo(
            @RequestParam(name = "startDate", required = false, defaultValue = "1970-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate", required = false, defaultValue = "3000-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate endDate,
            @RequestParam(name = "startTime", required = false, defaultValue = "00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)  LocalTime startTime,
            @RequestParam(name = "endTime", required = false, defaultValue = "23:59") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)  LocalTime endTime) {
                return super.getBetween(startDate, startTime, endDate, endTime);
            }
}