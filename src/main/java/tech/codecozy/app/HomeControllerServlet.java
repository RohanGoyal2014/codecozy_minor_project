package tech.codecozy.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class HomeControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DBUtil dbUtil;
	
    public HomeControllerServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		
		super.init();
		dbUtil = new DBUtil();
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("user") == null) {
			response.sendRedirect("accounts");
		} else {
			User user = (User)session.getAttribute("user");
			String email = user.getEmail();
			if(!dbUtil.emailExists(email)) {
				session.invalidate();
				response.sendRedirect("accounts");
			} else {
				if(!dbUtil.isVerified(email)) {
					RequestDispatcher rd = request.getRequestDispatcher("verifyUser.jsp");
					rd.forward(request, response);
				} else {
					ArrayList<Contest> contests = dbUtil.fetchContests();
					
					ArrayList<Contest> pastContests = new ArrayList<>();
					ArrayList<Contest> ongoingContests = new ArrayList<>();
					ArrayList<Contest> upcomingContests = new ArrayList<>();
					
					splitContestData(contests,pastContests,ongoingContests, upcomingContests);
					
					request.setAttribute("PAST_CONTEST_LIST", pastContests);
					request.setAttribute("ONGOING_CONTEST_LIST", ongoingContests);
					request.setAttribute("UPCOMING_CONTEST_LIST", upcomingContests);
					request.setAttribute("IS_ADMIN",dbUtil.isAdmin(email));
					RequestDispatcher rd= request.getRequestDispatcher("dashboard.jsp");
					rd.forward(request, response);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String command = request.getParameter("command");
		if(command == null) {
			doGet(request, response);
		} else {
			try {
				switch(command) {
				case "verifyAccount":
					verifyAccount(request, response);
					break;
				default:
					doGet(request, response);
				} 
			} catch(Exception e) {
				e.printStackTrace();
				doGet(request, response);
			}
		}
	}
	
	private void verifyAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("user") == null) {
			doGet(request, response);
		} else {
			String email = ((User)session.getAttribute("user")).getEmail();
			if(!dbUtil.emailExists(email)) {
				doGet(request, response);
			} else if(dbUtil.isVerified(email)) {
				doGet(request, response);
			} else {
				String subject = "Codecozy Verification Mail";
				String content = "Here is your verification mail:\n";
				String link = CloudConfig.getBaseUrl(request)
						+ "/accounts?action=verifyUser&"
						+ "user="+new CryptoUtil().urlSafeEncrypt(CryptoUtil.KEY, email);
				content += link+" \n This link is valid for 15 mins only";
				MailUtil.send(email, subject, content);
				dbUtil.markVerificationMailSentTime(email);
				request.setAttribute("MESSAGE", "Mail Sent. If you did not receive the mail, click Send button above again or try contacting administrator");
				RequestDispatcher rd = request.getRequestDispatcher("verifyUser.jsp");
				rd.forward(request, response);
			}
		}
	}
	
	private void splitContestData(ArrayList<Contest> contests, ArrayList<Contest> pastContests,
			ArrayList<Contest> ongoingContests, ArrayList<Contest> upcomingContests) {
		pastContests.clear();
		ongoingContests.clear();
		upcomingContests.clear();
		long currentTimestamp = System.currentTimeMillis();
		for(Contest i:contests) {
			if(i.getStart()<=currentTimestamp && i.getEnd()>=currentTimestamp) {
//				System.out.println("Hey"+currentTimestamp);
				ongoingContests.add(i);
			} else if(i.getEnd()<currentTimestamp) {
				pastContests.add(i);
			} else {
//				System.out.println("Amm"+currentTimestamp);
				upcomingContests.add(i);
			}
		}
		//Since latest passed contests are to be viewed first
		Collections.reverse(pastContests);
	}

}
