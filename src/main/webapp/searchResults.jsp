<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="header.jsp" %>
    <%@ page import="java.util.List" %>
    <%@ page import="models.Post" %>
<!DOCTYPE html>
<html>
<head>
    <title>Search Results</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
     <script src="js/scripts.js" defer></script>    
</head>
<body>
    
   <div class="tile-container">
    <%
        List<Post> posts = (List<Post>) request.getAttribute("posts");
        if (posts != null && !posts.isEmpty()) {
            for (Post post : posts) {
    %>
        <div class="tile" onclick="window.location.href='postDetails?postId=<%= post.getId() %>'">
            <div class="tile-content">
                <h3><%= post.getProblemStatement() %></h3>
                <p class="tile-description"><%= post.getDescription() %></p>
                <p class="tile-keywords"><strong>Keywords:</strong> <%= post.getKeyword() %></p>
            </div>
        </div>
    <%
            }
        } else {
    %>
        <p>No results found. Please try again.</p>
    <%
        }
    %>
</div>

    
</body>
</html>