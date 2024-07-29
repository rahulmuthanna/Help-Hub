<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Help-Hub</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="styles.css"> <!-- Ensure this link points to your CSS file -->
</head>
<body>
    <div class="header">
        <div class="logo">
            <a href="home.jsp"><img src="images/logo.png" alt="Logo" class="logo-img"> Help-Hub</a>
        </div>

        <div class="search-bar">
            <form action="search" method="get">
                <input type="text" name="keyword" placeholder="Search...">
                <button type="submit"><i class="fa fa-search"></i></button>
            </form>
        </div>

        <div class="user-info">
            <c:choose>
                <c:when test="${not empty sessionScope.username}">
                    <!-- User is logged in -->
                    <span><%= session.getAttribute("username") %></span>
                    <a href="http://localhost:8080/Help-Hub/logout" class="auth-button">Log Out</a>
                </c:when>
                <c:otherwise>
                    <!-- User is not logged in -->
                    <a href="login.jsp" class="auth-button">Log In</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
