<%--
    Document   : postList:文章列表; pageNum:当前页码; pageCount:页数;
    Created on : 2012-6-13, 6:06:27
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${user.displayName}的文章 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
    <h1>查看用户${user.displayName}的文章</h1>
    <ol>
      <c:forEach items="${postList}" var="post">
        <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a> 发布日期：<fmt:formatDate value="${post.createDate}" type="date" dateStyle="medium"/></li>
      </c:forEach>
    </ol>

    <ul class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <li><a href="<c:url value="/user/${user.id}/posts/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
      </c:forEach>
    </ul>

    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>