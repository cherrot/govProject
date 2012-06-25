<%--
    Document   : tags
    Created on : 2012-6-17, 17:14:12
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>文章标签管理 | 昆明文化辞典</title>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <div class="mainContent">
      <h1>文章标签管理</h1>
      <div>
        <table>
          <thead>
            <tr>
              <th>标签名</th>
              <th>标签短链接</th>
              <th>关联文章数</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${tagList}" var="tag">
              <tr>
                <td>${tag.name}</td>
                <td>${tag.slug}</td>
                <td>${tag.count}</td>
                <td>
                  <a href="<c:url value="/admin/tag/${tag.id}"/>">编辑</a>
                  <a href="<c:url value="/admin/tag/${tag.id}/delete"/>">删除</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>

      <ul class="pageNav">
        页码：
        <c:forEach begin="1" end="${pageCount}" varStatus="status">
          <li><a href="<c:url value="/admin/tag/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
        </c:forEach>
      </ul>
    </div>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>