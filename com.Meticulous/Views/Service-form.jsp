<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isEdit" value="${service.id > 0}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${isEdit ? 'Edit' : 'Add'} Service - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-page">

<%@ include file="topbar.jspf" %>

<div class="container">

    <div class="page-head">
        <h2>${isEdit ? 'Edit Service' : 'Add New Service'}</h2>
    </div>

    <div class="form-box">

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form action="${ctx}/service" method="post" onsubmit="return checkForm()">

            <input type="hidden" name="action" value="${isEdit ? 'update' : 'insert'}">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${service.id}">
            </c:if>

            <label for="name">Service Name</label>
            <input type="text" id="name" name="name" maxlength="100"
                   value="<c:out value='${service.name}'/>" required>

            <label for="category">Category</label>
            <select id="category" name="category" required>
                <option value="">-- Select category --</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat}" ${service.category == cat ? 'selected' : ''}>${cat}</option>
                </c:forEach>
            </select>

            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4"><c:out value="${service.description}"/></textarea>

            <label for="price">Price (&#8377;)</label>
            <input type="number" id="price" name="price" min="0" step="0.01"
                   value="${service.price}" required>

            <label for="image">Image file name (optional)</label>
            <input type="text" id="image" name="image" maxlength="100"
                   placeholder="e.g. clean.jpg"
                   value="<c:out value='${service.image}'/>">

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">${isEdit ? 'Update Service' : 'Save Service'}</button>
                <a class="btn btn-grey" href="${ctx}/service?action=list">Cancel</a>
            </div>
        </form>
    </div>

</div>

<script>
    // Client-side check (ServiceServlet validates again on the server)
    function checkForm() {
        var name = document.getElementById("name").value.trim();
        var price = parseFloat(document.getElementById("price").value);

        if (name === "") {
            alert("Service name is required.");
            return false;
        }
        if (isNaN(price) || price < 0) {
            alert("Please enter a valid price.");
            return false;
        }
        return true;
    }
</script>

</body>
</html>
