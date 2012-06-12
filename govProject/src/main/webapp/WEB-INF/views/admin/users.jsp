<%--
    Document   : users
    Created on : 2012-6-11, 15:51:20
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
      <%@include file="jspf/functionBar.jspf" %>
    </div>
    <!--Start MainContent-->
${user.commentList} ${user.login} ${user.displayName} ${user.postList} ${user.registerDate}
    ${user.siteLogList} ${user.url} ${user.userLevel} ${user.usermetaList}
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>