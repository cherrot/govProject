<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%--@ page session="false" --%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
    <c:forEach items="${categoryGroups}" var="category" varStatus="status">
      文章群组${status.count}：
      <h2>${category.name}</h2>
      <ol>
        <c:forEach items="${requestScope[category.name]}" var="post">
          <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
        </c:forEach>
      </ol>
    </c:forEach>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
