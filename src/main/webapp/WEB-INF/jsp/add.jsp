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
  <link href="../../css/selection-color.css" rel="stylesheet">
  <script defer src="../../js/form-validation.js"></script>
  <script defer src="../../js/max-file-size-upload.js"></script>
  <title>Add Book</title>
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
        <a class="nav-link active" href="/add">Add book</a>
        <a class="nav-link" href="/search" tabindex="-1">Search</a>
      </div>
    </div>
  </div>
</nav>
<div class="container mt-4">
  <form class="needs-validation" action="add" method="post" enctype="multipart/form-data" novalidate>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom01" class="form-label col-sm-2 col-form-label">Title</label>
      <div class="col-sm-4">
        <input type="text" name="title" class="form-control" id="validationCustom01" required>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom02" class="form-label col-sm-2 col-form-label">Author(s)</label>
      <div class="col-sm-4">
        <div class="input-group has-validation">
          <input type="text" name="authors" class="form-control" id="validationCustom02" required
                 pattern="[a-zA-Z '-]+ *(, *[a-zA-Z '-]+)*">
          <div class="invalid-feedback">
            Please, enter authors divided by ','
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustomUsername" class="form-label col-sm-2 col-form-label">Genre(s)</label>
      <div class="col-sm-4">
        <div class="input-group has-validation">
          <input type="text" name="genres" class="form-control" id="validationCustomUsername"
                 aria-describedby="inputGroupPrepend" required pattern="[a-zA-Z '-]+ *(, *[a-zA-Z '-]+)*">
          <div class="invalid-feedback">
            Please, enter genres divided by ','
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom03" class="form-label col-sm-2 col-form-label">Publisher</label>
      <div class="col-sm-4">
        <input type="text" name="publisher" class="form-control" id="validationCustom03" required>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom04" class="form-label col-sm-2 col-form-label">Publish date</label>
      <div class="col-sm-4">
        <input type="date" name="date" class="form-control" id="validationCustom04" required>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom05" class="form-label col-sm-2 col-form-label">Page count</label>
      <div class="col-sm-4">
        <input type="text" name="pageCount" class="form-control" id="validationCustom05" required pattern="\d+">
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom06" class="form-label col-sm-2 col-form-label">ISBN</label>
      <div class="col-sm-4">
        <input type="text" name="ISBN" class="form-control" id="validationCustom06" required>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom08" class="form-label col-sm-2 col-form-label">Total book amount</label>
      <div class="col-sm-4">
        <input type="text" name="totalBookAmount" class="form-control" id="validationCustom08" required pattern="\d+">
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="validationCustom07" class="form-label col-sm-2 col-form-label">Description</label>
      <div class="col-sm-4">
        <textarea class="form-control" name="description" id="validationCustom07"></textarea>
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <label for="cover" class="form-label col-sm-2 col-form-label">Cover</label>
      <div class="col-sm-4">
        <input class="form-control" type="file" accept="image/jpeg,image/png" name="cover" id="cover">
      </div>
      <div class="invalid-feedback">
        Пожалуйста, предоставьте действующий почтовый индекс.
      </div>
    </div>
    <div class="row mb-3 justify-content-center">
      <div class="col-sm-6">
        <button class="btn btn-primary me-3" type="submit">Save</button>
        <a class="btn btn-secondary" href="/main" role="button">Discard</a>
      </div>
    </div>
  </form>
</div>
</body>
</html>
