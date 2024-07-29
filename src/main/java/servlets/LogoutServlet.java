package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
	 @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Get the session and invalidate it
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }

	        // Redirect to the login page
	        response.sendRedirect("home.jsp");
	        
	    	      	        
    }
	 
}
