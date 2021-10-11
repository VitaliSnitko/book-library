<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Library</title>
    <link rel="stylesheet" href="css/main-page.css">
</head>
<body>

<a href="<c:url value="/add"/>">Add book</a>
<div id="book list">
    <c:forEach var="bookDto" items="${requestScope.bookList}">
        <div class="book">
            <c:choose>
                <c:when test="${bookDto.base64Cover.equals(\"\")}">
                    <img class="cover" src="images/placeholder-cover.png" alt="lorem">
                </c:when>
                <c:otherwise>
                    <img class="cover" src="data:image/jpeg;base64,${bookDto.base64Cover}" alt="lorem">
                </c:otherwise>
            </c:choose>
            <a href="<c:url value="/edit?id=${bookDto.id}"/>">
                <span class="title">${bookDto.title}</span>
            </a>

            <img class="icon" src="images/delete-icon.png" alt="delete"><br>
            <span>${bookDto.authorDtos.get(0).name}</span>
            <c:if test="${bookDto.authorDtos.size() > 1}">
                <c:forEach var="authorEntity" items="${bookDto.authorDtos}" begin="1">
                    <span>, ${authorEntity.name}</span>
                </c:forEach>
            </c:if>
            <br>
            <span>${bookDto.publishDate}</span>

        </div>
    </c:forEach>
</div>
</body>
</html>