<%--
    Document   : categories
    Created on : 2012-6-17, 17:14:04
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="../jspf/commonHead.jspf" %>
    <title>编辑文章标签 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="../jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="mainContent">
      <h1>文章文章标签</h1>
      <div>
        <h2>编辑标签：${tag.name}</h2>
        <form:form modelAttribute="tag">
          <form:errors path="*"/><br/>
          <label for="tag_name">标签名：</label>
          <form:input path="name" id="tag_name" placeholder="请输入文章标签的名字"/><br/>
          <label for="tag_slug">标签短链接</label>
          <form:input path="slug" id="tag_slug" placeholder="可以和分类名相同"/><br/>
          <%--<label for="tag_description">标签描述（可选）</label>
          <form:textarea path="description" id="tag_description"/>--%>
          <form:hidden path="id"/>
          <input type="submit" value="提交"/>
        </form:form>
      </div>
    </div>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>