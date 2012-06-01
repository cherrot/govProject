<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Home</title>
    </head>
    <body>
        <h1>
            User test
        </h1>
        ${errMsg}<br/>
        <br/>
        <form name="loginForm" action="user/doRegister" method="POST">
            <input type="email" name="login" value="" />
            <input type="password" name="pass" value="" />
            <input type="submit" value="register" name="register" />
        </form>
        <form name="loginForm" action="user/doLogin" method="POST">
            <input type="email" name="login" value="" />
            <input type="password" name="pass" value="" />
            <input type="submit" value="login" name="login" />
        </form>
        ${person.id}<br />${person.login}<br />
        <br/>
        <c:forEach items="${people}" var="person">
            <a href="edit?id=${person.id}">${person.id} -
                ${person.login} ${person.pass}</a>
            <br />
        </c:forEach>
    </body>
</html>
