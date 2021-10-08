<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.itechart.book_library.model.entity.BookEntity" %>
<%@ page import="com.itechart.book_library.model.dto.BookDto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

    <%--           <c:if test="${bookDto.authors.length > 1}">--%>
    <%--                <c:forEach var="author" items="${bookDto.authors}" begin="1">--%>
    <%--                    , ${author}</span><br>--%>
    <%--                </c:forEach>--%>
    <%--           </c:if>--%>
    <input type="text" placeholder="Author(s) (divided by ',')" name="authors" value="${bookDto.authors[0]}"><br>
    <input type="text" placeholder="Genre(s) (divided by ',')" name="genres" value="${bookDto.genres[0]}"><br>
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
