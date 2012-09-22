<%--
    Document   : posts 需要注入Post列表 postList，用于分页的 pageSize, pageNum, pageCount
    Created on : 2012-6-11, 15:51:46
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--只有scope为session时才会覆盖用户浏览器设置--%>
<fmt:setLocale value="zh_CN" scope="session"/>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="../jspf/commonHead.jspf" %>
    <title>文章管理 | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="../jspf/header.jspf" %>
      <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="formTable">
      <table width="100%">
        <thead>
          <tr>
            <th>标题</th>
            <th>作者</th>
            <th>发布时间</th>
            <th>所属分类</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${postList}" var="post">
            <tr>
              <td><a href="<c:url value="/post/${post.slug}" />">${post.title}</a></td>
              <td>${post.user.displayName}</td>
              <td><fmt:formatDate value="${post.createDate}" type="date" dateStyle="full"/></td>
              <td>
                <c:forEach items="${post.categoryList}" var="category">
                  <a href="<c:url value="/admin/category/${categoryId}"/>">${category.name}</a>&nbsp;
                </c:forEach>
              </td>
              <td>
                <a href="<c:url value="/admin/post/${post.id}" />">编辑</a>&nbsp;
                <a href="<c:url value="/admin/post/${post.id}/delete" />">删除</a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <div class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
          <c:choose>
            <c:when test="${status.count == pageNum}">
              <span>${status.count}&nbsp;</span>
            </c:when>
            <c:otherwise>
              <a href="<c:url value="/admin/post/page/${status.count}"/>">${status.count}</a>
            </c:otherwise>
          </c:choose>
      </c:forEach>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
