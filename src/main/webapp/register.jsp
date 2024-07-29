<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header_simple.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <script src="js/scripts.js" defer></script>
</head>
<body>
    <div class="form-container">
        <h1>Register</h1>
        <form action="register" method="POST">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" placeholder="Enter Username" required><br>
            <%
                if (request.getParameter("usernameError") != null) {
                    out.println("<p style='color:red;' class='error-message'>Username already exists</p>");
                }
            %>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" placeholder="Enter Password" required><br>
            <label for="department">Department:</label>
            <input type="text" id="department" name="department" placeholder="Enter Department" required><br>
            <label for="contactNumber">Contact Number:</label>
            <input type="text" id="contactNumber" name="contactNumber" placeholder="Enter Contact Number" required><br>
            <input type="submit" value="Register" class="submit-button">
        </form>
        <%
            if (request.getParameter("error") != null) {
                out.println("<p style='color:red;' class='error-message'>Registration failed. Please try again.</p>");
            }
        %>
    </div>
</body>
</html>
