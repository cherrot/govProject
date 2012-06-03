<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <% String mainContentimage="./mainContentimage.jpg";%>
        <% String mainContentsString="这里是介绍性的文字";%>
    </head>
    <body>
        <div id="mainContentimage">
            <img src=<%=mainContentimage%> />
        </div> 
        <div id="mainContentString">
            <%=mainContentsString%>    
        </div>
    </body>
</html>
