<%--
    Document   : editPost 需要注入 Post对象post、文章关键字对象tags、文章分类List<Category>对象categories,还有对已选文章分类的boolean值
    Created on : 2012-6-6, 20:24:18
    Author     : sai
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<c:url value="/resources/css/fileuploader.css"/>" rel="stylesheet" type="text/css">
    <title>${post.title} | 昆明文化辞典</title>

<!--    <script type="text/javascript">
      function init() {
        document.getElementById('file_upload_form').onsubmit=function() {
          document.getElementById('file_upload_form').target = 'upload_target'; //'upload_target' is the name of the iframe
          document.getElementById("upload_target").onload=function() {
            document.getElementById("upload_result").write("文件上传成功！");
          }
        }
      }
      window.onload=init;
    </script>-->
<!--    <script type="text/javascript">
      function callback(msg) {
        document.getElementById("file").outerHTML = document.getElementById("file").outerHTML;
        document.getElementById("msg").innerHTML = "<em>"+msg+"</em>";
      }
    </script>-->

    <script src="<c:url value="/resources/js/fileuploader.js"/>" type="text/javascript"></script>
    <script>
      <%--TODO: 修改回调函数，上传成功后不光返回上传状态，还要在文章中嵌入代码--%>
        function createUploader(){
            var uploader = new qq.FileUploader({
                element: document.getElementById('file-uploader'),
                action: '<c:url value="/post/upload"/>',
                params: {postId:${post.id},f:2},
                encoding: "multipart",
                debug: false
            });
        }
        window.onload = createUploader;
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

    <div id="file-uploader">
      <noscript>
        <p>为了上传更快，请您启用JavaScript。</p>
        <form id="file_upload_form" method="post" action="<c:url value="/post/upload"/>" enctype="multipart/form-data">
          <input type="file" name="qqfile" />
          <%--FIXME: 如果是新建文章，上传文件会出错：没有postId--%>
          <input type="hidden" name="postId" value="${post.id}"/>
          <input type="submit" value="上传"/>
          <iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;"></iframe>
        </form>
      </noscript>
    </div>

    <form:form modelAttribute="post" enctype="multipart/form-data" >
      <form:errors path="*" />
      <form:errors path="title"/>
      <form:input path="title" placeholder="请输入文章标题" /><br/>
      <%--TODO: 短链接可使用javascript自动生成，并使用AJAX验证是否可用--%>
      <form:errors path="slug"/>
      <form:input path="slug" placeholder="请输入文章短链接（可选）" /><br/>
      <%--不能直接用tagList，必须转成String--%>
      <input type="text" value="${tagListString}" placeholder="请输入文章关键字，以英文逗号隔开"/>
      <!--文章分类-->
      <ul>
        <c:forEach items="${categories}" var="category">
          <li><input type="checkbox" value="${category.name}" <c:if test="${requestScope[category.name]}">checked="checked"</c:if>/></li>
        </c:forEach>
      </ul>
      <form:errors path="content"/>
      <form:textarea path="content"/><br/>
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
