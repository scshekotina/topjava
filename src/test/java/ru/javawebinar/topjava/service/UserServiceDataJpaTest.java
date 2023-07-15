package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.MealTestData.meals;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void getWithMeals() {
        User actual = service.getWithMeals(UserTestData.USER_ID);
        USER_MATCHER.assertMatch(actual, UserTestData.user);
        MEAL_MATCHER.assertMatch(actual.getMeals(), meals);
    }
}
