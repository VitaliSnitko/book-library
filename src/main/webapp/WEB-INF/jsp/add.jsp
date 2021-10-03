<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Book</title>
</head>
<body>
<form action="add" method="post" enctype="multipart/form-data">
    <input type="text" placeholder="Title" name="title"><br>
    <input type="text" placeholder="Author(s) (divided by ',')" name="authors"><br>
    <input type="text" placeholder="Genre(s) (divided by ',')" name="genres"><br>
    <input type="text" placeholder="Publisher" name="publisher"><br>
    <input type="date" placeholder="Publish date" name="date"><br>
    <input type="text" placeholder="Page count" name="page count"><br>
    <input type="text" placeholder="ISBN" name="ISBN"><br>
    <textarea placeholder="Description" name="description"></textarea><br>
    <input type="file" accept="image/jpeg,image/png" name="cover"><br>
    <input type="submit" value="Submit">
</form>
</body>
</html>
