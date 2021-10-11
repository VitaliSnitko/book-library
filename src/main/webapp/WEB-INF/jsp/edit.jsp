<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Book page</title>
</head>
<body>

<c:set var="bookDto" scope="session" value="${requestScope.bookDto}"/>
<form action="edit" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${param.id}"/>
    <input type="text" placeholder="Title" name="title"
           value="${bookDto.title}"><br>
<%--    <c:set var="authorEntities" value="${bookDto.authorEntities[0]}"/>--%>
<%--    <c:if test="${fn:length(bookDto.authorEntities) > 1}">--%>
<%--        <c:forEach var="authorEntity" items="${bookDto.authorEntities}" begin="1">--%>
<%--            <c:set var="authorEntities" value="${authorEntities + authorEntity}"/>--%>
<%--        </c:forEach>--%>
<%--    </c:if>--%>
    <input type="text" placeholder="Author(s) (divided by ',')" name="authors"
           value="${bookDto.authorDtos.get(0).name}<c:if test="${bookDto.authorDtos.size() > 1}"><c:forEach var="authorEntity" items="${bookDto.authorDtos}" begin="1">, ${authorEntity.name}</c:forEach></c:if>"><br>
    <input type="text" placeholder="Genre(s) (divided by ',')" name="genres"
           value="${bookDto.genreDtos.get(0).name}<c:if test="${bookDto.genreDtos.size() > 1}"><c:forEach var="genreEntity" items="${bookDto.genreDtos}" begin="1">, ${genreEntity.name}</c:forEach></c:if>"><br>
    <input type="text" placeholder="Publisher" name="publisher"
           value="${bookDto.publisher}"><br>
    <input type="date" placeholder="Publish date" name="date"
           value="${bookDto.publishDate}"><br>
    <input type="text" placeholder="Page count" name="page count"
           value="${bookDto.pageCount}"><br>
    <input type="text" placeholder="ISBN" name="ISBN"
           value="${bookDto.ISBN}"><br>
    <textarea placeholder="Description" name="description">${requestScope.bookDto.description}</textarea><br>
    <input type="file" accept="image/jpeg,image/png" name="cover"><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
