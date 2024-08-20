<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<%@ page import="models.Post, models.Comment" %>
<%@ page import="java.util.List" %>
<%@ page session="true" %>   

<!DOCTYPE html>
<html>
<head>
    <title>Post Details</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <%
    String loggedInUsername = (String) session.getAttribute("username");
%>
    <script>
        var loggedInUsername = '<%= session.getAttribute("username") != null ? session.getAttribute("username").toString() : "" %>';
        console.log("Logged-in username: " + loggedInUsername); // Debugging output

        function confirmDelete(id, username, type) {
            console.log("Logged-in username: " + loggedInUsername);
            console.log("Target username: " + username);

            if (loggedInUsername === username) {
                var message = (type === 'post') ? 'post' : 'comment';
                if (confirm('Are you sure you want to delete this ' + message + '?')) {
                    var action = (type === 'post') ? 'delete' : 'deleteComment';
                    var param = (type === 'post') ? 'postId=' + id : 'commentId=' + id;
                    window.location.href = 'postDetails?' + param + '&action=' + action;
                }
            } else {
                alert('Only the creator of the ' + message + ' can delete it.');
            }
        }
    </script>
    <style>
        /* Comment Section Styling */
        .comments-section {
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }

        .comments-section h3 {
            margin-top: 0;
            font-size: 1.5em;
            color: #333;
        }

        .comment-tile {
            margin-bottom: 15px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #fff;
            display: flex;
            flex-direction: column;
            position: relative;
        }

        .comment-header {
            font-weight: bold;
            margin-bottom: 5px;
            display: flex;
            justify-content: space-between;
        }

        .comment-header .username {
            flex: 1;
        }

        .comment-header .comment-timestamp {
            font-size: 0.9em;
            color: #888;
        }

        .comment-content {
            margin: 10px 0;
        }

        .delete-button {
            background-color: #f44336;
            color: #fff;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
        }

        .delete-button:hover {
            background-color: #d32f2f;
        }

        .comment-form {
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
            display: flex;
            align-items: center;
        }

        .comment-form textarea {
            flex: 1;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
            margin-right: 10px;
        }

        .comment-form button {
            background-color: #4CAF50;
            color: #fff;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            margin-left: auto; /* Moves the button to the right */
        }

        .comment-form button:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <br></br>
    <% 
        Post post = (Post) request.getAttribute("post");
        if (post != null) {
    %>
        <p class='problem-statement'><%= post.getProblemStatement() %></p>
        <p class='postedby'><strong>Posted by:</strong> <%= post.getUsername() %> (<%= post.getDepartment() %>, <%= post.getContactNumber() %>)</p>
        <hr class='separator'>
        <div class='description-container'>
            <p class='description'><%= post.getDescription() %></p>
            <div class='keywords'>
                <% 
                    String[] keywords = post.getKeyword().split("[, ]");
                    for (String keyword : keywords) {
                        String trimmedKeyword = keyword.trim();
                        if (!trimmedKeyword.isEmpty()) {
                %>
                    <div class='keyword-tile'><a class='key_link' href='search?keyword=<%= trimmedKeyword %>'><%= trimmedKeyword %></a></div>
                <% 
                        }
                    }
                %>
            </div>
        </div>
        
        <!-- Display Comments -->
        <div class='comments-section'>
            <h3>Comments</h3>
            <% 
                List<Comment> comments = post.getComments();
                if (comments != null && !comments.isEmpty()) {
                    for (Comment comment : comments) {
            %>
                <div class='comment-tile'>
                    <div class='comment-header'>
                        <span class='username'><%= comment.getUsername() %></span>
                        <span class='comment-timestamp'><%= comment.getTimestamp() %></span>
                    </div>
                    <p class='comment-content'><%= comment.getContent() %></p>
                    <% 
                    if (loggedInUsername != null && loggedInUsername.equals(comment.getUsername())) { %>
                        <button class='delete-button' onclick="confirmDelete('<%= comment.getId() %>', '<%= comment.getUsername() %>', 'comment')">
                            <i class='fas fa-trash'></i>
                        </button>
                    <% } %>
                </div>
            <% 
                    }
                } else {
            %>
                <p>No comments yet.</p>
            <% 
                }
            %>
        </div>
    <% 
        } else {
    %>
        <p>Post not found.</p>
    <% 
        }
    %>

    <%-- Delete Button for Post --%>
    <% if (post != null) { %>
        <button class="delete-button" onclick="confirmDelete('<%= post.getId() %>', '<%= post.getUsername() %>', 'post')">
            <i class="fas fa-trash"></i>
        </button>
    <% } %>

    <%-- Comment Form --%>
    <div class="comment-form">
        <form action="postDetails" method="post" style="display: flex; width: 100%;">
            <input type="hidden" name="postId" value="<%= post != null ? post.getId() : "" %>">
            <% if (session.getAttribute("username") != null) { %>
                <textarea name="comment" rows="4" placeholder="Add your comment here..."></textarea>
                <button type="submit">Post Comment</button>
            <% } else { %>
                <p>You need to <a href="login.jsp">log in</a> to post a comment.</p>
            <% } %>
        </form>
    </div>
</body>
</html>
