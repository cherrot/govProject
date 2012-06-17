<%--
    Document   : adminPanel 需要注入userRole描述用户角色
    Created on : 2012-6-11, 23:34:14
    Author     : cherrot
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>控制面板 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div>
      <p>欢迎您，${loginUser.displayName} ！</p>
      <p>您的角色为： ${userRole}</p>
      <p>这是您的管理面板。</p>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>