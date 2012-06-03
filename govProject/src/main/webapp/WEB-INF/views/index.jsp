<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>testpage</title>
        <%@include file="header.jsp" %>
    </head>
    <body>
        <div id="mainContent" >
             <%@include file="mainContent_list.jsp" %> 
        </div>
        <div id="sidebar" >
             <%@include file="sidebar.jsp" %>
        </div>  
    </body>
    <footer>
        <div id="footer">
        <%@include file="footer.jsp" %>
        </div>
    </footer>
</html>
