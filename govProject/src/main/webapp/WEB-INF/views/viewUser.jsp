<%--
    Document   : viewUser
    Created on : 2012-6-11, 15:50:35
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <div>
      <p><em>昵称：</em>${user.displayName}</p>
      <p><em>登录邮箱：</em>${user.login}</p>
      <p><em>注册时间：</em>${user.registerDate}</p>
      <p><em>个人主页：</em>${user.url}</p>
      <p><em>角色：</em>${userRole}</p>
    </div>

    <div>
      <h3>用户近期文章</h3>
      <ul>
        <c:choose>
          <c:when test="${!empty userPosts}">
            <c:forEach items="${userPosts}" var="post">
              <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
            </c:forEach>
          </c:when>
          <c:otherwise>
            用户还未发表任何文章。
          </c:otherwise>
        </c:choose>
      </ul>
    </div>
    <div>
      <h3>用户近期评论</h3>
        <c:choose>
          <c:when test="${!empty userComments}">
            <ul>
              <c:forEach items="${userComments}" var="comment">
                <li><a href="<c:url value="/post/${comment.post.slug}"/>">${comment.post.title}</a></li>
              </c:forEach>
            </ul>
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