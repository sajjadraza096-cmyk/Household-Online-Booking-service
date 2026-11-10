<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="code" value="${requestScope['jakarta.servlet.error.status_code']}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/auth.css">
</head>
<body class="auth-page">

<div class="auth-box" style="text-align:center;">

    <h2 style="font-size:48px; margin-bottom:5px;">
        <c:out value="${empty code ? 'Oops' : code}"/>
    </h2>

    <c:choose>
        <c:when test="${code == 404}">
            <p>Sorry, the page you are looking for does not exist.</p>
        </c:when>
        <c:when test="${code == 403}">
            <p>You do not have permission to open this page.</p>
        </c:when>
        <c:otherwise>
            <p>Something went wrong on our side. Please try again later.</p>
        </c:otherwise>
    </c:choose>

    <a href="${ctx}/home"
       style="display:inline-block; margin-top:15px; padding:10px 20px; background:#1e88e5; color:#fff; border-radius:5px; text-decoration:none;">
        Back to Home
    </a>
</div>

</body>
</html>
