<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Services - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-page">

<%@ include file="topbar.jspf" %>

<div class="container">

    <div class="page-head">
        <h2>Manage Services</h2>
        <a class="btn btn-primary" href="${ctx}/service?action=new">+ Add New Service</a>
    </div>

    <%-- Messages sent by ServiceServlet --%>
    <c:choose>
        <c:when test="${param.msg == 'added'}">
            <div class="alert alert-success">Service added successfully.</div>
        </c:when>
        <c:when test="${param.msg == 'updated'}">
            <div class="alert alert-success">Service updated successfully.</div>
        </c:when>
        <c:when test="${param.msg == 'deleted'}">
            <div class="alert alert-success">Service deleted successfully.</div>
        </c:when>
        <c:when test="${param.msg == 'inuse'}">
            <div class="alert alert-error">This service already has bookings, so it cannot be deleted.</div>
        </c:when>
        <c:when test="${param.msg == 'notfound'}">
            <div class="alert alert-error">Service not found.</div>
        </c:when>
        <c:when test="${param.msg == 'invalid'}">
            <div class="alert alert-error">Invalid request.</div>
        </c:when>
    </c:choose>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Image</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${services}">
                    <tr>
                        <td>${s.id}</td>
                        <td><c:out value="${s.name}"/></td>
                        <td><c:out value="${s.category}"/></td>
                        <td><c:out value="${s.description}"/></td>
                        <td>&#8377; <fmt:formatNumber value="${s.price}" pattern="#,##0.00"/></td>
                        <td><c:out value="${s.image}"/></td>
                        <td>
                            <a class="btn btn-edit" href="${ctx}/service?action=edit&id=${s.id}">Edit</a>

                            <form class="inline-form" action="${ctx}/service" method="post"
                                  onsubmit="return confirm('Delete this service?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${s.id}">
                                <button type="submit" class="btn btn-delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty services}">
                    <tr><td colspan="7" class="empty">No services found. Click "Add New Service" to create one.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
