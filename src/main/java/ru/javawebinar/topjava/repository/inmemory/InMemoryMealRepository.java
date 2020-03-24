package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        else {
            repository.replace(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        // return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == SecurityUtil.authUserId()) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        if (meal != null && meal.getUserId() == SecurityUtil.authUserId()) {
            return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> meals = repository.values().stream().filter(meal -> meal.getUserId() == SecurityUtil.authUserId())
                .sorted((x,y) -> y.getDateTime().compareTo(x.getDateTime())).collect(Collectors.toList());
        return !meals.isEmpty() ? meals : Collections.emptyList();
    }
}

