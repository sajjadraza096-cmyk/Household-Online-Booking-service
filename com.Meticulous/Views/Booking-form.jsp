<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="isEdit" value="${booking.id > 0}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${isEdit ? 'Edit' : 'New'} Booking - Meticulous</title>
    <link rel="stylesheet" href="${ctx}/css/admin.css">
</head>
<body class="admin-page">

<%@ include file="topbar.jspf" %>

<div class="container">

    <div class="page-head">
        <h2>${isEdit ? 'Edit Booking' : 'New Booking'}</h2>
    </div>

    <div class="form-box">

        <c:if test="${not empty error}">
            <div class="alert alert-error"><c:out value="${error}"/></div>
        </c:if>

        <form action="${ctx}/booking" method="post" onsubmit="return checkForm()">

            <input type="hidden" name="action" value="${isEdit ? 'update' : 'insert'}">
            <c:if test="${isEdit}">
                <input type="hidden" name="id" value="${booking.id}">
            </c:if>

            <label for="serviceId">Service</label>
            <select id="serviceId" name="serviceId" required>
                <option value="">-- Select a service --</option>
                <c:forEach var="s" items="${services}">
                    <option value="${s.id}" ${booking.serviceId == s.id ? 'selected' : ''}>
                        <c:out value="${s.name}"/> (<c:out value="${s.category}"/>) - &#8377;<fmt:formatNumber value="${s.price}" pattern="#,##0.00"/>
                    </option>
                </c:forEach>
            </select>

            <label for="bookingDate">Booking Date</label>
            <input type="date" id="bookingDate" name="bookingDate"
                   min="${minDate}" value="${booking.bookingDate}" required>

            <label for="address">Address</label>
            <textarea id="address" name="address" rows="3" maxlength="255"
                      placeholder="House no., street, area, city" required><c:out value="${booking.address}"/></textarea>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary">${isEdit ? 'Update Booking' : 'Confirm Booking'}</button>
                <a class="btn btn-grey" href="${ctx}/booking?action=list">Cancel</a>
            </div>
        </form>
    </div>

</div>

<script>
    // Client-side check (BookingServlet validates again on the server)
    function checkForm() {
        var service = document.getElementById("serviceId").value;
        var date = document.getElementById("bookingDate").value;
        var address = document.getElementById("address").value.trim();

        if (service === "") {
            alert("Please select a service.");
            return false;
        }
        if (date === "") {
            alert("Please choose a booking date.");
            return false;
        }
        if (address === "") {
            alert("Address is required.");
            return false;
        }
        return true;
    }
</script>

</body>
</html>
