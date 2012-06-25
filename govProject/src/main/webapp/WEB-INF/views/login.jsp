<%--
    Document   : login
    Created on : 2012-6-6, 8:27:11
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <script language=JavaScript>
      //<!--
      function InputCheck(regForm)
      {
        if (regForm.reRegPass.value != regForm.regPass.value)
        {
          alert("两次密码不一致!");
          regForm.reRegPass.focus();
          return (false);
        }
      }
      //-->
    </script>
    <%@include file="jspf/commonHead.jspf" %>
    <title>用户登录 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      
      <div style="text-align:center;">
    <p>${errorMsg}</p>
    <table align="center">
        <tr>
            <td width="300px" valign="top">
    <h1>用户登录</h1>
    <div class="loginForm">
      <form action="<c:url value="/login" />" method="POST">
        <fieldset>
          <label for="username" class="loginLabel">登录邮箱：</label>
          <input type="email" name="username" placeholder="您注册的Email" required = "required" /><br />
          <label for="password" class="loginLabel">密码：</label>
          <input type="password" name="password" placeholder="请输入密码" required = "required" /><br />
          <input type="submit" name="loginSubmit" value="登录" />
        </fieldset>
      </form>
    </div>
        </td>
        <td width="450px" vlign="top">
    <h1>新用户注册</h1>
    <div class="registerForm">
      <form:form id="registerForm" action="/register" modelAttribute="newUser" onsubmit="return InputCheck(this)">
        <fieldset>
          <form:errors path="*" />
          <form:errors path="login" />
          <form:input id="regLogin" type="email" path="login" placeholder="请输入您的个人邮箱" required = "required" /><br />
          <form:errors path="pass" />
          <form:password id="regPass" path="pass" placeholder="请输入您的密码" required = "required" /><br />
          <input type="password" id="reRegPass" placeholder="请再输入一次密码" required="required" /><br />
          <form:input path="displayName" placeholder="请输入您的昵称" required = "required" /><br />
          <input type="date" name="birthday" placeholder="请选择您的生日" required = "required" /><br />
          <label for="male"><input type="radio" id="male" name="gender" value="male" />男</label>
          <lable for="female"><input type="radio" id="female" name="gender" value="female" />女</lable><br />
          <input type="submit" name="registerSubmit" value="注册" />
        </fieldset>
      </form:form>
    </div>
    </td>
    <td width="200px" align="left" vlign="top"><%@include file="jspf/sidebar.jspf" %></td>
    </tr></table>

      <%@include file="jspf/footer.jspf" %>
      </div>
  </body>
</html>
