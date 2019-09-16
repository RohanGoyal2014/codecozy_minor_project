package tech.codecozy.app;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/problem")
public class ProblemControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	
    public ProblemControllerServlet() {
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
				if(!dbUtil.emailExists(email) || !dbUtil.problemExists(id)) {
					response.sendRedirect("dashbaord");
					return;
				} else {
					Problem problem = dbUtil.getProblem(id);
					request.setAttribute("PROBLEM", problem);
					request.setAttribute("CONTEST", dbUtil.getContest(String.valueOf(problem.getContestId())));
					request.setAttribute("CURRENT_TIMESTAMP", System.currentTimeMillis());
				}
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher("problem.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
