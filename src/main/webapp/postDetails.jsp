<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%@ page import="models.Post" %>
<%@ page session="true" %>   
<!DOCTYPE html>
<html>
<head>
    <title>Post Details</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <script>
        // Pass the logged-in username to JavaScript
        var loggedInUsername = '<%= session.getAttribute("username") %>';
        
        function confirmDelete(postId, postUsername) {
            // Print statements for debugging
            console.log("Logged-in username: " + loggedInUsername);
            console.log("Post's username: " + postUsername);

            if (loggedInUsername === postUsername) {
                if (confirm('Are you sure you want to delete this post?')) {
                    window.location.href = 'postDetails?postId=' + postId + '&action=delete';
                }
            } else {
                alert('Only the creator of the post can delete it.');
            }
        }
    </script>
  
</head>
<body>
    <br></br>
     <%
        Post post = (Post) request.getAttribute("post");
        if (post != null) {
            out.println("<p class='problem-statement'>" + post.getProblemStatement() + "</p>");
            out.println("<p class='postedby'><strong>Posted by:</strong> " + post.getUsername() + " (" + post.getDepartment() + ", " + post.getContactNumber() + ")</p>");
            out.println("<hr class='separator'>");
            out.println("<div class='description-container'>");
            out.println("<p class='description'>" + post.getDescription() + "</p>");
            out.println("<div class='keywords'>");
            String[] keywords = post.getKeyword().split("[, ]");
            for (String keyword : keywords) {
                String trimmedKeyword = keyword.trim();
                if (!trimmedKeyword.isEmpty()) {
                	  out.println("<div class='keyword-tile'><a class='key_link' href='search?keyword=" + trimmedKeyword + "'>" + trimmedKeyword + "</a></div>");
                }
            }
            out.println("</div>");
            out.println("</div>");
        }
    %>
    
    <%-- Round Delete Button --%>
    <button class="delete-button" onclick="confirmDelete('<%= post.getId() %>', '<%= post.getUsername() %>')">
        <i class="fas fa-trash"></i>
    </button>
</body>
</html>
