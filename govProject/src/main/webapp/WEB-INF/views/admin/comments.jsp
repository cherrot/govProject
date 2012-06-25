<%--
    Document   : comments 需要pageNum:当前页码; pageCount:页数;
    Created on : 2012-6-11, 15:51:59
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>文章评论管理 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <h1>查看评论列表</h1>
    <c:url value="/admin/comment" var="onlyPending">
      <c:param name="pending" value="true"/>
    </c:url>
    <a href="${onlyPending}">只显示待审核评论</a><br/>
    <a href="<c:url value="/admin/comment"/>">显示全部评论</a>
    <table>
      <tr>
        <th>评论者</th>
        <th>评论时间</th>
        <th>评论文章</th>
        <th>评论内容</th>
        <th>操作</th>
      </tr>
      <c:forEach items="${commentList}" var="comment">
        <tr>
          <td>${comment.author}</td>
          <td><fmt:formatDate value="${comment.commentDate}" type="date" dateStyle="medium"/></td>
          <td><a href="<c:url value="/post/${comment.post.slug}"/>">${comment.post.title}</a></td>
          <td>${comment.content}</td>
          <td>
            <c:choose>
              <c:when test="${comment.approved}">
                <a href="<c:url value="/admin/comment/${comment.id}/edit?approved=false"/>">驳回</a>
              </c:when>
              <c:otherwise>
                <a href="<c:url value="/admin/comment/${comment.id}/edit?approved=true"/>">批准</a>
              </c:otherwise>
            </c:choose>
            <a href="<c:url value="/admin/comment/${comment.id}/edit"/>">编辑</a>&nbsp;
            <a href="<c:url value="/admin/comment/${comment.id}/delete"/>">删除</a>
          </td>
        </tr>
      </c:forEach>
    </table>
    <ul class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <li><a href="<c:url value="/admin/comment/${comment.id}/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
      </c:forEach>
    </ul>
  <!--End MainContent-->
  <%@include file="jspf/footer.jspf" %>
</body>
</html>
