<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%--@ page session="false" --%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>昆明文化辞典</title>
    <script src="<c:url value="resources/js/jquery/galleria/galleria-1.2.8.min.js" />"></script>
    <script>
      $(function(){
         /*Galleria && Galleria.loadTheme('<c:url value="resources/js/jquery/galleria/themes/dots/galleria.dots.js" />');
         $('#galleria').galleria({
           autoplay: 3000,
           popupLinks: true
         });*/
        Galleria && Galleria.loadTheme('<c:url value="resources/js/jquery/galleria/themes/classic/galleria.classic.min.js" />');
        Galleria.run('#galleria', {
          autoplay: 3000,
          popupLinks: true
        });
        $("#tab1,#tab2,#tab3,#tab4,#tab5").tabs({
          fx: { opacity: 'toggle', duration: 'fast'},
          event:'mousemove'
        });
      });
    </script>
  </head>
  <body>
    <%@include file="jspf/header.jspf" %>
    <%@include file="jspf/sidebar.jspf" %>

    <!--Start MainContent-->
      <div class="fl main-block" id="galleria">
        <c:forEach items="${imagePosts}" var="imagePost">
          <img src="<c:url value="${imagePost.excerpt}"/>" longdesc="<c:url value="/post/${imagePost.postParent.slug}"/>"/>
        </c:forEach>
          <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song1.jpg" longdesc="http://www.cherrot.com" />
          <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song1.jpg" longdesc="http://www.cherrot.com" />
      </div>

    <c:forEach items="${categoryGroups}" end="4" var="group" varStatus="groupStatus">
      <div id="tab${groupStatus.count}" class="fl main-block">
        <ul>
            <c:forEach items="${group.categoryList}" var="category" varStatus="status">
              <li><a href="#tab${groupStatus.count}-${status.count}">${category.name}</a></li>
            </c:forEach>
        </ul>
        <c:forEach items="${group.categoryList}" var="category" varStatus="status">
          <div id="tab${groupStatus.count}-${status.count}">
            <ul id="list${status.count}-${groupStatus.count}">
              <c:forEach items="${requestScope[category.name]}" var="post">
                <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
              </c:forEach>
            </ul>
          </div>
          </c:forEach>
      </div>
    </c:forEach>

    <!--End MainContent-->
    <%@include file="jspf/footer.jspf" %>
  </body>
</html>
