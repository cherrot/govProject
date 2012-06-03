<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Home</title>
    </head>
    <body>
        <h1>User test</h1>
        ${errMsg}
        <br/>

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
</html>
