<%--
    Document   : editUser
    Created on : 2012-6-11, 15:50:43
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${user.displayName} | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div>
      <%--XXX 这里不规定form的action，以便使此form可以映射到/user/edit和/admin/user/edit两个路径--%>
      <form:form modelAttribute="user">
        <form:input path="displayName" placeholder="显示昵称" required="required"/><br/>
        <form:input type="url" path="url" placeholder="个人主页"/><br/>
        <form:hidden path="id" value="${user.id}"/>
        <input type="submit"/>
      </form:form>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
