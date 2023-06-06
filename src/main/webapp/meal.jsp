<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <style>
        input {
            margin-top: 5px;
            margin-bottom: 5px;
        }
        label {
            display: inline-block;
            min-width: 120px;
        }
    </style>
    <c:choose>
        <c:when test="${param.action == 'insert'}">
            <title>Insert meal</title>
            <h2>Insert meal</h2>
        </c:when>
        <c:otherwise>
            <title>Update meal</title>
            <h2>Update meal</h2>
        </c:otherwise>
    </c:choose>
</head>
<body>
<form METHOD="post" action="meals" >
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
    <input type="number" readonly="readonly" name="mealId" value="${meal.id}" hidden/> <br/>
    <label for="datetime">Date and time:</label>
    <input type="datetime-local" name="datetime" value="${meal.dateTime}" id="datetime" required/><br/>
    <label for="description">Description :</label>
    <input type="text" name="description" value="${meal.description}" id="description" required/><br/>
    <label for="calories">Calories :</label>
    <input type="number" name="calories" value="${meal.calories}" id="calories" min="0" required/><br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>

</body>
</html>