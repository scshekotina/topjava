package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_USER_1_ID, USER_ID);
        assertMatch(meal, meal_user_1);
    }

    @Test
    public void getIncorrectUserException() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_USER_1_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_USER_1_ID, USER_ID));
    }

    @Test
    public void deleteIncorrectUserException() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_USER_1_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 31), USER_ID);
        assertMatch(betweenInclusive, Arrays.asList(meal_user_7, meal_user_6, meal_user_5, meal_user_4, meal_user_3,
                meal_user_2, meal_user_1));
    }

    @Test
    public void getBetweenInclusiveBorders() {
        List<Meal> betweenInclusive = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 30), USER_ID);
        assertMatch(betweenInclusive, Arrays.asList(meal_user_3, meal_user_2, meal_user_1));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, Arrays.asList(meal_admin_2, meal_admin_1));
    }

    @Test
    public void getAllNotFound() {
        List<Meal> all = service.getAll(NOT_FOUND);
        assertMatch(all, Collections.emptyList());
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_USER_1_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateIncorrectUserException() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
        assertMatch(service.get(MEAL_USER_1_ID, USER_ID), meal_user_1);
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId =created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTime() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Duplicate", 400), USER_ID));
    }
}