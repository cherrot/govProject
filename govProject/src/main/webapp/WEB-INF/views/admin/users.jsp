<%--
    Document   : users 需要注入List<User>对象userList,每个User的roleList，用户的文章数postCountList、评论数commentCountList
    Created on : 2012-6-11, 15:51:20
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>用户管理 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/functionBar.jspf" %>
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
            <th>用户级别</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${userList}" var="user" varStatus="status">
            <tr>
              <td>${user.login}</td>
              <td>${user.displayName}</td>
              <td>${user.registerDate}</td>
              <td><a href="<c:url value="/user/${user.id}" />" target="_blank">详细资料</a></td>
              <td><a href="<c:url value="/user/${user.id}/posts"/>" title="查看${user.displayName}的全部文章" target="_blank">共${postCountList[status.index]}篇文章</a></td>
              <td><a href="<c:url value="/user/${user.id}/comments"/>" title="查看${user.displayName}的全部评论" target="_blank">共${commentCountList[status.index]}条评论</a></td>
              <td>${roleList4UserList[status.index]}</td>
              <td>
                <a href="<c:url value="/admin/user/${user.id}" />">编辑</a>&nbsp;
                <a href="<c:url value="/admin/user/${user.id}/delete" />">删除</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
    <ul class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <li><a href="<c:url value="/admin/user/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
      </c:forEach>
    </ul>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>