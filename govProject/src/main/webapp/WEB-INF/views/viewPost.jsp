<%--
    Document   : viewPost
    Created on : 2012-6-6, 14:29:27
    Author     : Cherrot Luo<cherrot+dev@cherrot.com>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--只有scope为session时才会覆盖用户浏览器设置--%>
<fmt:setLocale value="zh_CN" scope="session"/>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>${post.title} | 昆明文化辞典</title>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
    <h1 class="postTitle"><a href="<c:url value="/post/${post.slug}" />" title="${post.title}" >${post.title}</a></h1>
    <h2 class="meta">
      由&nbsp;<a href="<c:url value="/user/${post.user.id}" />">${post.user.displayName}</a>&nbsp;发表&nbsp;
      <fmt:formatDate value="${post.createDate}" type="date" dateStyle="full"/> &nbsp;
      所属分类：
      <c:forEach items="${post.categoryList}" var="category">
        <a href="<c:url value="/category/${category.slug}"/>" title="点击察看 ${category.name} 分类的所有文章">${category.name}</a>&nbsp;
      </c:forEach>
        <a href="#comments">${post.commentCount} 条评论</a>
    </h2>

    <div class="postContent">
      ${post.content}
    </div>
    <p>
      <c:if test="${!empty tagList}">文章标签：</c:if>
      <%--c:set value="${fn:length(tagList)}" var="tagListSize"/--%>
      <c:forEach items="${tagList}" var="tag" varStatus="status">
        <a href="<c:url value="/tag/${tag.slug}"/>">${tag.name}</a><c:if test="${!status.last}">,&nbsp;</c:if>
      </c:forEach>
    </p>

    <c:if test="${sessionScope[loginUser] eq post.user}">
      <a href="<c:url value="/post/${post.slug}/edit"/>">编辑文章</a>
    </c:if>

    <div id="comments" class="postComments">
      <ol>
        <c:forEach items="${post.commentList}" var="comment">
          <c:if test="${comment.approved}">
            <li>${comment.author}:${comment.content} (${comment.commentDate})</li>
          </c:if>
        </c:forEach>
      </ol>
      <ul>
        <c:forEach items="${pendingComments}" var="pendingComment">
          您的评论正等待审核:<li><a href="${pendingComment.authorUrl}">${pendingComment.author}</a>: ${pendingComment.content}</li>
        </c:forEach>
      </ul>
    </div>

    <div id="newComment">
      <h3>留下评论</h3>
      <form:form modelAttribute="newComment">
        <label for="new_comment_author">您的姓名</label>
        <form:input id="new_comment_author" path="author" placeholder="请输入您的姓名" required="required" />
        <form:errors path="author" /><br/>
        <label for="new_comment_email">您的邮箱</label>
        <form:input id="new_comment_email" path="authorEmail" placeholder="请输入您的Email（如QQ邮箱）" required="required"/>
        <form:errors path="authorEmail" /><br/>
        <label for="new_comment_url">您的个人主页</label>
        <form:input id="new_comment_url" path="authorUrl" placeholder="您的人人主页、QQ空间、百度空间等（可不填）"/>
        <form:errors path="authorUrl" /><br/>
        <label for="new_comment_content">您的评论</label>
        <form:textarea id="new_comment_content" path="content" placeholder="写点什么吧：" required="required"/>
        <form:errors path="content" /><br/>
        <input type="hidden" name="postId" value="${post.id}"/>
        <input type="hidden" name="commentParentId"/>
        <input type="hidden" name="authorIp" value="${pageContext.request.remoteAddr}"/>
        <input type="submit" value="提交"/>
      </form:form>
    </div>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
