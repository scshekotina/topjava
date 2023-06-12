<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .filter-col-1 {
            display: table-cell;
            width: 130px;
        }
        .filter-col-padding {
            padding-left: 100px;
        }
        .filter-col-2 {
            display: table-cell;
            width: 900px;
        }
        .filter-form {
            outline-style: solid;
            outline-width: thin;
            padding: 10px;
        }
        .meals-list {
            margin: 1% 10% 1% 10%;
        }
        .filter-button {
            margin-top: 15px;
        }
    </style>
</head>
<body>
<section>
    <div class="meals-list">
        <h3><a href="index.html">Home</a></h3>
    <hr/>
    <div class="filter-form">
        <form method="post" action="meals?action=filter">
            <div class="filter-col-1"><label>От даты (включая):
                <input type="date" value="${dateFrom}" name="dateFrom">
            </label>
            </div>
            <div class="filter-col-1"><label>До даты (включая):
                <input type="date" value="${dateTo}" name="dateTo">
            </label></div>
            <div class="filter-col-2 filter-col-padding"><label>От времени (включая):
                <input type="time" value="${timeFrom}" name="timeFrom">
            </label></div>
            <div class="filter-col-2"><label>До времени (исключая):
                <input type="time" value="${timeTo}" name="timeTo">
            </label>
            </div>
            <div class="filter-button">
                <button type="submit">Filter</button>
            </div>
        </form>
    </div>
    <h2>Meals</h2>
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
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
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
</section>
</body>
</html>