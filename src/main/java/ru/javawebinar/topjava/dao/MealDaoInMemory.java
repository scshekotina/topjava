package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemory implements MealDao{

    private static Map<Integer, Meal> meals = new HashMap<>();
    private static AtomicInteger counter = new AtomicInteger(0);

    public MealDaoInMemory() {
        meals.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.put(7, new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        counter.set(7);
    }

    @Override
    public void add(Meal meal) {
        int newId = counter.getAndIncrement();
        meal.setId(newId);
        meals.put(newId, meal);
    }

    @Override
    public void delete(int id) {
        Meal meal = meals.get(id);
        if (meal == null) {
            return;
        }
        meals.remove(id);
        counter.getAndDecrement();
    }

    @Override
    public void update(Meal meal) {
        meals.replace(meal.getId(), meal);
    }

    @Override
    public List<Meal> getAll() {
        return (new ArrayList<>(meals.values()));
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }
}
