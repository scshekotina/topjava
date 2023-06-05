package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private final MealDao dao = new MealDaoInMemory();

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String forward="/meals.jsp";

        if (action == null) {
            log.debug("get mealTos");
            List<MealTo> mealTos = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
            req.setAttribute("meals", mealTos);
        } else if (action.equalsIgnoreCase("delete")) {
            log.debug("delete meal id = " + req.getParameter("id"));
            dao.delete(Integer.parseInt(req.getParameter("id")));
        } else if (action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            log.debug("edit meal id = " + id);
            Meal meal = dao.get(id);
            req.setAttribute("meal", meal);
            forward = "/meal.jsp";
        } else if (action.equalsIgnoreCase("insert")) {
            log.debug("insert meal");
            forward = "/meal.jsp";
        }

        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Meal meal = new Meal();
        try {
            meal.setDateTime(LocalDateTime.parse(req.getParameter("datetime")));
            meal.setCalories(Integer.parseInt(req.getParameter("calories")));
        } catch (NumberFormatException | DateTimeParseException e) {
            log.debug(e.toString());
        }
        meal.setDescription(req.getParameter("description"));
        String mealId = req.getParameter("mealId");
        if (mealId == null) {
            dao.add(meal);
        }
        else {
            meal.setId(Integer.parseInt(mealId));
            dao.update(meal);
        }
        List<MealTo> mealTos = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
        req.setAttribute("meals", mealTos);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
