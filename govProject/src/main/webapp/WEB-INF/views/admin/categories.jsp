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
    <title>文章分类管理 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="mainContent">
      <h1>文章分类管理</h1>
      <div>
        <h2>当前文章分类</h2>
        <ul>
          <c:forEach items="${categoryList}" var="category2nd">
            <li>
              ${category2nd.name} &nbsp;
              <a href="<c:url value="/admin/category/${category2nd.id}/edit"/>">编辑</a> &nbsp;
              <a href="<c:url value="/admin/category/${category2nd.id}/delete"/>">删除</a>
            </li>
            <c:if test="${!empty category2nd.categoryList}">
              <ul>
                <c:forEach items="${category2nd.categoryList}" var="category3rd">
                  <li>
                    ${category3rd.name} &nbsp;
                    <a href="<c:url value="/admin/category/${category3rd.id}/edit"/>">编辑</a> &nbsp;
                    <a href="<c:url value="/admin/category/${category3rd.id}/delete"/>">删除</a>
                  </li>
                </c:forEach>
              </ul>
            </c:if>
          </c:forEach>
        </ul>
      </div>

      <div>
        <h2>添加顶级文章分类</h2>
        <form:form modelAttribute="category">
          <form:errors path="*"/><br/>
          <label for="category2nd_name">分类名：</label>
          <form:input path="name" id="category2nd_name" placeholder="请输入文章分类的名字" required="required"/><br/>
          <label for="category2nd_slug">分类短链接</label>
          <form:input path="slug" id="category2nd_slug" placeholder="可以和分类名相同" required="required"/><br/>
          <label for="category2nd_description">分类描述（可选）</label>
          <form:textarea path="description" id="category2nd_description"/>
          <label for="category2nd_parent">所属分类群组</label>
          <select id="category2nd_parent" name="parent">
            <c:forEach items="${categoryGroups}" var="category1st">
              <option value="${category1st.id}">${category1st.name}</option>
            </c:forEach>
          </select>
          <input type="submit" value="提交"/>
        </form:form>
      </div>
      <div>
        <h2>添加二级文章分类</h2>
        <form:form modelAttribute="category">
          <form:errors path="*"/><br/>
          <label for="category3rd_name">分类名：</label>
          <form:input path="name" id="category3rd_name" placeholder="请输入文章分类的名字" required="required"/><br/>
          <label for="category3rd_slug">分类短链接</label>
          <form:input path="slug" id="category3rd_slug" placeholder="可以和分类名相同" required="required"/><br/>
          <label for="category3rd_description">分类描述（可选）</label>
          <form:textarea path="description" id="category3rd_description"/>
          <label for="category3rd_parent">所属二级分类</label>
          <select id="category3rd_parent" name="parent">
            <c:forEach items="${categoryList}" var="category2nd">
              <option value="${category2nd.id}">${category2nd.name}</option>
            </c:forEach>
          </select>
          <input type="submit" value="提交"/>
        </form:form>
      </div>
    </div>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>