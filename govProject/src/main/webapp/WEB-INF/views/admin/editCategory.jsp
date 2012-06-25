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
    <%@include file="jspf/commonHead.jspf" %>
    <title>编辑文章分类 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="mainContent">
      <h1>文章分类管理</h1>
      <div>
        <h2>编辑分类：{category.name}</h2>
        <form:form modelAttribute="category">
          <form:errors path="*"/><br/>
          <label for="category_name">分类名：</label>
          <form:input path="name" id="category_name" placeholder="请输入文章分类的名字"/><br/>
          <label for="category_slug">分类短链接</label>
          <form:input path="slug" id="category_slug" placeholder="可以和分类名相同"/><br/>
          <label for="category_description">分类描述（可选）</label>
          <form:textarea path="description" id="category_description"/>
          <label for="category_parent">所属分类群组</label>
          <select id="category_parent" name="parent">
            <c:forEach items="${categoryParents}" var="parent">
              <option value="${categoryParent.id}">${categoryParent.name}</option>
            </c:forEach>
          </select>
          <form:hidden path="id"/>
          <input type="submit" value="提交"/>
        </form:form>
      </div>
    </div>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>