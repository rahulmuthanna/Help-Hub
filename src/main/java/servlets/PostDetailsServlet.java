package servlets;

import utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Post;

@WebServlet("/postDetails")
public class PostDetailsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String postId = request.getParameter("postId");
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            handleDelete(request, response, postId);
            return;
        }

        showPostDetails(request, response, postId);
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

    private void handleDelete(HttpServletRequest request, HttpServletResponse response, String postId) throws ServletException, IOException {
        String loggedInUsername = (String) request.getSession().getAttribute("username");

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Check if the post belongs to the logged-in user
            String checkPostSql = "SELECT u.username FROM posts p JOIN users u ON p.user_id = u.id WHERE p.id = ?";
            PreparedStatement checkPostStmt = connection.prepareStatement(checkPostSql);
            checkPostStmt.setInt(1, Integer.parseInt(postId));
            ResultSet checkPostResultSet = checkPostStmt.executeQuery();

            if (checkPostResultSet.next()) {
                String postUsername = checkPostResultSet.getString("username");

                /* Print statements for debugging
                System.out.println("Logged-in username: " + loggedInUsername);
                System.out.println("Post's username: " + postUsername);*/

                if (loggedInUsername != null && loggedInUsername.equals(postUsername)) {
                    // Perform deletion
                    String deleteSql = "DELETE FROM posts WHERE id = ?";
                    PreparedStatement deleteStmt = connection.prepareStatement(deleteSql);
                    deleteStmt.setInt(1, Integer.parseInt(postId));
                    int rowsAffected = deleteStmt.executeUpdate();

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
}
