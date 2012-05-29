<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Home</title>
    </head>
    <body>
        <h1>
            User test
        </h1>
        ${controllerMessage}<br/>
        <br/>
        <form name="loginForm" action="/user/login" method="POST">
            <input type="email" name="login" value="" />
            <input type="password" name="pass" value="" />
        </form>
        <br/>
        <a href="person/list">Go to the person list</a>
    </body>
</html>
