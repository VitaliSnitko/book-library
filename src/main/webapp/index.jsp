<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.commons.io.IOUtils" %>
<%@ page import="com.itechart.book_library.model.dto.BookDto" %>
<%@ page import="java.util.Base64" %>
<html>
<head>
    <title>Book Library</title>
    <style>
        a {
            text-decoration: none;
        }
        .book {
            width: 400px;
            height: 160px;
            border: 1px black solid;
            padding: 10px;
            margin-top: 10px;
        }

        .cover {
            float: left;
            width: 100px;
            height: 150px;
        }

        .title {
            font-size: 16px;
            font-family: Arial, serif;
        }

        .icon {
            width: 20px;
            height: 20px;
        }
    </style>
</head>
<body>
<a href="<c:url value="/add"/>">Add book</a>
<div id="book list">
    <c:forEach var="bookDto" items="${requestScope.bookList}">
        <div class="book">
            <img class="cover" src="data:image/jpeg;base64,${bookDto.base64Cover}" alt="lorem">
            <a href="<c:url value="/edit?id=${bookDto.id}"/>">
                <span class="title">${bookDto.title}</span>
            </a>

            <img class="icon" src="images/delete-icon.png" alt="delete">
            <br>
            <span>${bookDto.authors[0]}</span><br>
<%--            <c:if test="${bookDto.authors.length > 1}">--%>
<%--                <c:forEach var="author" items="${bookDto.authors}" begin="1">--%>
<%--                    <span>, ${author}</span><br>--%>
<%--                </c:forEach>--%>
<%--            </c:if>--%>


            <span>${bookDto.publishDate}</span>

        </div>
    </c:forEach>
</div>
</body>
</html>