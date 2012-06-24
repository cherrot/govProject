<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%--@ page session="false" --%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <%@include file="jspf/commonHead.jspf" %>
    <title>昆明文化辞典</title>

    <style type="text/css">
			*{font-size:12px;margin:0;padding:0;}
			.groupPanel{width:340px; height:255px; overflow:hidden; border:1px #cccbc9 solid; line-height:20px;margin:1em;padding:1em;}
			ul{list-style: none;}
			a{text-decoration: none;}
			a:hover{text-decoration: underline;}
			.TabADS{width:340px;}
			.TabADS ul{width:340px; height:24px;background:#fff;}
      .TabADS li{width:84px; float:left; height:18px; padding:6px 0 0 0; background:url(<c:url value="/resources/images/sinahome_ws_013.gif"/>) no-repeat right #e4e4e4; text-align:center; color:#333; cursor:pointer;}
      .TabADS .TasADSOn{background:url(<c:url value="/resources/images/sinahome_ws_012.gif"/>) no-repeat right #ffe4a6; text-align:center; color:#333; font-weight:bold; cursor:pointer;}
			.TabADSCon{background:#FFD77B;padding:5px;width:330px;}
			.TabADSCon li{text-align:left; line-height:20px;}
			.dreamdu{margin-bottom:1em;}

      .container, .container img{width:340px; height:255px;}
      .container img{border:0;vertical-align:top;}
      .container ul, .container li{list-style:none;margin:0;padding:0;}

      .num{ position:absolute; right:5px; bottom:5px; font:12px/1.5 tahoma, arial; height:18px;}
      .num li{
        float: left;
        color: #d94b01;
        text-align: center;
        line-height: 16px;
        width: 16px;
        height: 16px;
        font-family: Arial;
        font-size: 11px;
        cursor: pointer;
        margin-left: 3px;
        border: 1px solid #f47500;
        background-color: #fcf2cf;
      }
      .num li.on{
        line-height: 18px;
        width: 18px;
        height: 18px;
        font-size: 14px;
        margin-top:-2px;
        background-color: #ff9415;
        font-weight: bold;
        color:#FFF;
                        
                      
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
                <script src="../../resources/js/SlideTrans.js"></script>
  </head>
  <body>
      <table align="center">
          <tr><td>
      <div width="1000">
      <%@include file="jspf/header.jspf" %>
      </div>
      </td></tr>
      <tr style="text-align:center;">
          <td style="text-align:center;">
      
    <!--Start MainContent-->
    <div style="text-align:left;">
    <table>
        <tr>
            <td width="350px">
      <div class="container" id="idContainer2" style="margin-left:auto; margin-right:auto;">
      <ul id="idSlider2">
        <li><a href="http://www.cnblogs.com/cloudgamer/archive/2009/12/22/ImagePreview.html"> <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song1.jpg" alt="图片上传预览" /> </a></li>
        <li><a href="http://www.cnblogs.com/cloudgamer/archive/2009/08/10/FixedMenu.html"> <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song2.jpg" alt="多级联动菜单" /> </a></li>
        <li><a href="http://www.cnblogs.com/cloudgamer/archive/2009/07/07/FixedTips.html"> <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song3.jpg" alt="浮动定位提示" /> </a></li>
        <li><a href="http://www.cnblogs.com/cloudgamer/archive/2010/02/01/LazyLoad.html"> <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song4.jpg" alt="数据延迟加载" /> </a></li>
        <li><a href="http://www.cnblogs.com/cloudgamer/archive/2009/12/01/Quick_Upload.html"> <img src="http://images.cnblogs.com/cnblogs_com/cloudgamer/143727/r_song5.jpg" alt="简便文件上传" /> </a></li>
      </ul>
      <ul class="num" id="idNum">
      </ul>
    </div>
    <script title="imageSlide">
      var nums = [], timer, n = $$("idSlider2").getElementsByTagName("li").length,
      st = new SlideTrans("idContainer2", "idSlider2", n, {
        onStart: function(){//设置按钮样式
          forEach(nums, function(o, i){ o.className = st.Index == i ? "on" : ""; })
        }
      });
      for(var i = 1; i <= n; AddNum(i++)){};
      function AddNum(i){
        var num = $$("idNum").appendChild(document.createElement("li"));
        num.innerHTML = i--;
        num.onmouseover = function(){
          timer = setTimeout(function(){ num.className = "on"; st.Auto = false; st.Run(i); }, 200);
        }
        num.onmouseout = function(){ clearTimeout(timer); num.className = ""; st.Auto = true; st.Run(); }
        nums[i] = num;
      }
      st.Run();
    </script>
    
            </td>
            <td width="340px" valign="top">
    <c:forEach items="${categoryGroups}" begin="0" end="0" var="group" varStatus="groupStatus">    
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
    </td>
        <td width="200px" align="left" valign="top" rowspan="3"><%@include file="jspf/sidebar.jspf" %></td>
        </tr>
    <tr>
                <td width="360px" valign="top">
    <c:forEach items="${categoryGroups}" begin="1" end="1" var="group" varStatus="groupStatus">    
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
    </td>
                <td width="340px" valign="top">
    <c:forEach items="${categoryGroups}" begin="2" end="2" var="group" varStatus="groupStatus">    
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
    </td>
    <td></td>
    </tr>
    <tr>
                <td width="360px" valign="top">
    <c:forEach items="${categoryGroups}" begin="3" end="3" var="group" varStatus="groupStatus">    
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
    </td>
                <td width="340px" valign="top">
    <c:forEach items="${categoryGroups}" begin="4" end="4" var="group" varStatus="groupStatus">    
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
    </td>
    <td></td>
    </tr>
    </table>
        

    <!--End MainContent-->
      <%@include file="jspf/footer.jspf" %>
      
      </div>
      </td></tr></table>
  </body>
</html>
