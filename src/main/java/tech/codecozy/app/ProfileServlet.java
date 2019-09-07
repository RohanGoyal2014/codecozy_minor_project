package tech.codecozy.app;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	
    public ProfileServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		super.init();
		dbUtil = new DBUtil();
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String user = request.getParameter("user");
		if(user == null) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);	
		} else {
			if(!dbUtil.usernameExists(user)) {
				response.sendRedirect("search");
			} else {
				User profile = dbUtil.getUserByUsername(user);
				request.setAttribute("USER", profile);
				RequestDispatcher rd = request.getRequestDispatcher("profile.jsp");
				rd.forward(request, response);
			}
		}
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
