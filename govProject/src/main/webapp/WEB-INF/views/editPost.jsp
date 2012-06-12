<%--
    Document   : editPost
    Created on : 2012-6-6, 20:24:18
    Author     : sai
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>${post.title} | 昆明文化辞典</title>
    <script type="text/javascript">
      function callback(msg) {
        document.getElementById("file").outerHTML = document.getElementById("file").outerHTML;
        document.getElementById("msg").innerHTML = "<em>"+msg+"</em>";
      }
    </script>
  </head>
  <body>
    <div id="header">
      <%@include file="jspf/header.jspf" %>
    </div>
    <div id="sidebar" >
      <%@include file="jspf/sidebar.jspf" %>
    </div>
    <!--Start MainContent-->
    <h3>${successMsg}</h3>
    <form action="<c:url value="/post/upload"/>" enctype="mutipart/form-data" method="post" >
      <input type="file" name="file" />
      <input type="hidden" name="postId" value="${post.id}"/>
      <input type="submit" value="上传"/>
      <iframe id="hidden_frame" ></iframe>
    </form>

      <form method="post" action="<c:url value="/post/upload"/>" enctype="multipart/form-data">
        <input type="file" name="file"/>
        <input type="hidden" name="postId" value="${post.id}"/>
        <input type="submit"/>
      </form>

    <form:form modelAttribute="post">
      <form:errors path="*" />
      <form:errors path="title"/><form:input path="title" placeholder="请输入文章标题" /><br/>
      <%--TODO: 短链接可使用javascript自动生成，并使用AJAX验证是否可用--%>
      <form:errors path="slug"/><form:input path="slug" placeholder="请输入文章短链接（可选）" /><br/>
      <form:errors path="content"/><form:textarea path="content"/><br/>
      <label for="comment_status">允许评论</label>
      <form:checkbox id="comment_status" path="commentStatus" value="true" selected="selected" /><br/>
      <form:password path="password" placeholder="文章访问密码，不设请留空" /><br/>
      <form:select path="status" items="${postStatus}" /><br/>
      <form:hidden path="id" value="${post.id}"/>
      <input type="submit" value="发布" />
    </form:form>
    <!--End MainContent-->
    <div id="footer">
      <%@include file="jspf/footer.jspf" %>
    </div>
  </body>
</html>
