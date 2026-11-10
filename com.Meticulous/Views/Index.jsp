<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en" dir="ltr">
<head>

<script async src="https://www.googletagmanager.com/gtag/js?id=UA-166000335-1"></script>
<script>
      window.dataLayer = window.dataLayer || [];
      function gtag(){dataLayer.push(arguments);}
      gtag('js', new Date());

      gtag('config', 'UA-166000335-1');
    </script>
<meta charset="UTF-8">
<title>METICULOUS HOUSEHOLD CLEANING SERVICE</title>
<link rel="stylesheet" href="${ctx}/css/style.css">
<link rel="stylesheet" href="${ctx}/css/home-extra.css">

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<div class="scroll-button">
<a href="#home"><i class="fas fa-arrow-up"></i></a>
</div>

<nav>
<div class="navbarr">
<div class="logo"><a href="#"><center>METICULOUS HOUSEHOLD CLEANING SERVICE</center> </a></div>
</div>
<ul style="list-style-type:none" class="menu" >

<br>

<div class="navbar">

<li><a href="#home"> Home</a></li>
<li><a href="#about"> Cleaning Services</a></li>
<li><a href="#skills"> Handyman</a></li>
<li><a href="#services"> Garden Maintenance</a></li>
<li><a href="#contact"> Contact</a></li>

<%-- Items that change depending on login status --%>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <li><a href="${ctx}/booking?action=list"> My Bookings</a></li>
                <c:if test="${sessionScope.user.role == 'ADMIN'}">
            <li><a href="${ctx}/service?action=list"> Manage Services</a></li>
            <li><a href="${ctx}/enquiry"> Enquiries</a></li>
        </c:if>
        <li><a href="${ctx}/logout"> Logout (<c:out value="${sessionScope.user.name}"/>)</a></li>
    </c:when>
    <c:otherwise>
        <li><a href="${ctx}/login"> Login</a></li>
        <li><a href="${ctx}/register"> Register</a></li>
    </c:otherwise>
</c:choose>

</ul>

</div>
<div class="menu-btn">
<i class="fas fa-bars"></i>
</div>
</nav>

<section class="background" id="background">
<div class="content">
</div></section>

<section class="home" id="home">
<div class="home-content">
<div class="text">
  <br>
  <br>
<div class="text-one"><br>Hey you,</div><br>
<div class="text-two">Looking for household booking services?</div>
<div class="text-four"><br>Don't worry! Meticulous Household Services have now been established ourselves as a leading supplier of quality janitor services and attractive online booking in the whole of Greater Noida.Ours is Covai's most efficient and affordable premier cleaning service. Meticulous Household Services have earned a reputation for high quality customer service, effective cleaning solutions and low price janitorial services. When you choose Meticulous, you get the best offers with excellent services. Our specialists guarantees 100% customer satisfaction.What are you waiting for? Go checkout our surprising services! Don't miss out.....</div>

<br>
<div class="text-three"><marquee width="100%" text-align="center" direction="down" height="100px" scrollamount="1"><center>Motto: "The objective of cleaning is not just to clean, but to make you feel happy living within that environment!"</center></marquee></div>

</div>
</div>
</section>


<%-- ================= CLEANING ================= --%>
<section class="about" id="about">
<div class="content">
<div class="title"><span>Cleaning Service</span></div>
<div class="about-details">
<div class="left">
<img src="${ctx}/image/clean.jpg" alt="">
</div>
<div class="right">
<div class="topic"></div>
<p>All you want is to keep your house looking clean & good and the best way to do that is by having a everyday house cleaning service from our Meticulous Household Services. House cleaning service is the best if you want it to look great without having to fuss over the details yourself.</p>

<div class="db-services">
    <h4>Available Services</h4>
    <c:forEach var="s" items="${cleaningServices}">
        <div class="db-service-card">
            <div class="info">
                <strong><c:out value="${s.name}"/></strong>
                <small><c:out value="${s.description}"/></small>
            </div>
            <span class="price">&#8377; <fmt:formatNumber value="${s.price}" pattern="#,##0.00"/></span>
            <a class="book-btn" href="${ctx}/booking?action=new&serviceId=${s.id}">Book Now</a>
        </div>
    </c:forEach>
    <c:if test="${empty cleaningServices}"><p>No services available right now.</p></c:if>
</div>

<div class="boxes">
<div class="box">
<div class="icon">
  <i class="fas fa-phone"></i>
</div>
    <div class="topic">Contact</div>

    <p><center>ALPHA 1 - <a style="color: #030303">0452 89652<br></a>
      ALPHA 2 - <a style="color: #030303">0452 89653<br></a>
      GREATER NOIDA - <a style="color: #030303">0452 89654<br></a></center></p>
</div>
</div>
</div>
</div>
</div>
</section>


<%-- ================= HANDYMAN ================= --%>
<section class="skills" id="skills">
<div class="content">
<div class="title"><span>Handyman</span></div>
<div class="skill-details">
  <div class="right">
  <img src="${ctx}/image/handyman.jpg" alt="">
  </div>
<div class="left">
<div class="topic"></div>
<p>Need a handyman? No worries, Our handyman team will help with issues around your home. Art & Mirror Hanging Carpentry, Ceiling Fan Installation, Deck Repair, Door Replacement or Adjustment, Drywall Repair, Furniture Assembly, Irrigation Repair, Light Bulb replacement, Light Fixture Installation, Painting Interior and Exterior, Plumbing Repairs and Adjustments, Shelf Installation, Tub, Sink and Toilet Snaking, Tile and Tile Repair, TV and Wiring and Installation are the best services provided.</p>

<div class="db-services">
    <h4>Available Services</h4>
    <c:forEach var="s" items="${handymanServices}">
        <div class="db-service-card">
            <div class="info">
                <strong><c:out value="${s.name}"/></strong>
                <small><c:out value="${s.description}"/></small>
            </div>
            <span class="price">&#8377; <fmt:formatNumber value="${s.price}" pattern="#,##0.00"/></span>
            <a class="book-btn" href="${ctx}/booking?action=new&serviceId=${s.id}">Book Now</a>
        </div>
    </c:forEach>
    <c:if test="${empty handymanServices}"><p>No services available right now.</p></c:if>
</div>

<div class="boxes">
<div class="box">
<div class="icon">
  <i class="fas fa-phone"></i>
</div>
    <div class="topic">Contact</div>

    <p><center>ALPHA 1 - <a style="color: #030303">0452 89652<br></a>
      ALPHA 2 - <a style="color: #030303">0452 89662<br></a>
      GREATER NOIDA - <a style="color: #030303">0452 89672<br></a></center></p>

</div>
</div>
</div>
</div>
</div>
</section>


<%-- ================= GARDEN ================= --%>
<section class="services" id="services">
<div class="content">
<div class="title"><span>Garden Maintenance</span></div>
<div class="service-details">
  <div class="right">
  <img src="${ctx}/image/garden.jpg" alt="">
  </div>
<div class="left">
<div class="topic"></div>
<p>Meticulous is GREANO's most trusted and affordable premier gardening service. You get the best offers available and negotiate your terms with the top service providers, when you choose Meticulous.We provide all types of Garden Maintenance such as mowing, pruning, and fertilizing, as well as disposal services, like removing brush and other debris. Now get ready to make your lawn a Heaven!!!</p>

<div class="db-services">
    <h4>Available Services</h4>
    <c:forEach var="s" items="${gardenServices}">
        <div class="db-service-card">
            <div class="info">
                <strong><c:out value="${s.name}"/></strong>
                <small><c:out value="${s.description}"/></small>
            </div>
            <span class="price">&#8377; <fmt:formatNumber value="${s.price}" pattern="#,##0.00"/></span>
            <a class="book-btn" href="${ctx}/booking?action=new&serviceId=${s.id}">Book Now</a>
        </div>
    </c:forEach>
    <c:if test="${empty gardenServices}"><p>No services available right now.</p></c:if>
</div>

<div class="boxes">
<div class="box">
<div class="icon">
  <i class="fas fa-phone"></i>
</div>
    <div class="topic">Contact</div>

    <p><center>ALPHA 1 - <a style="color: #030303">0452 39652<br></a>
      ALPHA 2 - <a style="color: #030303">0452 29662<br></a>
     GREATER NOIDA- <a style="color: #030303">0452 19672<br></a></center></p>

</div>
</div>
</div>
</div>
</div>
</section>


<%-- ================= CONTACT ================= --%>
<section class="contact" id="contact">
  <div class="contact-details">
<div class="content">
<div class="title"><span>Contact Us</span></div>
<div class="text">
<div class="topic">

        <%-- Result message from EnquiryServlet --%>
    <c:if test="${param.enquiry == 'sent'}">
        <div class="enquiry-msg enquiry-ok">Thank you! Your enquiry has been received. We will contact you soon.</div>
    </c:if>
    <c:if test="${param.enquiry == 'invalid'}">
        <div class="enquiry-msg enquiry-bad">Please fill all fields correctly. Mobile number must be 10 digits.</div>
    </c:if>

    <form action="${ctx}/enquiry" method="post" onsubmit="return checkEnquiry()">
        <input type="hidden" name="action" value="insert">
        <i style="color:#FA8072;font-size:30px;">
        Can We help You?</i><br><center>

             <i style="font-size:20px;">
             Name :  <input type="text" id="enqName" name="name" maxlength="100" required />
             <br>
             Email-ID : <input type="email" id="enqEmail" name="email" maxlength="100" required /><br>
             Mobile Number : <input type="text" id="enqMobile" name="mobile" maxlength="10" required />

             <br>
             Kind of service needed : <input type="text" id="enqService" name="service" maxlength="150" required /></i></center>
             <div class="button" >
             <button type="submit">Send Enquiry</button>
           </div><br>
    </form>

    <script>
        // Client-side check (EnquiryServlet validates again on the server)
        function checkEnquiry() {
            var mobile = document.getElementById("enqMobile").value.trim();
            if (!/^\d{10}$/.test(mobile)) {
                alert("Mobile number must be exactly 10 digits.");
                return false;
            }
            return true;
        }
    </script>
          <br><i style="color:#FA8072;font-size:30px;">For any queries just ping us in</i><br>
<br>
  <center><i class="fa fa-phone-square" style="color:#FA8072;"><h6 style="font-size:15px;color:black;font-family: poppins;"> FOUNDERS -SAJJAD ALI(9876564312), SANJIT KUMAR SINGH(9987652341),SAHITYA SHARMA(786543298)</h6></i></center><br>
  <center><i class="fa fa-home" style="color:#FA8072;"><h6 style="font-size:20px;color:black;">GALGOTIAS COLLEGE OF ENGINEERING AND TECHNOLOGY, GREATER NOIDA</h6></i></center><br>
    <center><i class="fa fa-envelope" style="color:#FA8072;"><h6 style="font-size:20px;color:black;">meticulousgalgotias986@gmail.com</h6></i></center>
    </div>

<div class="boxes">
<div class="box">
<div class="topic">
</div>
<br>
<br>
<footer>
<div class="text">
<span><br> <a href="#"></a>  &#169; meticulous.com All Rights reserved. | Meticulous</span>

</div>
</footer>
</div>
</div>
</div>
</div>
</div>
</section>

<script src="${ctx}/js/script.js"></script>
</body>
</html>
