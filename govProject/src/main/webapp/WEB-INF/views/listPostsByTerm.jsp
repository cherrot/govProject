<%--
    Document   : postList:根据分类或标签筛选的文章列表; term:分类或标签; pageNum:当前页码; pageCount:页数;
    Created on : 2012-6-13, 6:06:27
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${term.name} | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>

    <!--Start MainContent-->
    <c:catch var="ex">
        <%--判断term是目录还是标签--%>
        ${term.categoryParent}
    </c:catch>
    <c:choose>
      <c:when test="${ex == null}">
        <c:set value="category" var="type" />
        <c:set value="目录" var="typeString"/>
      </c:when>
      <c:otherwise>
        <c:set value="tag" var="type" />
        <c:set value="标签" var="typeString"/>
      </c:otherwise>
    </c:choose>

    <h1>文章${typeString}：${term.name}</h1>
    <ol>
      <c:forEach items="${postList}" var="post">
        <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
      </c:forEach>
    </ol>

    <ul class="pageNav">
      页码：
      <c:forEach begin="1" end="${pageCount}" varStatus="status">
        <li><a href="<c:url value="/${type}/${term.slug}/page/${status.count}"/>" <c:if test="${status.count == pageNum}">style="color: red;"</c:if>>${status.count}</a></li>
      </c:forEach>
    </ul>

    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>