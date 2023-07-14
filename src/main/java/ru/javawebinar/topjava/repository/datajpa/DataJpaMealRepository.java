package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if(meal.isNew()) {
            User userReference = crudUserRepository.getReferenceById(userId);
            meal.setUser(userReference);
            return crudMealRepository.save(meal);
        }
        Meal mealFromStorage = get(meal.id(), userId);
        if (mealFromStorage == null) {
            return null;
        }
        meal.setUser(mealFromStorage.getUser());
        return crudMealRepository.save(meal);
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Meal mealFromStorage = get(id, userId);
        if (mealFromStorage == null) {
            return false;
        }
        crudMealRepository.delete(mealFromStorage);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Meal get(int id, int userId) {
        Optional<Meal> meal = crudMealRepository.findById(id);
        if (meal.isPresent() && meal.get().getUser().id() == userId) {
            return meal.get();
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.getAll(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        User userReference = crudUserRepository.getReferenceById(userId);
        return crudMealRepository.findByDateTimeBetweenAndUserOrderByDateTimeDesc(startDateTime, endDateTime.minusMinutes(1), userReference);
    }
}
