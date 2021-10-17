<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
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
    <input type="text" placeholder="Page count" name="pageCount"
           value="${bookDto.pageCount}"><br>
    <input type="text" placeholder="ISBN" name="ISBN"
           value="${bookDto.ISBN}"><br>
    <textarea placeholder="Description" name="description">${bookDto.description}</textarea><br>
    <span>Available ${bookDto.availableBookAmount} out of </span>
    <input type="text" placeholder="Total book amount" name="totalBookAmount"
           value="${bookDto.totalBookAmount}"><br>
    <input type="file" accept="image/jpeg,image/png" name="cover"><br>
    <input type="submit" value="Edit book"><br>
</form><br><br><br>

<form action="add-record" method="post">
    <input type="hidden" name="bookId" value="${bookDto.id}">
    <input id="email" type="text" placeholder="Email" name="email"><br>
    <input id="name" type="text" placeholder="Reader name" name="name"><br>
    <select name="period">
        <option value="1">1 month</option>
        <option value="2">2 months</option>
        <option value="3">3 months</option>
        <option value="6">6 months</option>
        <option value="12">12 months</option>
    </select><br>
    <input type="submit" value="Add record"><br><br>
</form>
<c:forEach var="record" items="${requestScope.records}">
    <div>
        <span class="email">${record.reader.email}</span>, <span class="name">${record.reader.name}</span>,
        <span>${record.borrowDate}, ${record.dueDate}</span><br>
    </div>
</c:forEach>

<script>
    let names = document.getElementsByClassName("name");
    for (let i = 0; i < names.length; i++) {
        names[i].addEventListener('click', () => editRecord(i));
    }

    function editRecord(i) {
        document.getElementById("email").setAttribute("value", document.getElementsByClassName("email")[i].textContent);
        document.getElementById("name").setAttribute("value", document.getElementsByClassName("name")[i].textContent);
    }
</script>
</body>
</html>
