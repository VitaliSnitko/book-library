<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Book</title>
</head>
<body>
<form action="add" method="post">
    <input type="text" placeholder="Title" name="title"><br>
    <input type="text" placeholder="Publisher" name="Publisher"><br>
    <input type="text" placeholder="Publish date" name="Publish date"><br>
    <input type="text" placeholder="Page count" name="Page count"><br>
    <input type="text" placeholder="ISBN" name="ISBN"><br>
    <textarea placeholder="Description" name="Description"></textarea><br>
    <input type="file" accept="image/jpeg,image/png" name="file"><br>
</form>
</body>
</html>
