<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <% String listName="测试边栏";%>
        <% String listLink1="http://www.w3school.com.cn";%>
        <% String listLink1sString="链接一";%>
        <% String listLink2sString="链接二";%>
        <% String listLink3sString="链接三";%>
        <% String listLink4sString="链接四";%>
        <% String listLink5sString="链接五";%>
        <% String listLink6sString="链接六";%>
        <!--可以添加方法改变链接名字-->
    </head>
    <body>
        <div id="sidebarlist1">
        <p><%=listName%></p>
        <ul type="circle">
          <li><a href=""><%=listLink1sString%></a></li>
          <li><a href=""><%=listLink2sString%></a></li>
          <li><a href=""><%=listLink3sString%></a></li>
          <li><a href=""><%=listLink4sString%></a></li>
          <li><a href=""><%=listLink5sString%></a></li>
          <li><a href=""><%=listLink6sString%></a></li>
        </ul>
        </div>
        <div id="sidebarlist2">
        <p><%=listName%></p>
        <ul type="circle">
          <li><a href=""><%=listLink1sString%></a></li>
          <li><a href=""><%=listLink2sString%></a></li>
          <li><a href=""><%=listLink3sString%></a></li>
          <li><a href=""><%=listLink4sString%></a></li>
          <li><a href=""><%=listLink5sString%></a></li>
          <li><a href=""><%=listLink6sString%></a></li>
        </ul>
        </div>
    </body>
</html>
