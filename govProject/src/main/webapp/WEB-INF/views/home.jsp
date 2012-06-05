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
        <li><a href="${category.slug}">${category.name}</a></li>
      </c:forEach>
    </ul>

    <c:forEach items="${linkCategories}" var="category">
      <ul>
        <c:forEach items="${category.linkList}" var="link">
          <li><a href="${link.url}" target="${link.target}" title="${link.description}" >${link.name}</a></li>
        </c:forEach>
      </ul>
    </c:forEach>
  </body>
  <div id="footer">
    <%@include file="jspf/footer.jspf" %>
  </div>
</html>
