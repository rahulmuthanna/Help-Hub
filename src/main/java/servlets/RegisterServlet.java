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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String department = request.getParameter("department");
        String contactNumber = request.getParameter("contactNumber");
        String password = request.getParameter("password");

        try (Connection connection = DatabaseUtil.getConnection()) {
            // Check if the username already exists
            String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserSql);
            checkUserStatement.setString(1, username);
            ResultSet resultSet = checkUserStatement.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                // Username already exists
                response.sendRedirect("register.jsp?usernameError=true");
                return;
            }

            // Insert the new user
            String insertUserSql = "INSERT INTO users (username, password, department, contact_number) VALUES (?, ?, ?, ?)";
            PreparedStatement insertUserStatement = connection.prepareStatement(insertUserSql);
            insertUserStatement.setString(1, username);
            insertUserStatement.setString(2, password);
            insertUserStatement.setString(3, department);
            insertUserStatement.setString(4, contactNumber);
            insertUserStatement.executeUpdate();

            response.sendRedirect("login.jsp");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=true");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported");
    }
}
