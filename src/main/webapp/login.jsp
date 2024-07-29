<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header_simple.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <script src="js/scripts.js" defer></script>
</head>
<body>
    <div class="form-container">
        <h1>Login</h1>
        <form action="login" method="post">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" class="input-field" placeholder="Enter Username" required><br>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" class="input-field" placeholder="Enter Password" required><br>
            <input type="submit" value="Login" class="submit-button">
        </form>
        <p class="subtext" >Not a user?  <a href="register.jsp" class="subtext_link">Register now</a></p>
       
        <%
            if (request.getParameter("error") != null) {
                out.println("<p style='color:red;' class='error-message'>Invalid login. Please try again.</p>");
            }
        %>
    </div>
</body>
</html>
