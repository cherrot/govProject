<%--
    Document   : editPost
    Created on : 2012-6-6, 20:24:18
    Author     : sai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${post.title} | 昆明文化辞典</title>
  </head>
  <body>
    <div id="header">
      <%@include file="jspf/header.jspf" %>
    </div>
    <div id="sidebar" >
      <%@include file="jspf/sidebar.jspf" %>
    </div>
    <!--Start MainContent-->
    <form:form modelAttribute="post">

    </form:form>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>
