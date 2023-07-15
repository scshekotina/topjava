package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void getWithMeals() {
        User withMeals = service.getWithMeals(UserTestData.ADMIN_ID);
        User dsf = service.getWithMeals(UserTestData.USER_ID);
    }
}
