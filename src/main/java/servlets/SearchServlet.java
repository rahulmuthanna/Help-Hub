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

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        List<Post> posts = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String sql = "SELECT * FROM posts WHERE keyword LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setProblemStatement(resultSet.getString("problem_statement"));
                post.setDescription(resultSet.getString("description"));
                post.setKeyword(resultSet.getString("keyword"));
                post.setUserId(resultSet.getInt("user_id"));
                posts.add(post);
            }

            request.setAttribute("posts", posts);
            request.getRequestDispatcher("searchResults.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("home.jsp?error=true");
        }
    }
}
