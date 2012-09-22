<%--
    Document   : viewUser
    Created on : 2012-6-11, 15:50:35
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${user.displayName} | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
<div class="person">
  <div class="infoBlock">
    <h3>${user.displayName} 的个人资料</h3>
    <c:if test="${user eq sessionUser}">
      <p class="mb10"><a href="<c:url value="/user/edit"/>">编辑个人资料</a>&nbsp;&nbsp;&nbsp;<a href="<c:url value="/post/create"/>">新建文章</a></p>
    </c:if>
      <p>昵称：${user.displayName}</p>
      <c:if test="${user eq sessionUser}">
        <p>登录邮箱：${user.login}</p>
      </c:if>
      <p>注册时间：<fmt:formatDate value="${user.registerDate}" type="date" dateStyle="long"/></p>
      <p>个人主页：${user.url}</p>
      <p>角色：${userRole}</p>
      <c:forEach var="usermeta" items="${user.usermetaList}">
        <p>${usermeta.key}${usermeta.value}</p>
      </c:forEach>
  </div>
  <div class="infoBlock">
      <h4>用户近期文章</h4>
      <c:choose>
        <c:when test="${!empty postList}">
            <c:forEach items="${postList}" var="post">
              <p><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></p>
            </c:forEach>
          <p  class="more"><a href="<c:url value="/user/${user.id}/posts"/>" title="查看${user.displayName}的全部文章">查看全部</a></p>
        </c:when>
        <c:otherwise>
          <p>用户还未发表任何文章。</p>
        </c:otherwise>
      </c:choose>
  </div>
  <div class="infoBlock">
      <h4>用户近期评论</h4>
        <c:choose>
          <c:when test="${!empty commentList}">
              <c:forEach items="${commentList}" var="comment">
                <p><a href="<c:url value="/post/${comment.post.slug}"/>">${comment.post.title}</a></p>
              </c:forEach>
            <p><a href="<c:url value="comments"/>" title="查看${user.displayName}的全部评论">查看全部</a></p>
          </c:when>
          <c:otherwise>
            <p>用户还未发表任何评论</p>
          </c:otherwise>
        </c:choose>
  </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
