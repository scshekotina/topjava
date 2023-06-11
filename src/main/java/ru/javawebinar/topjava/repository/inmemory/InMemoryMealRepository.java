package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> userMealMap = repository.get(userId);
            if (userMealMap != null) {
                userMealMap.put(meal.getId(), meal);
                return meal;
            }
            userMealMap = new ConcurrentHashMap<>();
            userMealMap.put(meal.getId(), meal);
            repository.put(userId, userMealMap);
            return meal;
        }
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap != null) {
            // handle case: update, but not present in storage
            return userMealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        return userMealMap != null && userMealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        return userMealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> allMeals = repository.get(userId);
        if (allMeals == null) {
            return new ArrayList<>();
        }
        return allMeals.values().stream().sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllHalfOpen(LocalDateTime dateFrom, LocalDateTime dateTo, int userId) {
        List<Meal> allMeals = getAll(userId);
        return allMeals.stream()
                .filter(meal -> (DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), dateFrom, dateTo)))
                .collect(Collectors.toList());
    }
}

