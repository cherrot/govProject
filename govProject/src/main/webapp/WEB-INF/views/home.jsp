<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
  <head>
    <title>昆明文化辞典</title>
  </head>
  <body>
    <div id="header">
      <%@include file="jspf/header.jspf" %>
    </div>
    <div id="sidebar" >
      <%@include file="jspf/sidebar.jspf" %>
    </div>
    <ul>
      <c:forEach items="${categories}" var="category">
        <li><a href="<c:url value="/category/${category.slug}" />">${category.name}</a></li>
      </c:forEach>
    </ul>

    <c:forEach items="${categories}" var="category">
      <ol>
        <c:forEach items="${requestScope[category.name]}" var="post">
          <li>${post.title}</li>
        </c:forEach>
      </ol>
    </c:forEach>

    <c:forEach items="${linkCategories}" var="category">
      <h3>${category.name}</h3>
      
    </c:forEach>

  </body>
  <div id="footer">
    <%@include file="jspf/footer.jspf" %>
  </div>
</html>
