<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
          integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
          crossorigin="anonymous"></script>
  <title>Search</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <div class="navbar-nav">
        <a class="nav-link" aria-current="page" href="/main">Home</a>
        <a class="nav-link" href="/add">Add book</a>
        <a class="nav-link active" href="/search" tabindex="-1">Search</a>
      </div>
    </div>
  </div>
</nav>
<div class="container h-100">
  <div class="row h-100 justify-content-center align-items-center">
    <div class="col">
      <form action="main" method="get">
        <div class="row mb-5 justify-content-center">
          <div class="col-3 text-center">
            <h2>Search</h2>
          </div>
        </div>
        <div class="row mb-2 justify-content-center">
          <label for="title" class="form-label col-sm-1 col-form-label">Title</label>
          <div class="col-sm-4">
            <input type="text" name="title" class="form-control" id="title">
          </div>
        </div>
        <div class="row mb-2 justify-content-center">
          <label for="authors" class="form-label col-sm-1 col-form-label">Authors</label>
          <div class="col-sm-4">
            <input type="text" name="authors" class="form-control" id="authors">
          </div>
        </div>
        <div class="row mb-2 justify-content-center">
          <label for="genres" class="form-label col-sm-1 col-form-label">Genres</label>
          <div class="col-sm-4">
            <input type="text" name="genres" class="form-control" id="genres">
          </div>
        </div>
        <div class="row mb-2 justify-content-center">
          <label for="description" class="form-label col-sm-1 col-form-label">Description</label>
          <div class="col-sm-4">
            <input type="text" name="description" class="form-control" id="description">
          </div>
        </div>
        <div class="row mt-4 justify-content-center">
          <div class="col-sm-5">
            <button class="btn btn-primary me-3" type="submit">Search</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
</body>
</html>
