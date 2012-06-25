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
      <form:form modelAttribute="user" onsubmit="return InputCheck(this)">
        <form:errors path="*"/>
        <label for="user_login">登陆邮箱：</label>
        <form:input type="email" id="user_login" path="login" placeholder="登陆邮箱" required="required"/><br/>
        <label for="user_displayName">显示昵称：</label>
        <form:input id="user_displayName" path="displayName" placeholder="显示昵称" required="required"/><br/>
        <label for="user_url">个人主页（可选）：</label>
        <form:input type="url" path="url" placeholder="个人主页"/><br/>
        <label for="user_pass">修改密码（不改请留空）：</label>
        <form:password path="pass"/><br/>

        <c:if test="${!empty roleMap}">
          <label for="user_level">用户等级：</label>
          <form:select id="user_level" items="${roleMap}" path="userLevel"/><br/>
        </c:if>

        <form:hidden path="id" value="${user.id}"/>
        <input type="submit"/>
      </form:form>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
