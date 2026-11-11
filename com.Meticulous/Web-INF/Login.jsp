<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Meticulous Household Services</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-page">

<div class="auth-box">
    <h2>Login</h2>

    <%-- Success messages sent by RegisterServlet and LogoutServlet --%>
    <c:if test="${param.msg == 'registered'}">
        <div class="alert alert-success">Registration successful! Please login.</div>
    </c:if>
    <c:if test="${param.msg == 'loggedout'}">
        <div class="alert alert-success">You have been logged out.</div>
    </c:if>
    <c:if test="${param.msg == 'loginrequired'}">
        <div class="alert alert-error">Please login to continue.</div>
    </c:if>

    <%-- Error message sent by LoginServlet --%>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" required>

        <button type="submit">Login</button>
    </form>

    <div class="link">
        New user? <a href="${pageContext.request.contextPath}/register">Create an account</a>
    </div>
</div>

</body>
</html>
