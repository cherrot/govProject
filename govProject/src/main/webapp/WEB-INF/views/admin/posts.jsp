<%--
    Document   : posts 需要注入Post列表 postList，用于分页的 pageSize, pageNum, pageCount
    Created on : 2012-6-11, 15:51:46
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>文章管理 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div>
      <table>
        <thead>
          <tr>
            <th>标题</th>
            <th>作者</th>
            <th>发布时间</th>
            <th>编辑</th>
            <th>删除</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${postList}" var="post">
            <tr>
              <td><a href="<c:url value="/post/${post.slug}" />">${post.title}</a></td>
              <td>${post.user.displayName}</td>
              <td>${post.createDate}</td>
              <td><a href="<c:url value="/admin/post/edit?id=${post.id}" />">编辑</a></td>
              <td><a href="<c:url value="/admin/post/delete?id=${post.id}" />">删除</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div class="pageNav">
      <!--分页-->
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
