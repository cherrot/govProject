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
        <form:form name="registerForm" modelAttribute="user" action="user/doRegister">
            <input type="email" name="login" />
            <input type="password" name="pass" />
            <input type="submit" value="register" name="register" />
        </form:form>
        <form:form name="loginForm" action="user/doLogin" method="POST">
            <input type="email" name="username" />
            <input type="password" name="password" />
            <input type="submit" name="login" value="login" />
        </form:form>
        ${person.id}<br />${person.login}<br />
        <br/>
        <c:forEach items="${people}" var="person">
            <a href="edit?id=${person.id}">${person.id} -
                ${person.login} ${person.pass}</a>
            <br />
        </c:forEach>
    </body>
</html>
