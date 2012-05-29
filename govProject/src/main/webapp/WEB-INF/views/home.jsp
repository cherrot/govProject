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
        <form name="loginForm" action="user/register" method="POST">
            <input type="email" name="login" value="" />
            <input type="password" name="pass" value="" />
            <input type="submit" value="register" name="register" />
        </form>
        <form name="loginForm" action="user/login" method="POST">
            <input type="email" name="login" value="" />
            <input type="password" name="pass" value="" />
            <input type="submit" value="login" name="login" />
        </form>
        <br/>
        <a href="person/list">Go to the person list</a>
    </body>
</html>
