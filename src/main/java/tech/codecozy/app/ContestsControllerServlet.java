package tech.codecozy.app;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/contests")
public class ContestsControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	
    public ContestsControllerServlet() {
        super();
    }
    

	@Override
	public void init() throws ServletException {
		super.init();
		dbUtil = new DBUtil();
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		if(id == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} else {
			HttpSession session = request.getSession(false);
			if(session == null || session.getAttribute("user") == null) {
				response.sendRedirect("dashboard");
				return;
			} else {
				User user = (User) session.getAttribute("user");
				String email = user.getEmail();
				if(!dbUtil.emailExists(email) || !dbUtil.contestExists(id)) {
					response.sendRedirect("dashboard");
					return;
				} else {
					ArrayList<Problem> problem = dbUtil.getProblems(id);
					request.setAttribute("CURRENT_TIMESTAMP", System.currentTimeMillis());
					request.setAttribute("CONTEST", dbUtil.getContest(id));
					request.setAttribute("PROBLEMS", problem);
				}
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher("contest.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
