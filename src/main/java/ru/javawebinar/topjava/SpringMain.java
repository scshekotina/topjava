package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.MealRestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpringMain {

    private static final Logger log = LoggerFactory.getLogger(SpringMain.class);
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.now(), "dfcs", 123));
            log.info(mealRestController.getAll().toString());
            Meal meal = mealRestController.create(new Meal(LocalDateTime.now(), "33333", 100));
            log.info(mealRestController.getAll().toString());
            mealRestController.update(new Meal(meal.getId(), LocalDateTime.now(), "aaa", 311), meal.getId());
            log.info(mealRestController.getAll().toString());
            mealRestController.delete(meal.getId());
            log.info(mealRestController.getAll().toString());
        }
    }
}
