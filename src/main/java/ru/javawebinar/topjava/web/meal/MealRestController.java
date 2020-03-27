package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.MealServlet;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;


public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealService service;

    public void setService(MealService service) {
        this.service = service;
    }

    public void methodPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        service.save(meal);
        response.sendRedirect("meals");
    }

    public void methodGet(HttpServletRequest request, HttpServletResponse response) throws Exception, ServletException, ParseException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                service.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                                "", 1000) :
                        service.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                request.setAttribute("meals",
                        MealsUtil.getFilteredTos(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                                DateTimeUtil.getLocalDateTime(request.getParameter("from_date"), request.getParameter("from_time")),
                                DateTimeUtil.getLocalDateTime(request.getParameter("before_date"), request.getParameter("before_time"))));
                request.getSession().setAttribute("from_date", request.getParameter("from_date"));
                request.getSession().setAttribute("from_time", request.getParameter("from_time"));
                request.getSession().setAttribute("before_date", request.getParameter("before_date"));
                request.getSession().setAttribute("before_time", request.getParameter("before_time"));
                request.getSession().setAttribute("curentDate", DateTimeUtil.getCurrentDate());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.getSession().setAttribute("curentDate", DateTimeUtil.getCurrentDate());
                request.getSession().setAttribute("from_date", "1970-01-01");
                request.getSession().setAttribute("from_time", "00:00");
                request.getSession().setAttribute("before_date", DateTimeUtil.getCurrentDate());
                request.getSession().setAttribute("before_time", DateTimeUtil.getCurrentTime());
                request.setAttribute("meals",
                        MealsUtil.getTos(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}