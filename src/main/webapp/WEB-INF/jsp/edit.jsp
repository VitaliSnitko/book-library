<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.time.temporal.ChronoUnit" %>
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
  <script defer src="../../js/add-record.js"></script>
  <script defer src="../../js/suggestion-email.js"></script>
  <script defer src="../../js/file-upload-validation.js"></script>
  <script defer src="../../js/edit-record.js"></script>
  <script defer src="../../js/delete-record.js"></script>
  <title>Book page</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <div class="navbar-nav">
        <a class="nav-link" aria-current="page" href="<c:url value="/main"/>">Home</a>
        <a class="nav-link active" href="<c:url value="/add"/>">Add book</a>
        <a class="nav-link" href="<c:url value="/search"/>" tabindex="-1">Search</a>
      </div>
    </div>
  </div>
</nav>
<c:set var="bookDto" value="${requestScope.bookDto}"/>
<c:choose>
  <c:when test="${bookDto.id == 0}">
    <div class="container mt-5">
      <input type="hidden" name="id" value="${param.id}"/>
      <div class="row justify-content-center">
        <div class="col-6">
          <p class="fs-2 text-center">Such book does not exist</p>
        </div>
      </div>
    </div>
  </c:when>
  <c:otherwise>
    <!-- Add Modal -->
    <div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form class="needs-validation record-form" action="javascript:void(0);" novalidate>
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel">Add Record</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <label for="recordEmail" class="form-label">Email</label>
              <div class="search-input">
                <input type="email" class="form-control mb-2" id="recordEmail" required>
                <div class="autocom-box">
                  <!-- here list are inserted from javascript -->
                </div>
              </div>
              <label for="recordName" class="form-label">Name</label>
              <input type="text" class="form-control mb-2" id="recordName" required>
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
    <!-- Edit Modal -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="exampleModalLabel2" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <form class="record-form" action="javascript:void(0);">
            <div class="modal-header">
              <h5 class="modal-title" id="exampleModalLabel2">Edit Record</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" id="editRecordId">
              <label for="editRecordEmail" class="form-label">Email</label>
              <input type="email" readonly class="form-control-plaintext mb-2" id="editRecordEmail">
              <label for="editRecordName" class="form-label">Name</label>
              <input type="text" readonly class="form-control-plaintext mb-2" id="editRecordName">
              <label for="editRecordBorrowDate" class="form-label">Borrow Date</label>
              <input type="date" readonly class="form-control-plaintext mb-2" id="editRecordBorrowDate">
              <label for="editRecordPeriod" class="form-label">Time period</label>
              <select class="form-select mb-2" id="editRecordPeriod" disabled>
                <option value="1">1 month</option>
                <option value="2">2 months</option>
                <option value="3">3 months</option>
                <option value="6">6 months</option>
                <option value="12">12 months</option>
              </select>
              <label for="editRecordStatus" class="form-label-plaintext">Status</label>
              <select class="form-select mb-2" id="editRecordStatus">
                <option value="returned">Returned</option>
                <option value="returned and damaged">Returned and damaged</option>
                <option value="lost">Lost</option>
              </select>
            </div>
            <div class="modal-footer">
              <button id="editRecordModalButton" type="submit" class="btn btn-primary me-3 save-record"
                      data-bs-dismiss="modal">Save
              </button>
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Discard</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <form class="needs-validation" action="edit" method="post" enctype="multipart/form-data" novalidate>

      <input type="hidden" name="bookId" value="${bookDto.id}">
      <!-- Book -->
      <div class="container mt-4">
        <input type="hidden" name="id" value="${param.id}"/>
        <div class="row mb-1 justify-content-center">
          <label for="validationCustom01" class="form-label col-sm-2 col-form-label">Title</label>
          <div class="col-sm-4">
            <input type="text" name="title" class="form-control" id="validationCustom01" value="${bookDto.title}"
                   required>
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
            <input type="text" name="publisher" class="form-control" id="validationCustom03"
                   value="${bookDto.publisher}"
                   required>
          </div>
        </div>
        <div class="row mb-1 justify-content-center">
          <label for="validationCustom04" class="form-label col-sm-2 col-form-label">Publish date</label>
          <div class="col-sm-4">
            <input type="date" name="date" class="form-control" id="validationCustom04" value="${bookDto.publishDate}"
                   required>
          </div>
        </div>
        <div class="row mb-1 justify-content-center">
          <label for="validationCustom05" class="form-label col-sm-2 col-form-label">Page count</label>
          <div class="col-sm-4">
            <input type="text" name="pageCount" class="form-control" id="validationCustom05"
                   value="${bookDto.pageCount}"
                   required pattern="\d+">
          </div>
        </div>
        <div class="row mb-1 justify-content-center">
          <label for="validationCustom06" class="form-label col-sm-2 col-form-label">ISBN</label>
          <div class="col-sm-4">
            <input type="text" name="ISBN" class="form-control" id="validationCustom06" value="${bookDto.ISBN}"
                   required>
          </div>
        </div>
        <div class="row mb-1 justify-content-center">
          <label for="editTotalAmount" class="form-label col-sm-2 col-form-label">Total book amount</label>
          <div class="col-sm-4">
            <input type="text" name="totalBookAmount" class="form-control" id="editTotalAmount"
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
          <label for="editStatus" class="form-label col-sm-2 col-form-label">Status</label>
          <div class="col-sm-4">
            <c:choose>
              <c:when test="${bookDto.availableBookAmount == 0}">
                <input class="form-control-plaintext" type="text" readonly
                       value="Unavailable (expected to become available on ${requestScope.nearestAvailableDate})"
                       id="editStatus">
              </c:when>
              <c:otherwise>
                <input class="form-control-plaintext" type="text" readonly
                       value="Available ${bookDto.availableBookAmount} out of ${bookDto.totalBookAmount}"
                       id="editStatus">
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <div class="row mb-2 justify-content-center">
          <div class="col-sm-6">
            <button class="btn btn-primary me-3" type="submit">Save</button>
            <a class="btn btn-secondary" href="<c:url value="/main"/>" role="button">Discard</a>
          </div>
        </div>

        <div class="row justify-content-center mt-3">
          <div class="col-sm-6">
            <c:choose>
              <c:when test="${bookDto.availableBookAmount == 0}">
                <div data-bs-toggle="tooltip" data-bs-placement="right" title="No books available">
                  <button id="addRecordButton" type="button" class="btn btn-primary" data-bs-toggle="modal"
                          data-bs-target="#addModal" disabled>Add Record
                  </button>
                </div>
              </c:when>
              <c:otherwise>
                <button id="addRecordButton" type="button" class="btn btn-primary" data-bs-toggle="modal"
                        data-bs-target="#addModal">Add Record
                </button>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
        <!-- Active Records -->
        <div class="row justify-content-center mt-2">
          <div class="col-sm-6">
            <h6 class="text-muted">Active Records</h6>
            <div class="list-group">
              <c:forEach var="record" items="${requestScope.activeRecords}">
                <div class="list-group-item list-group-item-action d-flex" style="position:relative;">
                  <div class="flex-column">
                    <input type="hidden" name="status" value="borrowed">
                    <input type="hidden" name="recordId" value="${record.id}">
                    <input type="hidden" name="borrowDate" value="${record.borrowDate}">
                    <input type="hidden" name="dueDate" value="${record.dueDate}">
                    <input type="hidden" name="periodCalculated"
                           value="${ChronoUnit.MONTHS.between(record.borrowDate, record.dueDate)}">
                    <a href="javascript:void(0);" class="${record.reader.name}" data-bs-toggle="modal"
                       data-bs-target="#editModal">${record.reader.name}</a>
                    <p><small class="${record.reader.email}">${record.reader.email}</small></p>
                    <span class="badge rounded-pill bg-info"></span>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
        <!-- Record History -->
        <div class="row justify-content-center mt-2">
          <div class="col-sm-6">
            <h6 class="text-muted">Record History</h6>
            <div class="list-group">
              <c:forEach var="record" items="${requestScope.inactiveRecords}">
                <div class="list-group-item list-group-item-action d-flex" style="position:relative;">
                  <div class="flex-column">
                    <a href="javascript:void(0);" class="text-secondary text-decoration-none">${record.reader.name}</a>
                    <br><small>${record.reader.email}</small><br>
                    <small>
                      <span class="font-weight-normal">Return date:</span>
                      <span class="font-weight-light">${record.returnDate}</span>
                    </small>
                    <span class="badge rounded-pill bg-info"></span>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </form>


    <%--<div style="position:relative;" class="list-group-item list-group-item-action d-flex">--%>
    <%--  <input type="hidden" name="email" value="${name}">--%>
    <%--  <input type="hidden" name="name" value="${email}">--%>
    <%--  <input type="hidden" name="period" value="${period}">--%>
    <%--  <div style="position: absolute; right: 0" data-bs-toggle="tooltip" data-bs-placement="right"--%>
    <%--       title="Record was not created. Press 'Save' to save changes or 'Discard' to discard">--%>
    <%--    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="#ffc107"--%>
    <%--         class="bi bi-exclamation-triangle-fill flex-shrink-0 me-2" viewBox="0 0 16 16" role="img"--%>
    <%--         aria-label="Warning:">--%>
    <%--      <path--%>
    <%--          d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>--%>
    <%--    </svg>--%>
    <%--  </div>--%>
    <%--  <div class="trash-icon" style="position: absolute; right: 12; bottom: 4" onclick="deleteRecord(this)">--%>
    <%--    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash"--%>
    <%--         viewBox="0 0 16 16">--%>
    <%--      <path--%>
    <%--          d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>--%>
    <%--      <path fill-rule="evenodd"--%>
    <%--            d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>--%>
    <%--    </svg>--%>
    <%--  </div>--%>
    <%--  <div class="flex-column">--%>
    <%--    <a href="#">${name}</a>--%>
    <%--    <p><small>${email}</small></p>--%>
    <%--    <span class="badge rounded-pill bg-info"></span>--%>
    <%--  </div>--%>
    <%--</div>--%>


    <script>
        let emailSuggestions = [
            <c:forEach var="email" items="${requestScope.emailSuggestions}">
            "${email}",
            </c:forEach>
        ];
        let nameSuggestions = [
            <c:forEach var="name"  items="${requestScope.nameSuggestions}">
            "${name}",
            </c:forEach>
        ]
    </script>
  </c:otherwise>
</c:choose>
</body>
</html>
