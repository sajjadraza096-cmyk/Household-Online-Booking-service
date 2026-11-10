<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Enquiries - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-page">

<%@ include file="topbar.jspf" %>

<div class="container">

    <div class="page-head">
        <h2>Customer Enquiries</h2>
    </div>

    <%-- Messages sent by EnquiryServlet --%>
    <c:choose>
        <c:when test="${param.msg == 'deleted'}">
            <div class="alert alert-success">Enquiry deleted successfully.</div>
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
                    <th>Received</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Mobile</th>
                    <th>Service Needed</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="e" items="${enquiries}">
                    <tr>
                        <td>${e.id}</td>
                        <td><fmt:formatDate value="${e.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                        <td><c:out value="${e.name}"/></td>
                        <td><a href="mailto:<c:out value='${e.email}'/>"><c:out value="${e.email}"/></a></td>
                        <td><c:out value="${e.mobile}"/></td>
                        <td><c:out value="${e.serviceNeeded}"/></td>
                        <td>
                            <form class="inline-form" action="${ctx}/enquiry" method="post"
                                  onsubmit="return confirm('Delete this enquiry?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${e.id}">
                                <button type="submit" class="btn btn-delete">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty enquiries}">
                    <tr><td colspan="7" class="empty">No enquiries received yet.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>

</body>
</html>
