<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <title>testpage</title>
    </head>
    <body>
        <%@include file="header.jspf" %>
        <div id="sidebar" >
             <%@include file="sidebar.jspf" %>
        </div>  
        <div id="mainContent" >
             <%@include file="mainContent_list.jspf" %> 
        </div>
        <ul>
            <c:forEach items="${categories}" var="category">
                <li><a href="${category.slug}">${category.name}</a></li>
            </c:forEach>
        </ul>

        <c:forEach items="${linkCategories}" var="category">
            <ul>
                <c:forEach items="${category.linkList}" var="link">
                    <li><a href="${link.url}" target="${link.target}" title="${link.description}" >${link.name}</a></li>
                </c:forEach>
            </ul>
        </c:forEach>
    </body>
    <footer>
        <div id="footer">
        <%@include file="footer.jspf" %>
        </div>
    </footer>
</html>
