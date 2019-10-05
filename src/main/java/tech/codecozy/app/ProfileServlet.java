package tech.codecozy.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
			HttpSession session = request.getSession();
			if(session.getAttribute("user") == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				User sessionUser = (User) session.getAttribute("user");
				String username = dbUtil.getUserByEmail(sessionUser.getEmail()).getUsername();
				response.sendRedirect("profile?user="+username);
			}
		} else {
			if(!dbUtil.usernameExists(user)) {
				response.sendRedirect("search");
			} else {
				User profile = dbUtil.getUserByUsername(user); 
				HashMap<Long,Integer> map = dbUtil.getParticipatedContestsByUsername(user);
				
				request.setAttribute("USER", profile);
				request.setAttribute("CONTESTS_PARTICIPATED", map);
				RequestDispatcher rd = request.getRequestDispatcher("profile.jsp");
				rd.forward(request, response);
			}
		}
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
