<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
  <link rel="stylesheet" href="../../css/suggestion-email.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
          integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
          crossorigin="anonymous"></script>
  <script defer src="../../js/form-validation.js"></script>
  <script defer src="../../js/record-modal-validation.js"></script>
  <script defer src="../../js/record-adding.js"></script>
  <script defer src="../../js/suggestion-email.js"></script>
  <script defer src="../../js/max-file-size-upload.js"></script>
  <title>Book page</title>
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
<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form class="needs-validation record-form" action="javascript:void(0);" novalidate>
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Book Record</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <label for="recordEmail" class="form-label">Email</label>
          <div class="search-input">
            <input type="email" name="title" class="form-control mb-2" id="recordEmail" required>
            <div class="autocom-box">
              <!-- here list are inserted from javascript -->
            </div>
          </div>

          <label for="recordName" class="form-label">Name</label>
          <input type="text" name="title" class="form-control mb-2" id="recordName" required>
          <label for="recordPeriod" class="form-label">Time period</label>
          <select class="form-select mb-2" id="recordPeriod">
            <option value="1">1 month</option>
            <option value="2">2 months</option>
            <option value="3">3 months</option>
            <option value="6">6 months</option>
            <option value="12">12 months</option>
          </select>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary me-3 save-record">Save</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Discard</button>
        </div>
      </form>
    </div>
  </div>
</div>
<form class="needs-validation" action="edit" method="post" enctype="multipart/form-data" novalidate>
  <c:set var="bookDto" value="${requestScope.bookDto}"/>
  <input type="hidden" name="bookId" value="${bookDto.id}">
  <!-- Book -->
  <div class="container mt-4">


    <input type="hidden" name="id" value="${param.id}"/>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom01" class="form-label col-sm-2 col-form-label">Title</label>
      <div class="col-sm-4">
        <input type="text" name="title" class="form-control" id="validationCustom01" value="${bookDto.title}" required>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom02" class="form-label col-sm-2 col-form-label">Author(s)</label>
      <div class="col-sm-4">
        <div class="input-group has-validation">
          <input type="text" name="authors" class="form-control" id="validationCustom02"
                 value="${bookDto.authorDtos.get(0).name}<c:if test="${bookDto.authorDtos.size() > 1}"><c:forEach var="authorEntity" items="${bookDto.authorDtos}" begin="1">, ${authorEntity.name}</c:forEach></c:if>"
                 required
                 pattern="[[a-zA-Z '-]+ *(, *[a-zA-Z '-]+)*">
          <div class="invalid-feedback">
            Please, enter authors divided by ','
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustomUsername" class="form-label col-sm-2 col-form-label">Genre(s)</label>
      <div class="col-sm-4">
        <div class="input-group has-validation">
          <input type="text" name="genres" class="form-control" id="validationCustomUsername"
                 value="${bookDto.genreDtos.get(0).name}<c:if test="${bookDto.genreDtos.size() > 1}"><c:forEach var="genreEntity" items="${bookDto.genreDtos}" begin="1">, ${genreEntity.name}</c:forEach></c:if>"
                 aria-describedby="inputGroupPrepend" required pattern="[a-zA-Z '-]+ *(, *[a-zA-Z '-]+)*">
          <div class="invalid-feedback">
            Please, enter genres divided by ','
          </div>
        </div>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom03" class="form-label col-sm-2 col-form-label">Publisher</label>
      <div class="col-sm-4">
        <input type="text" name="publisher" class="form-control" id="validationCustom03" value="${bookDto.publisher}" required>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom04" class="form-label col-sm-2 col-form-label">Publish date</label>
      <div class="col-sm-4">
        <input type="date" name="date" class="form-control" id="validationCustom04" value="${bookDto.publishDate}" required>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom05" class="form-label col-sm-2 col-form-label">Page count</label>
      <div class="col-sm-4">
        <input type="text" name="pageCount" class="form-control" id="validationCustom05" value="${bookDto.pageCount}"
               required pattern="\d+">
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom06" class="form-label col-sm-2 col-form-label">ISBN</label>
      <div class="col-sm-4">
        <input type="text" name="ISBN" class="form-control" id="validationCustom06" value="${bookDto.ISBN}" required>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom08" class="form-label col-sm-2 col-form-label">Total book amount</label>
      <div class="col-sm-4">
        <input type="text" name="totalBookAmount" class="form-control" id="validationCustom08"
               value="${bookDto.totalBookAmount}" required pattern="\d+">
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="validationCustom07" class="form-label col-sm-2 col-form-label">Description</label>
      <div class="col-sm-4">
        <textarea class="form-control" name="description" id="validationCustom07">${bookDto.description}</textarea>
      </div>
    </div>
    <div class="row mb-1 justify-content-center">
      <label for="cover" class="form-label col-sm-2 col-form-label">Cover</label>
      <div class="col-sm-4">
        <input class="form-control" type="file" accept="image/jpeg,image/png" name="cover" id="cover">
      </div>
    </div>
    <div class="row mb-2 justify-content-center">
      <label for="validationCustom10" class="form-label col-sm-2 col-form-label">Status</label>
      <div class="col-sm-4">
        <input class="form-control-plaintext" type="text"
               value="Available ${bookDto.availableBookAmount} out of ${bookDto.totalBookAmount}"
               id="validationCustom10">
      </div>
    </div>
    <div class="row mb-2 justify-content-center">
      <div class="col-sm-6">
        <button class="btn btn-primary me-3" type="submit">Save</button>
        <a class="btn btn-secondary" href="/main" role="button">Discard</a>
      </div>
    </div>


    <!-- Record List -->
    <div class="row justify-content-center mt-3">
      <div class="col-sm-6">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal">Add Record
        </button>
      </div>
    </div>
    <div class="row justify-content-center mt-2">
      <div class="col-sm-6">
        <h6 class="text-muted">Record List</h6>
        <div class="list-group">
          <c:forEach var="record" items="${requestScope.records}">
            <div class="list-group-item list-group-item-action d-flex">
              <div class="flex-column">
                <a href="#">${record.reader.name}</a>
                <p><small>${record.reader.email}</small></p>
                <span class="badge rounded-pill bg-info"></span>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </div>
  </div>
</form>
<script>
    let suggestions = [
        <c:forEach var="suggestion" items="${requestScope.suggestions}">
        "${suggestion}",
        </c:forEach>
    ]
</script>
</body>
</html>
