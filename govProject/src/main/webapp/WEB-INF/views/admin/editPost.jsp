<%--
    Document   : editPost 需要注入 Post对象post、文章关键字对象tags、文章分类List<Category>对象categories,还有对已选文章分类的boolean值
    Created on : 2012-6-6, 20:24:18
    Author     : sai
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <link href="<c:url value="/resources/js/fileuploader/fileuploader.css"/>" rel="stylesheet" type="text/css">
    <title>${post.title} | 昆明文化辞典</title>

    <%--    <script type="text/javascript">
          function init() {
            document.getElementById('file_upload_form').onsubmit=function() {
              document.getElementById('file_upload_form').target = 'upload_target'; //'upload_target' is the name of the iframe
              document.getElementById("upload_target").onload=function() {
                document.getElementById("upload_result").write("文件上传成功！");
              }
            }
          }
          window.onload=init;
        </script>--%>
    <%--    <script type="text/javascript">
          function callback(msg) {
            document.getElementById("file").outerHTML = document.getElementById("file").outerHTML;
            document.getElementById("msg").innerHTML = "<em>"+msg+"</em>";
          }
        </script>--%>

    <%--https://github.com/valums/file-uploader--%>
    <script src="<c:url value="/resources/js/fileuploader/fileuploader.js"/>" type="text/javascript"></script>

    <%--http://ueditor.baidu.com/--%>
    <script type="text/javascript" src="<c:url value="/resources/js/ueditor/editor_config.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/resources/js/ueditor/editor_all.js"/>"></script>
    <link rel="stylesheet" href="<c:url value="/resources/js/ueditor/themes/default/ueditor.css"/>">

    <script>
      <%--TODO: 修改回调函数，上传成功后不光返回上传状态，还要在文章中嵌入代码--%>
        function createUploader(){
          var uploader = new qq.FileUploader({
            element: document.getElementById('file-uploader'),
            action: '<c:url value="/post/uploadvideo"/>',
            params: {postId:${post.id},f:2},
            encoding: "multipart",
            debug: false
          });
      <%--Ueditor初始化--%>
          var editor = new baidu.editor.ui.Editor();
          editor.render("content");
        }
        window.onload = createUploader;
    </script>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/functionBar.jspf" %>
    <!--Start MainContent-->
    <h3>${successMsg}</h3>

    <div id="file-uploader">
      <noscript>
      <p>为了上传更快，请您启用JavaScript。</p>
      <form id="file_upload_form" method="post" action="<c:url value="/post/upload"/>" enctype="multipart/form-data">
        <input type="file" name="qqfile" />
        <%--FIXME: 如果是新建文章，上传文件会出错：没有postId。考虑等主Post更新后再更新附件所属的post。比如通过文件路径找到该文件对应的Post--%>
        <input type="hidden" name="postId" value="${post.id}"/>
        <input type="submit" value="上传"/>
        <iframe id="upload_target" name="upload_target" src="" style="width:0;height:0;border:0px solid #fff;"></iframe>
      </form>
      </noscript>
    </div>

    <a href="<c:url value="/post/${post.slug}/delete"/>">删除文章</a>
    <form:form modelAttribute="post" enctype="multipart/form-data" >
      <form:errors path="*" />
      <form:errors path="title"/>
      <label for="postTitle">标题</label><form:input id="postTitle" path="title" placeholder="请输入文章标题" /><br/>
      <%--TODO: 短链接可使用javascript自动生成，并使用AJAX验证是否可用--%>
      <form:errors path="slug"/>
      <form:input path="slug" placeholder="请输入文章短链接（可选）" /><br/>
      <label for="postTags">文章标签</label>
      <input id="postTags" name="postTags" type="text" value="<c:forEach items="${post.tagList}" var="tag" varStatus="status" >${tag.name}<c:if test="${! status.last}">,&nbsp;</c:if></c:forEach>" placeholder="请输入文章关键字，以英文逗号隔开"/>
          <br/><label>文章分类</label>
          <ul>
        <c:forEach items="${postCategories}" var="category">
          <li><input type="checkbox" name="postCategories" value="${category.id}" <c:if test="${requestScope[category.name]}">checked="checked"</c:if>/>${category.name}</li>
            <c:if test="${!empty category.categoryList}">
            <ul>
              <c:forEach items="${category.categoryList}" var="childCategory">
                <li><input type="checkbox" name="postCategories" value="${childCategory.id}" <c:if test="${requestScope[childCategory.name]}">checked="checked"</c:if>/>${childCategory.name}</li>
                </c:forEach>
            </ul>
          </c:if>
        </c:forEach>
      </ul>

      <form:errors path="content"/>
      <%--<form:textarea path="content"/>--%><br/>

      <script type="text/plain" id="content" name="content">
        ${post.content}
      </script>

      <label for="comment_status">允许评论</label><form:checkbox id="comment_status" path="commentStatus" value="true" selected="selected" /><br/>
      <form:password path="password" placeholder="文章访问密码，不设请留空" /><br/>
      <label>发布状态</label><form:select path="status" items="${postStatus}"/><br/>

      <%--更改文章属主。只对管理员有效(/admin/post/{postId}/edit)--%>
      <c:if test="${!empty userList}">
        <select name="postAuthor">
          <c:forEach items="${userList}" var="postAuthor">
            <option value="${postAuthor.id}" <c:if test="${postAuthor eq sessionUser}">selected="selected"</c:if>>${postAuthor.displayName}</option>
          </c:forEach>
        </select>
      </c:if>

      <form:hidden path="id" value="${post.id}"/>
      <input type="submit" value="发布" />
    </form:form>
    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>
