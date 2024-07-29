package servlets;

import utils.DatabaseUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/createPost")
public class CreatePostServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreatePostServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String problemStatement = request.getParameter("problemStatement");
        String description = request.getParameter("description");
        String keyword = request.getParameter("keyword");

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "INSERT INTO posts (problem_statement, description, keyword, user_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, problemStatement);
            statement.setString(2, description);
            statement.setString(3, keyword);
            statement.setInt(4, userId);
            statement.executeUpdate();
            response.sendRedirect("home.jsp");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating post", e);
            response.sendRedirect("createPost.jsp?error=true");
        }
    }
}
