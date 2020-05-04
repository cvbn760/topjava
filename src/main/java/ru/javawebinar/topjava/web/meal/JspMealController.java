package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController{
    private static final Logger log = LoggerFactory.getLogger(JspMealController.class);

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAll(Model model) {
        log.info("JspMealController getAll");
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request, @RequestParam("id") int id) {
        log.info("JspMealController delete {}", id);
        super.delete(id);
        return "redirect:/meals";
    }

    @RequestMapping(value={"/create", "/update"}, method = RequestMethod.GET)
    public ModelAndView update(HttpServletRequest request, @RequestParam(required = false) Integer id) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.info("JspMealController update {}", id);
        ModelAndView modelAndView = new ModelAndView();
        final Meal meal = (id == null) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 0) : super.get(id);
        if (id == null) request.setAttribute("action", "create");
        modelAndView.addObject("meal", meal);
        modelAndView.setViewName("mealForm");
        return modelAndView;
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    public String getAllFiltered(@RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate,
                                 @RequestParam(value = "startTime", required = false) String startTime,
                                 @RequestParam(value = "endTime", required = false) String endTime,
                                 Model model) {
        log.info("JspMealController getAllFiltered");
        model.addAttribute("meals", super.getBetween(parseLocalDate(startDate),
                parseLocalTime(startTime),
                parseLocalDate(endDate),
                parseLocalTime(endTime)));
        return "meals";
    }

    @RequestMapping(value = "/mealForm", method = RequestMethod.POST)
    public String mealForm(HttpServletRequest request) throws IOException {
        log.info("JspMealController mealForm");
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            super.create(meal);
        } else {
            super.update(meal, Integer.parseInt(request.getParameter("id")));
        }
        return "redirect:/meals";
    }
}
