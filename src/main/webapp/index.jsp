<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Library</title>
</head>
<body>
<a href="<c:url value="/add"/>">Add book</a>
<div>
    <c:forEach var="bookDto" items="${requestScope.bookList}">
        <li><c:out value="${bookDto.title}"/></li>
    </c:forEach>
</div>
</body>
</html>