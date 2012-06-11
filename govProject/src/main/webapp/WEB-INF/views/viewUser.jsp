<%--
    Document   : viewUser
    本页面需要注入User对象user，用户角色String描述userRole
    Created on : 2012-6-11, 15:50:35
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

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
      <%@include file="jspf/sidebar.jspf" %>
    </div>
    <!--Start MainContent-->
    ${user.commentList} ${user.login} ${user.displayName} ${user.postList} ${user.registerDate}
    ${user.siteLogList} ${user.url} ${user.userLevel} ${user.usermetaList}
    <h1>${user.displayName} 的个人资料</h1>
    <div>
      <p><em>昵称：</em>${user.displayName}</p>
      <p><em>登录邮箱：</em>${user.login}</p>
      <p><em>注册时间：</em>${user.registerDate}</p>
      <p><em>个人主页：</em>${user.url}</p>
      <p><em>角色：</em>${userRole}</p>
    </div>
    
    <div>
      <!--分页显示用户的评论和文章-->
    </div>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>