<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<%--    <title>"${param.action == "insert" ? "Insert meal" : 'Edit meal'}"</title>--%>
    <title><c:choose>
    <c:when test="${param.action == 'insert'}">
        not empty
    </c:when>
    <c:otherwise>
        empty
    </c:otherwise>
    </c:choose></title>
</head>
<body>
<form METHOD="post" action="meals" >
<%--    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>--%>
    <input type="number" readonly="readonly" name="mealId" value="${meal.id}" hidden/> <br/>
    <label>Date :
        <input type="datetime-local" name="datetime" value="${meal.dateTime}" />
    </label><br/>
    <label>Description :
        <input type="text" name="description" value="<c:out value="${meal.description}" />" />
    </label><br/>
    <label>Calories :
        <input type="number" name="calories" value="<c:out value="${meal.calories}" />" />
    </label><br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>