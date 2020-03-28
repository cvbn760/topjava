<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .about {
            display:inline-block;
            margin: 0% 2%;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Моя еда</h2>
    <div align="center">
        <form method="get">
            <input type="hidden" name="action" value="filter">
            <div style="margin: 0% 10%">
                <div align="center">
                    <div class="about" align="left">
                        <label>От даты</label>
                        <br>
                        <input type="date" name="from_date"  value=<%=(session.getAttribute("from_date"))%>  min="1970-01-01" max=<%=(session.getAttribute("curentDate"))%>>
                    </div>
                    <div class="about" align="left">
                        <label>До даты</label>
                        <br>
                        <input type="date" name="before_date" value=<%=(session.getAttribute("before_date"))%> min="1970-01-01" max=<%=(session.getAttribute("curentDate"))%>>
                    </div>
                    <div class="about" align="right">
                        <label>От времени</label>
                        <br>
                        <input type="time" name="from_time"  value=<%=(session.getAttribute("from_time"))%>>
                    </div>
                    <div class="about" align="right">
                        <label>До времени</label>
                        <br>
                        <input type="time" name="before_time"  value=<%=(session.getAttribute("before_time"))%>>
                    </div>
                </div>
                <hr>
                <div align="right">
                    <button type="button" onclick="window.history.back()">Отменить</button>
                    <button type="submit">Отфильтровать</button>
                </div>
            </div>
        </form>
        <div>
            <a href="meals?action=create">Add Meal</a>
            <br><br>
            <table border="1" cellpadding="8" cellspacing="0">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Calories</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <c:forEach items="${meals}" var="meal">
                    <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
                    <tr data-mealExcess="${meal.excess}">
                        <td>
                                <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                                <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                                        ${fn:formatDateTime(meal.dateTime)}
                        </td>
                        <td>${meal.description}</td>
                        <td>${meal.calories}</td>
                        <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                        <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</section>
</body>
</html>