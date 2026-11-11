<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register - Meticulous Household Services</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-page">

<div class="auth-box">
    <h2>Create Account</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" onsubmit="return validateForm()">

        <label for="name">Full Name</label>
        <input type="text" id="name" name="name" value="<c:out value='${param.name}'/>" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<c:out value='${param.email}'/>" required>

        <label for="mobile">Mobile Number</label>
        <input type="text" id="mobile" name="mobile" maxlength="10"
               value="<c:out value='${param.mobile}'/>" required>

        <label for="password">Password (min 6 characters)</label>
        <input type="password" id="password" name="password" required>

        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>

        <button type="submit">Register</button>
    </form>

    <div class="link">
        Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
    </div>
</div>

<script>
    // Client-side check (the servlet validates again on the server)
    function validateForm() {
        var mobile = document.getElementById("mobile").value;
        var pass = document.getElementById("password").value;
        var confirm = document.getElementById("confirmPassword").value;

        if (!/^\d{10}$/.test(mobile)) {
            alert("Mobile number must be exactly 10 digits.");
            return false;
        }
        if (pass.length < 6) {
            alert("Password must be at least 6 characters.");
            return false;
        }
        if (pass !== confirm) {
            alert("Passwords do not match.");
            return false;
        }
        return true;
    }
</script>

</body>
</html>
