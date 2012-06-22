<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%--@ page session="false" --%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>昆明文化辞典</title>
    <style type="text/css">
			*{font-size:12px;margin:0;padding:0;}
			.groupPanel{width:340px; height:255px; overflow:hidden; border:1px #cccbc9 solid; line-height:20px;margin:2em;padding:2em;}
			ul{list-style: none;}
			a{text-decoration: none;}
			a:hover{text-decoration: underline;}
			.TabADS{width:340px;}
			.TabADS ul{width:340px; height:24px;background:#fff;}
      .TabADS li{width:84px; float:left; height:18px; padding:6px 0 0 0; background:url(<c:url value="/resources/images/sinahome_ws_013.gif"/>) no-repeat right #e4e4e4; text-align:center; color:#333; cursor:pointer;}
      .TabADS .TasADSOn{background:url(<c:url value="/resources/images/sinahome_ws_012.gif"/>) no-repeat right #ffe4a6; text-align:center; color:#333; font-weight:bold; cursor:pointer;}
			.TabADSCon{background:#FFD77B;padding:5px;width:330px;}
			.TabADSCon li{text-align:left; line-height:20px;}
			.dreamdu{margin-bottom:2em;}
		</style>
		<script language="javascript" type="text/javascript">
			function Show_TabADSMenu(tabadid_num,tabadnum)
			{
				for(var i=1;i<5;i++){document.getElementById("tabadcontent_"+tabadid_num+i).style.display="none";}
				for(var i=1;i<5;i++){document.getElementById("tabadmenu_"+tabadid_num+i).className="";}
				document.getElementById("tabadmenu_"+tabadid_num+tabadnum).className="TasADSOn";
				document.getElementById("tabadcontent_"+tabadid_num+tabadnum).style.display="block";
			}
		</script>
  </head>
  <body>
      <%@include file="jspf/header.jspf" %>
      <%@include file="jspf/sidebar.jspf" %>
    <!--Start MainContent-->
    <c:forEach items="${categoryGroups}" end="4" var="group" varStatus="groupStatus">
      <div class="groupPanel">
        <div class="TabADS">
          <ul>
            <c:forEach items="${group.categoryList}" var="category" varStatus="categoryStatus">
              <li id="tabadmenu_${groupStatus.count}${categoryStatus.count}" onmouseover="setTimeout('Show_TabADSMenu(${groupStatus.count},${categoryStatus.count})',200);" <c:if test="${categoryStatus.first}">class="TasADSOn"</c:if>>${category.name}</li>
            </c:forEach>
          </ul>
        </div>
        <div class="TabADSCon">
          <c:forEach items="${group.categoryList}" var="category" varStatus="status">
            <ul id="tabadcontent_${groupStatus.count}${status.count}">
              <c:forEach items="${requestScope[category.name]}" var="post">
                <li><a href="<c:url value="/post/${post.slug}"/>">${post.title}</a></li>
              </c:forEach>
            </ul>
          </c:forEach>
        </div>
      </div>
    </c:forEach>
    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
  </body>
</html>
