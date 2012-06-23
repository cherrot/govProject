<%--
    Document   : commentList:评论列表; pageNum:当前页码; pageCount:页数;
    Created on : 2012-6-13, 6:06:27
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${user.displayName}的评论 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
    <h1>查看用户${user.displayName}的评论</h1>
    <table>
      <tr>
        <th>评论者</th>
        <th>评论时间</th>
        <th>评论文章</th>
        <th>评论内容</th>
      </tr>
      <c:forEach items="${commentList}" var="comment">
        <tr>
          <td>${comment.author}</td>
          <td><fmt:formatDate value="${comment.commentDate}" type="date" dateStyle="medium"/></td>
          <td><a href="<c:url value="/post/${comment.post.slug}"/>">${comment.post.title}</a></td>
          <td>${comment.content}</td>
        </tr>
      </c:forEach>
    </table>

    <ul class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <li><a href="<c:url value="/user/${user.id}/comments/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
      </c:forEach>
    </ul>

    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>