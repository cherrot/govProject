<%--
    Document   : users 需要注入List<User>对象userList,每个User的userRole
    Created on : 2012-6-11, 15:51:20
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>${user.displayName} | 昆明文化辞典</title>
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
            <th>查看</th>
            <th>编辑</th>
            <th>删除</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${userList}" var="user">
            <tr>
              <td>${user.login}</td>
              <td>${user.displayName}</td>
              <td>${user.registerDate}</td>
              <td><a href="<c:url value="/admin/user?id=${user.id}" />">查看</a></td>
              <td><a href="<c:url value="/admin/user/edit?id=${user.id}" />">编辑</a></td>
              <td><a href="<c:url value="/admin/user/delete?id=${user.id}" />">删除</a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>