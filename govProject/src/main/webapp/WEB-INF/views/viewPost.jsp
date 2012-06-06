<%--
    Document   : viewPost
    Created on : 2012-6-6, 14:29:27
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <title>${post1.title} | 昆明文化辞典</title>
  </head>
  <body>
    <div id="header">
      <%@include file="jspf/header.jspf" %>
    </div>
    <div id="sidebar" >
      <%@include file="jspf/sidebar.jspf" %>
    </div>
    <!--Start MainContent-->
    <h1 class="postTitle"><a href="<c:url value="/post/${post.slug}" />" title="${post.title}" >${post.title}</a></h1>
    <h2 class="meta">
      由&nbsp;<a href="<c:url value="/user/${post.user.id}" />">${post.user.displayName}</a>&nbsp;发表&nbsp;
      ${post.createDate}&nbsp;
      所属类别：
      <c:forEach items="${post.itemList}" var="category">
        <a href="<c:url value="/term/${category.slug}"/>" title="点击察看 ${category.name} 分类的所有文章">${category.name}</a>&nbsp;
      </c:forEach>
        <a href="#comments">{post.commentCount} 条评论</a>
    </h2>
    <div class="postContent">
      ${post.content}
    </div>
    <div id="comments" class="postComments">
      <ol>
        <c:forEach items="${post.commentList}" var="comment">
          <c:if test="${comment.approved}">
            <li>${comment.author}:${comment.content} (${comment.commentDate})</li>
          </c:if>
        </c:forEach>
      </ol>
    </div>
    <div id="newComment">
      <h3>留下评论</h3>
      <form:form modelAttribute="newComment">
        <label for="newCommentAuthor">您的姓名</label>
        <form:input id="newCommentAuthor" path="author" placeholder="请输入您的姓名" required="required" /><br/>
        <label for="newCommentEmail">您的邮箱</label>
        <form:input id="newCommentEmail" path="authorEmail"/>
        <form:input id="newCoomentUrl" path="authorUrl"/>
        <form:textarea id="newCommentContent" path="content"/>
      </form:form>
    </div>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>
