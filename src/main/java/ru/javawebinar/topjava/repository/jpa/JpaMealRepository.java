package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class, userId);
        meal.setUser(ref);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else if (em.createNamedQuery(Meal.UPDATE)
                .setParameter("description", meal.getDescription())
                .setParameter("calories", meal.getCalories())
                .setParameter("dateTime", meal.getDateTime())
                .setParameter("id", meal.id())
                .setParameter("userId", userId)
                .executeUpdate() == 1) {
            return meal;
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        try {
            Meal mealReference = em.getReference(Meal.class, id);
            if (mealReference != null && mealReference.getUser().id() == userId) {
                em.remove(mealReference);
                return true;
            }
        } catch (EntityNotFoundException ignored) {
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal != null && meal.getUser().id() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALF_OPEN, Meal.class)
                .setParameter(1, startDateTime)
                .setParameter(2, endDateTime)
                .setParameter(3, userId)
                .getResultList();
    }
}