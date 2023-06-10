package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.user.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (request.getParameter("action") != null && request.getParameter("action") .equalsIgnoreCase("filter")) {
            log.info("Filtering");
            String dateFromInForm = request.getParameter("dateFrom");
            LocalDate dateFrom = dateFromInForm != null &&
                    !dateFromInForm.isEmpty() ?
                    LocalDate.parse(dateFromInForm) :
                    null;
            String dateToFromForm = request.getParameter("dateTo");
            LocalDate dateTo = dateToFromForm != null &&
                    !dateToFromForm.isEmpty() ?
                    LocalDate.parse(dateToFromForm) :
                    null;
            String timeFromFromForm = request.getParameter("timeFrom");
            LocalTime timeFrom = timeFromFromForm != null &&
                    !timeFromFromForm.isEmpty() ?
                    LocalTime.parse(timeFromFromForm) :
                    null;
            String timeToFromForm = request.getParameter("timeTo");
            LocalTime timeTo = timeToFromForm != null &&
                    !timeToFromForm.isEmpty() ?
                    LocalTime.parse(timeToFromForm) :
                    null;

            request.setAttribute("meals", mealRestController.getAll(dateFrom, dateTo, timeFrom, timeTo));
            request.setAttribute("dateFrom", dateFrom);
            request.setAttribute("dateTo", dateTo);
            request.setAttribute("timeFrom", timeFrom);
            request.setAttribute("timeTo", timeTo);
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else {
            request.setCharacterEncoding("UTF-8");
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            if (meal.isNew()) {
                log.info("Create {}", meal);
                mealRestController.create(meal);
            } else {
                log.info("Update {}", meal);
                mealRestController.update(meal, meal.getId());
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (request.getParameter("user") != null) {
            SecurityUtil.setAuthUserId(Integer.parseInt(request.getParameter("user")));
        }
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
