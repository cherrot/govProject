<%--
    Document   : users 需要注入List<User>对象userList,每个User的roleList，用户的文章数postCountList、评论数commentCountList
    Created on : 2012-6-11, 15:51:20
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>用户管理 | 昆明文化辞典</title>
  </head>
  <body>
    <div id="header">
      <%@include file="jspf/header.jspf" %>
    </div>
    <div id="sidebar" >
      <%@include file="jspf/functionBar.jspf" %>
    </div>
    <!--Start MainContent-->
    <div>
      <table>
        <thead>
          <tr>
            <th>用户名</th>
            <th>用户昵称</th>
            <th>注册时间</th>
            <th>查看详细资料</th>
            <th>查看用户文章</th>
            <th>查看用户评论</th>
            <th>编辑用户</th>
            <th>删除用户</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${userList}" var="user" varStatus="status">
            <tr>
              <td>${user.login}</td>
              <td>${user.displayName}</td>
              <td>${user.registerDate}</td>
              <td><a href="<c:url value="/admin/user?id=${user.id}" />">详细资料</a></td>
              <td><a href="<c:url value="/admin/post/list?userId=${user.id}"/>" title="查看${user.displayName}的全部文章">共${postCountList[status.index]}篇文章</a></td>
              <td><a href="<c:url value="/admin/comment/list?userId=${user.id}"/>" title="查看${user.displayName}的全部评论">共${commentCountList[status.index]}条评论</a></td>
              <td><a href="<c:url value="/admin/user/edit?id=${user.id}" />">编辑</a></td>
              <td><a href="<c:url value="/admin/user/delete?id=${user.id}" />">删除</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <div id="pageNav">
      <!--分页-->
    </div>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>