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
    <title>编辑文章评论 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="../jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="mainContent">
      <h1>文章评论管理</h1>
      <div>
        <h2>编辑评论</h2>
        <form:form modelAttribute="comment">
          <form:errors path="*"/><br/>
          <label for="comment_approved">批准：</label>
          <form:checkbox path="approved" id="comment_approved"/><br/>
          <label for="comment_author">评论人姓名：</label>
          <form:input path="author" id="comment_author" placeholder="评论人显示的名字" required="required"/><br/>
          <label for="comment_email">评论人邮箱：</label>
          <form:input type="email" path="authorEmail" id="comment_email" required="required"/><br/>
          <label for="comment_url">评论人网址（可选）：</label>
          <form:input type="url" path="authorUrl" id="comment_url"/><br/>
          <label for="comment_content">评论内容</label>
          <form:textarea path="content" id="comment_content" required="required"/>
          <form:hidden path="id"/>
          <input type="submit" value="提交"/>
        </form:form>
      </div>
    </div>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>
