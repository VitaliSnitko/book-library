<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
  <link rel="stylesheet" href="../../css/main-page.css">
  <script defer src="../../js/main-page.js"></script>
  <title>Book Library</title>
</head>
<body>



<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <div class="container">
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <div class="navbar-nav">
        <a class="nav-link active" aria-current="page" href="/main">Home</a>
        <a class="nav-link" href="/add">Add book</a>
        <a class="nav-link" href="/search" tabindex="-1">Search</a>
      </div>
    </div>
  </div>
</nav>
<div class="container">
  <form action="delete" method="post">
    <div class="row mt-2">
      <div class="col-12 col-sm-8 col-lg-5">
        <div class="list-group">
          <c:forEach var="bookDto" items="${requestScope.bookList}">
            <a href="/edit?id=${bookDto.id}" class="list-group-item list-group-item-action d-flex">
              <div class="image-parent">
                <c:choose>
                  <c:when test="${bookDto.base64Cover.equals(\"\")}">
                    <img class="img-fluid rounded cover" src="../../images/placeholder-cover.png"
                         alt="lorem">
                  </c:when>
                  <c:otherwise>
                    <img class="img-fluid rounded cover"
                         src="data:image/jpeg;base64,${bookDto.base64Cover}"
                         alt="lorem">
                  </c:otherwise>
                </c:choose>
              </div>
              <div class="flex-column ms-3">
                <h4>${bookDto.title}</h4>
                <p><small>
                    ${bookDto.authorDtos.get(0).name}
                  <c:if test="${bookDto.authorDtos.size() > 1}">
                    <c:forEach var="authorEntity" items="${bookDto.authorDtos}" begin="1">
                      , ${authorEntity.name}
                    </c:forEach>
                  </c:if>
                </small></p>
                <span class="badge rounded-pill bg-info"> ${bookDto.availableBookAmount} Copies in Stock</span>
                <small class="publish-date text-muted"><b>Publish date:</b> ${bookDto.publishDate}
                </small>
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" name="delete" value="${bookDto.id}">
                </div>
              </div>
            </a>
          </c:forEach>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="alert alert-danger d-none position-fixed bottom-0 end-0 col-2" role="alert">
        Delete selected books?
        <button class="btn btn-danger" type="submit">Ok</button>
      </div>
    </div>
  </form>
  <c:set var="pageAmount" value="${requestScope.pageAmount}"/>
  <nav aria-label="Page navigation example">
    <ul class="pagination">
      <c:if test="${param.page != null && param.page != 1}">
        <li class="page-item">
          <a class="page-link" href="/main?page=${param.page - 1}" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
      </c:if>

      <c:forEach var="pageNum" begin="1" end="${pageAmount}">
        <li class="page-item"><a class="page-link" href="/main?page=${pageNum}">${pageNum}</a></li>
      </c:forEach>

      <c:if test="${param.page != pageAmount}">
        <c:set var="currentPage" value="${param.page}"/>
        <c:if test="${currentPage == null}"><c:set var="currentPage" value="1"/></c:if>
        <li class="page-item">
          <a class="page-link" href="/main?page=${currentPage + 1}" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </c:if>
    </ul>
  </nav>
</div>

</body>
</html>