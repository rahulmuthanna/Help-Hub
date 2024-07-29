<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection, java.sql.PreparedStatement, java.sql.ResultSet, java.sql.SQLException" %>
<%@ page import="utils.DatabaseUtil" %>
<%@ include file="header.jsp" %>
 <%@ page import="java.util.List" %>
    <%@ page import="models.Post" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <script src="js/scripts.js" defer></script>
</head>
<body>
   <body>
<div class="tile-container">
    <%
        // Fetching posts from the database
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DatabaseUtil.getConnection();
            String sql = "SELECT id, problem_statement, description, keyword FROM posts";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int postId = resultSet.getInt("id");
                String problemStatement = resultSet.getString("problem_statement");
                String description = resultSet.getString("description");
                String keyword = resultSet.getString("keyword");
    %>
                <div class="tile" onclick="window.location.href='postDetails?postId=<%= postId %>'">
                    <div class="tile-content">
                        <h3><%= problemStatement %></h3>
                        <p class="tile-description"><%= description %></p>
                        <p class="tile-keywords"><strong>Keywords:</strong> <%= keyword %></p>
                    </div>
                </div>
    <%
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (statement != null) try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (connection != null) try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    %>
</div>

    <button class="floating-button" onclick="createPost()">
        <i class="fas fa-plus"></i>
    </button>


</body>
</html>
