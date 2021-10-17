<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Book Library</title>
    <%--    <link rel="stylesheet" href="../../css/main-page.css">--%>
    <style>
        a {
            text-decoration: none;
            color: black;
        }

        .selected a {
            color: white;
        }

        .add-book {
            position: relative;
            height: 48px;
            width: 120px;
            border-radius: 24px;
            color: #3c4043;
            box-shadow: 0 1px 2px 0 rgb(60 64 67 / 30%), 0 1px 3px 1px rgb(60 64 67 / 15%);
        }

        .add-book:hover {
            box-shadow: 0 1px 3px 0 rgb(60 64 67 / 30%), 0 4px 8px 3px rgb(60 64 67 / 15%);
            background-color: #fafafb;
        }

        .plus-icon {
            position: absolute;
            top: 50%;
            left: 8px;
            transform: translate(0, -50%);
        }

        .add-book-text {
            position: absolute;
            top: 50%;
            left: 40px;
            transform: translate(0, -50%);
            margin-right: 5px;
            font-family: 'Google Sans', Roboto, RobotoDraft, Helvetica, Arial, sans-serif;
            font-size: .875rem;
        }

        .book {
            position: relative;
            width: 700px;
            height: 150px;
            border: 1px black solid;
            padding: 10px;
            margin-top: 10px;
        }

        .cover {
            float: left;
            width: 100px;
            height: 150px;
            margin-right: 20px;
        }

        .title {
            font-size: 24px;
            font-family: "Arial Black", serif;
        }

        .publish-date {
            position: absolute;
            bottom: 5px;
            right: 5px;
        }

        .authors {
            font-family: Arial, serif;
            font-size: 16px;
        }

        /*.delete-icon {*/
        /*    position: absolute;*/
        /*    top: 3px;*/
        /*    right: 3px;*/
        /*    width: 30px;*/
        /*    height: 30px;*/
        /*}*/
        .checkbox {
            display: none;
        }

        .checkbox + label {
            background: url("../../images/delete-icon.png") no-repeat;
            position: absolute;
            top: 3px;
            right: 3px;
            width: 30px;
            height: 30px;
            background-size: 100%;
        }

        .selected {
            background-color: #614ad3;
        }

        .delete-message {
            width: 400px;
            height: 90px;
            position: fixed;
            bottom: 0;
            right: 30px;
            border-radius: 6px 6px 0 0;
            background-color: #5338d9;
            font-size: 27px;
            font-family: "Arial Black", serif;
            color: #e53c6f;
            text-align: center;
        }

        .ok {
            background-color: Transparent;
            font-size: 30px;
            text-align: center;
            color: #dc1a55;
            font-family: "Arial Black", serif;
        }

        .ok:hover {
            color: whitesmoke;
        }

        .ok:active {
            position: relative;
            top: 3px;
            font-size: 24px;
        }

        .hidden {
            display: none;
        }
    </style>
</head>
<body>
<a href="<c:url value="/add"/>">
    <div class="add-book">
        <img class="plus-icon" src="../../images/plus-icon.png" alt="add">
        <span class="add-book-text">Add book</span>
    </div>
</a>

<form action="delete" method="post">
    <div id="book list">

        <c:forEach var="bookDto" items="${requestScope.bookList}">
            <div class="book">
                <c:choose>
                    <c:when test="${bookDto.base64Cover.equals(\"\")}">
                        <img class="cover" src="../../images/placeholder-cover.png" alt="lorem">
                    </c:when>
                    <c:otherwise>
                        <img class="cover" src="data:image/jpeg;base64,${bookDto.base64Cover}" alt="lorem">
                    </c:otherwise>
                </c:choose>
                <a href="<c:url value="/edit?id=${bookDto.id}"/>">
                    <span class="title">${bookDto.title}</span>
                </a><br>

                <input class="checkbox" type="checkbox" name="delete" value="${bookDto.id}" id="${bookDto.id}">
                <label for="${bookDto.id}"></label>

                <span class="authors">${bookDto.authorDtos.get(0).name}</span>
                <c:if test="${bookDto.authorDtos.size() > 1}">
                    <c:forEach var="authorEntity" items="${bookDto.authorDtos}" begin="1">
                        <span class="authors">, ${authorEntity.name}</span>
                    </c:forEach>
                </c:if>
                <br>
                <span class="publish-date"><b>Publish date:</b> ${bookDto.publishDate}</span>
            </div>
        </c:forEach>
    </div>

<c:set var="pageAmount" value="${requestScope.pageAmount}"/>

<c:if test="${param.page != null && param.page != 1}">
    <a href="<c:url value="/main?page=${param.page - 1}"/>">< </a>
</c:if>

<c:forEach var="pageNum" begin="1" end="${pageAmount}">
    <a href="<c:url value="/main?page=${pageNum}"/>">${pageNum} </a>
</c:forEach>

<c:if test="${param.page != pageAmount}">
    <a href="<c:url value="/main?page=${param.page + 1}"/>"> ></a>
</c:if>
<div class="delete-message hidden">
    <span>Delete selected books?</span><br>
    <input class="ok" type="submit" value="Ok">
<%--    <a class="ok" href="<c:url value="/delete"/>">Ok</a>--%>
</div>
</form>
<script>
    let deleteIcons = document.getElementsByClassName("checkbox");
    let books = document.getElementsByClassName("book");
    let isDeleteMessageShown = false;

    function selectDelete(i) {
        books[i].classList.toggle("selected");
        if (document.getElementsByClassName("selected").length === 1 && isDeleteMessageShown === false
            || document.getElementsByClassName("selected").length === 0 && isDeleteMessageShown === true) {
            document.getElementsByClassName("delete-message")[0].classList.toggle("hidden");
            isDeleteMessageShown = !isDeleteMessageShown;
        }
    }

    for (let i = 0; i < deleteIcons.length; i++) {
        deleteIcons[i].addEventListener('click', () => selectDelete(i));
    }

    let deleteButton = document.getElementsByClassName("ok")[0];
    deleteButton.onmousedown = function () {
        let url = new URL(deleteButton.href);
        let books = document.getElementsByClassName('book');
        let ids = document.querySelectorAll('.book .id');
        for (let i = 0; i < books.length; i++) {
            if (books[i].classList.contains('selected')) {
                url.searchParams.append('id', ids[i].textContent);
            }
        }
        deleteButton.href = url;
    }


</script>
</body>
</html>