<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header_simple.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Create Post</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <script src="js/scripts.js" defer></script>
</head>
<body>
    <%
        String username = (String) session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }
    %>
    <div class="form-container">
        <form action="createPost" method="post" onsubmit="return validatePostForm()">
            <label for="problemStatement">Create Post:</label>
            <textarea id="problemStatement" name="problemStatement" placeholder="Enter Problem Statement..." required></textarea><br>
            <textarea id="description" name="description" rows="10" cols="50" placeholder="Enter Description..." required oninput="autoResize(this)"></textarea><br>
            <input type="text" id="keyword" name="keyword" placeholder="Enter Keyword..." required><br>
            <button type="submit" class="submit-button">Create Post</button>
        </form>
        <%
            if (request.getParameter("error") != null) {
                out.println("<p style='color:red;'>Post creation failed. Please try again.</p>");
            }
        %>
    </div>
</body>
</html>
	