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
    <!--Start MainContent-->
    <c:forEach items="${categories}" var="category">
      <h2>${category.name}</h2>
      <ol>
        <c:forEach items="${requestScope[category.name]}" var="post">
          <li>${post.tittle}</li>
        </c:forEach>
      </ol>
    </c:forEach>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>
