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
    <%@include file="jspf/sidebar.jspf" %>

    <div class="loginForm">
      <div class="logincontent fl">
        <p>${errorMsg}</p> <%--TODO 没有错误样式--%>
        <h3>用户登录</h3>
        <form action="<c:url value="/login" />" method="POST">
          <table width="100%">
            <colgroup>
              <col width="50">
              <col width="140">
            </colgroup>
            <tbody>
              <tr>
                <td><label for="username" class="loginLabel">登录邮箱：</label></td>
                <td><input type="email" id="username" name="username" placeholder="您注册的Email" required = "required" /></td>
              </tr>
              <tr>
                <td><label for="password" class="loginLabel">密码：</label></td>
                <td><input type="password" id="password" name="password" placeholder="请输入密码" required = "required" /></td>
              </tr>
              <tr>
                <td></td>
                <td><input type="submit" name="loginSubmit" value="登录" /></td>
              </tr>
            </tbody>
          </table>
        </form>
      </div>

      <div class="regcontent fl">
        <h3>新用户注册</h3>
        <form:form id="registerForm" action="/register" modelAttribute="newUser" onsubmit="return InputCheck(this)">
          <table width="100%">
            <colgroup>
              <col width="55">
              <col width="140">
            </colgroup>
            <tbody>
              <tr>
                <td><label for="regLogin">邮 箱:</label></td>
                <td>
                  <form:errors path="login" />
                  <form:input id="regLogin" type="email" path="login" placeholder="请输入您的个人邮箱" required = "required" />
                </td>
              </tr>
              <tr>
                <td><label for="regPass">密 码:</label></td>
                <td>
                  <form:errors path="pass" />
                  <form:password id="regPass" path="pass" placeholder="请输入您的密码" required = "required" />
                </td>
              </tr>
              <tr>
                <td><label for="reRegPass">确认密码:</label></td>
                <td><input type="password" id="reRegPass" placeholder="请再输入一次密码" required="required" /></td>
              </tr>
              <tr>
                <td><label for="displayName">昵 称:</label></td>
                <td><form:input id="displayName" path="displayName" placeholder="请输入您的昵称" required = "required" /></td>
              </tr>
              <tr>
                <td><label for="birthday">生 日:</label></td>
                <td><input type="date" id="birthday" name="birthday" placeholder="请选择您的生日" required = "required" /></td>
              </tr>
              <tr>
                <td><label>性 别:</label></td>
                <td>
                  <label for="male"><input type="radio" id="male" name="gender" value="male" />男</label>
                  <lable for="female"><input type="radio" id="female" name="gender" value="female" />女</lable>
                </td>
              </tr>
              <tr style="display: none"> <%--TODO 校验码--%>
                <td><label for="">校验码:</label></td>
                <td>blablabla
                  <!--动态读取验证码图片的src-->
                  <!--<img src="" alt="">-->
                </td>
              </tr>
              <tr>
                <td></td>
                <td><input type="submit" name="registerSubmit" value="注册" /></td>
              </tr>
            </tbody>
          </table>
        </form:form>
      </div>
    </div>
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>
