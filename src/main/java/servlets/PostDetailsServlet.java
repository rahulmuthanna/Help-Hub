package servlets;

import utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Post;
import models.Comment;

@WebServlet("/postDetails")
public class PostDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postId = request.getParameter("postId");
        String action = request.getParameter("action");
        String commentId = request.getParameter("commentId");

        if ("delete".equals(action)) {
            handleDelete(request, response, postId);
            return;
        }

        if ("deleteComment".equals(action) && commentId != null) {
            handleDeleteComment(request, response, commentId);
            return;
        }

        showPostDetails(request, response, postId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postId = request.getParameter("postId");
        String commentContent = request.getParameter("comment");

        if (request.getSession().getAttribute("username") != null && commentContent != null && !commentContent.trim().isEmpty()) {
            saveComment(request, response, postId, commentContent);
        }

        response.sendRedirect("postDetails?postId=" + postId);
    }

    private void showPostDetails(HttpServletRequest request, HttpServletResponse response, String postId) throws ServletException, IOException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT p.id, p.problem_statement, p.description, p.keyword, u.username, u.department, u.contact_number "
                       + "FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(postId));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setProblemStatement(resultSet.getString("problem_statement"));
                post.setDescription(resultSet.getString("description"));
                post.setKeyword(resultSet.getString("keyword"));
                post.setUsername(resultSet.getString("username"));
                post.setDepartment(resultSet.getString("department"));
                post.setContactNumber(resultSet.getString("contact_number"));

                post.setComments(loadComments(connection, post.getId()));

                request.setAttribute("post", post);
                request.getRequestDispatcher("postDetails.jsp").forward(request, response);
            } else {
                response.sendRedirect("home.jsp?error=true");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?error=true");
        }
    }

    private List<Comment> loadComments(Connection connection, int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, post_id, username, content, timestamp FROM comments WHERE post_id = ? ORDER BY timestamp ASC";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, postId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Comment comment = new Comment();
            comment.setId(resultSet.getInt("id"));
            comment.setPostId(resultSet.getInt("post_id"));
            comment.setUsername(resultSet.getString("username"));
            comment.setContent(resultSet.getString("content"));
            comment.setTimestamp(resultSet.getTimestamp("timestamp"));
            comments.add(comment);
        }

        return comments;
    }

    private void saveComment(HttpServletRequest request, HttpServletResponse response, String postId, String commentContent) {
        String username = (String) request.getSession().getAttribute("username");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO comments (post_id, username, content, timestamp) VALUES (?, ?, ?, NOW())";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(postId));
            statement.setString(2, username);
            statement.setString(3, commentContent);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, String postId) throws ServletException, IOException {
        String loggedInUsername = (String) request.getSession().getAttribute("username");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String checkPostSql = "SELECT u.username FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = ?";
            PreparedStatement checkPostStmt = connection.prepareStatement(checkPostSql);
            checkPostStmt.setInt(1, Integer.parseInt(postId));
            ResultSet checkPostResultSet = checkPostStmt.executeQuery();

            if (checkPostResultSet.next()) {
                String postUsername = checkPostResultSet.getString("username");

                if (loggedInUsername != null && loggedInUsername.equals(postUsername)) {
                    String deleteCommentsSql = "DELETE FROM comments WHERE post_id = ?";
                    PreparedStatement deleteCommentsStmt = connection.prepareStatement(deleteCommentsSql);
                    deleteCommentsStmt.setInt(1, Integer.parseInt(postId));
                    deleteCommentsStmt.executeUpdate();

                    String deletePostSql = "DELETE FROM posts WHERE id = ?";
                    PreparedStatement deletePostStmt = connection.prepareStatement(deletePostSql);
                    deletePostStmt.setInt(1, Integer.parseInt(postId));
                    int rowsAffected = deletePostStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        response.sendRedirect("home.jsp?deleteSuccess=true");
                    } else {
                        response.sendRedirect("home.jsp?error=true");
                    }
                } else {
                    response.sendRedirect("home.jsp?postId=" + postId + "&error=permission");
                }
            } else {
                response.sendRedirect("home.jsp?error=true");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?error=true");
        }
    }

    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response, String commentId) throws ServletException, IOException {
        String loggedInUsername = (String) request.getSession().getAttribute("username");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String checkCommentSql = "SELECT username FROM comments WHERE id = ?";
            PreparedStatement checkCommentStmt = connection.prepareStatement(checkCommentSql);
            checkCommentStmt.setInt(1, Integer.parseInt(commentId));
            ResultSet checkCommentResultSet = checkCommentStmt.executeQuery();

            if (checkCommentResultSet.next()) {
                String commentUsername = checkCommentResultSet.getString("username");

                if (loggedInUsername != null && loggedInUsername.equals(commentUsername)) {
                    String deleteCommentSql = "DELETE FROM comments WHERE id = ?";
                    PreparedStatement deleteCommentStmt = connection.prepareStatement(deleteCommentSql);
                    deleteCommentStmt.setInt(1, Integer.parseInt(commentId));
                    int rowsAffected = deleteCommentStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        response.sendRedirect("postDetails?postId=" + request.getParameter("postId") + "&deleteCommentSuccess=true");
                    } else {
                        response.sendRedirect("postDetails?postId=" + request.getParameter("postId") + "&error=true");
                    }
                } else {
                    response.sendRedirect("postDetails?postId=" + request.getParameter("postId") + "&error=permission");
                }
            } else {
                response.sendRedirect("postDetails?postId=" + request.getParameter("postId") + "&error=true");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("postDetails?postId=" + request.getParameter("postId") + "&error=true");
        }
    }
}







