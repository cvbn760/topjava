package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/ajax/profile/meals")
public class MealUIController extends AbstractMealController {

    private final Logger log = LoggerFactory.getLogger(MealUIController.class);

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        log.info("AJAX get all meals");
        return super.getAll();
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("AJAX delete meal id={}", id);
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid Meal meal, BindingResult result) {
        log.info("AJAX create or update meal - {}", meal.toString());
        if (result.hasErrors()){
            StringJoiner joiner = new StringJoiner("<br>");
            result.getFieldErrors().forEach(error -> joiner.add(String.format("%s: %s", error.getField(), error.getDefaultMessage())));
            return ResponseEntity.unprocessableEntity().body(joiner.toString());
        }
        if (meal.isNew()) {
            super.create(meal);
        }
        else {
            super.update(meal, meal.getId());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        log.info("AJAX filter meal after {} {} and before {} {}", startDate, startTime, endDate, endTime);
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable("id") int id) {
        log.info("AJAX get user id - {}", id);
        return super.get(id);
    }
}