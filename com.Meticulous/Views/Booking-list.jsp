<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isAdmin" value="${sessionScope.user.role == 'ADMIN'}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${isAdmin ? 'All Bookings' : 'My Bookings'} - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-page">

<%@ include file="topbar.jspf" %>

<div class="container">

    <div class="page-head">
        <h2>${isAdmin ? 'All Bookings' : 'My Bookings'}</h2>
        <a class="btn btn-primary" href="${ctx}/booking?action=new">+ New Booking</a>
    </div>

    <%-- Messages sent by BookingServlet --%>
    <c:choose>
        <c:when test="${param.msg == 'added'}">
            <div class="alert alert-success">Booking created successfully. Status: PENDING.</div>
        </c:when>
        <c:when test="${param.msg == 'updated'}">
            <div class="alert alert-success">Booking updated successfully.</div>
        </c:when>
        <c:when test="${param.msg == 'deleted'}">
            <div class="alert alert-success">Booking deleted successfully.</div>
        </c:when>
        <c:when test="${param.msg == 'statusupdated'}">
            <div class="alert alert-success">Booking status updated.</div>
        </c:when>
        <c:when test="${param.msg == 'notallowed'}">
            <div class="alert alert-error">You are not allowed to change this booking. Only PENDING bookings can be edited or deleted.</div>
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
                    <c:if test="${isAdmin}"><th>Customer</th></c:if>
                    <th>Service</th>
                    <th>Date</th>
                    <th>Address</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="b" items="${bookings}">

                    <%-- Can the current user edit or delete this booking? --%>
                    <c:set var="canModify"
                           value="${isAdmin || (b.userId == sessionScope.user.id && b.status == 'PENDING')}" />

                    <tr>
                        <td>${b.id}</td>
                        <c:if test="${isAdmin}">
                            <td><c:out value="${b.userName}"/></td>
                        </c:if>
                        <td><c:out value="${b.serviceName}"/></td>
                        <td>${b.bookingDate}</td>
                        <td><c:out value="${b.address}"/></td>

                        <td>
                            <c:choose>
                                <c:when test="${isAdmin}">
                                    <%-- Admin: change status with a dropdown --%>
                                    <form action="${ctx}/booking" method="post" style="margin:0;">
                                        <input type="hidden" name="action" value="status">
                                        <input type="hidden" name="id" value="${b.id}">
                                        <select name="status" onchange="this.form.submit()" style="padding:5px;">
                                            <c:forEach var="st" items="${statuses}">
                                                <option value="${st}" ${b.status == st ? 'selected' : ''}>${st}</option>
                                            </c:forEach>
                                        </select>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-${b.status}">${b.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <c:choose>
                                <c:when test="${canModify}">
                                    <a class="btn btn-edit" href="${ctx}/booking?action=edit&id=${b.id}">Edit</a>

                                    <form class="inline-form" action="${ctx}/booking" method="post"
                                          onsubmit="return confirm('Delete this booking?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${b.id}">
                                        <button type="submit" class="btn btn-delete">Delete</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <small>Locked</small>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty bookings}">
                    <tr>
                        <td colspan="${isAdmin ? 7 : 6}" class="empty">
                            No bookings yet. Click "New Booking" to create one.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
