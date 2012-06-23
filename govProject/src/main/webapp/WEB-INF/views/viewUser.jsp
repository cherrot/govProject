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
    <h1>${user.displayName} 的个人资料</h1>
    <c:if test="${user eq sessionUser}">
      <a href="<c:url value="/user/edit"/>">编辑个人资料</a>
      <a href="<c:url value="/post/create"/>">新建文章</a>
    </c:if>
    <div>
      <p><em>昵称：</em>${user.displayName}</p>
      <c:if test="${user eq sessionUser}">
        <p><em>登录邮箱：</em>${user.login}</p>
      </c:if>
      <p><em>注册时间：</em><fmt:formatDate value="${user.registerDate}" type="date" dateStyle="long"/></p>
      <p><em>个人主页：</em>${user.url}</p>
      <p><em>角色：</em>${userRole}</p>
      <c:forEach var="usermeta" items="${user.usermetaList}">
        <p><em>${usermeta.key}</em>${usermeta.value}</p>
      </c:forEach>
    </div>

    <div>
      <h3>用户近期文章</h3>
      <c:choose>
        <c:when test="${!empty postList}">
          <ul>
            <c:forEach items="${postList}" var="post">
              <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
            </c:forEach>
          </ul>
          <a href="<c:url value="/user/${user.id}/posts"/>" title="查看${user.displayName}的全部文章">查看全部</a>
        </c:when>
        <c:otherwise>
          用户还未发表任何文章。
        </c:otherwise>
      </c:choose>
    </div>
    <div>
      <h3>用户近期评论</h3>
        <c:choose>
          <c:when test="${!empty commentList}">
            <ul>
              <c:forEach items="${commentList}" var="comment">
                <li><a href="<c:url value="/post/${comment.post.slug}"/>">${comment.post.title}</a></li>
              </c:forEach>
            </ul>
            <a href="<c:url value="comments"/>" title="查看${user.displayName}的全部评论">查看全部</a>
          </c:when>
          <c:otherwise>
            用户还未发表任何评论
          </c:otherwise>
        </c:choose>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>