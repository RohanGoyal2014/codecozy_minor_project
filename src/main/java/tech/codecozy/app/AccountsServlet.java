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

import com.google.gson.Gson;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	DBUtil dbUtil;
	
    public AccountsServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			dbUtil = new DBUtil();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		try {
			String action = request.getParameter("action");
			String user = request.getParameter("user");
			if(action != null && user != null && action.equals("verifyUser")) {
				// User Verification Management Routing Path
				String email = new CryptoUtil().urlSafeDecrypt(CryptoUtil.KEY, user);
				if(!dbUtil.emailExists(email) || dbUtil.isVerified(email) || dbUtil.isUserVerificationTimedOut(email)) {
					response.sendRedirect("accounts");
				} else {
					dbUtil.markVerified(email);
					RequestDispatcher rd = request.getRequestDispatcher("youareverified.jsp");
					rd.forward(request, response);
				}
			} else {
				if(session == null) {	
					//Session doesn't exist
					RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
					rd.forward(request, response);
				} else {
					if(session.getAttribute("user") == null) {
						//User not logged in(even when session exists)
						action = request.getParameter("action");
						user = request.getParameter("user");
						if(action != null && action.equals("resetPassword") && user!=null && dbUtil.emailExists(new CryptoUtil().urlSafeDecrypt(CryptoUtil.KEY, user))) {
							//Reset Password Routing Management
							RequestDispatcher rd = request.getRequestDispatcher("resetpasswordpage.jsp");
							request.setAttribute("USER", user);
							rd.forward(request, response);
						} else {
							//Invalid Reset Password Route
							RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
							rd.forward(request, response);
						}
					} else {
						//User logged in
						response.sendRedirect("dashboard");
						return;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			request.getSession().invalidate();
			response.sendRedirect("accounts");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String command = request.getParameter("command");
		if(command == null) {
			doGet(request, response);
		} else {
			try {
				switch(command) {
				case "login":
					performLogin(request,response);
					break;
				case "register":
					performRegister(request,response);
					break;
				case "forgot":
					recoverAccount(request, response);
					break;
				case "resetPassword":
					resetPasswordFromLink(request, response);
					break;
				case "api_usernames":
					getStoredUserNames(request, response);
					break;
				case "logout":
					logoutUser(request, response);
					break;
				default:
					doGet(request,response);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void performLogin(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String email = request.getParameter("lg_email");
		String password = request.getParameter("lg_password");
		
		//Adding mode 1 for identifying login tab on accounts.jsp
		request.setAttribute("MODE", 1);
		
		if(password == null || email == null) {
			request.setAttribute("ERROR", "Input Fields can not be null");
		} else {
			email = email.trim();
			password = password.trim();
			if(password.isEmpty()) {
				request.setAttribute("ERROR", "Password can not be empty");
			} else if(email.isEmpty()) {
				request.setAttribute("ERROR", "Email can not be left empty");
			} else {
				if(dbUtil.checkLoginCredentials(email, password)) {
					HttpSession session = request.getSession(true);
					User user = new User(null,null,email,null,null,'\0');
					session.setAttribute("user", user);
					
					response.sendRedirect("accounts");
					
					return;
				}
				request.setAttribute("ERROR","Either username/password incorrect");
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
		rd.forward(request, response);
	}	
	
	private void performRegister(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String email = request.getParameter("rg_email");
		String password = request.getParameter("rg_password");
		String confirmPassword = request.getParameter("rg_cpassword");
		String fname = request.getParameter("rg_fname");
		String lname = request.getParameter("rg_lname");
		String gender = request.getParameter("rg_gender");
		String username = request.getParameter("rg_uname");
		
		//Adding mode 2 for identifying register tab on accounts.jsp
		request.setAttribute("MODE", 2);
		
		if(email== null || password == null || confirmPassword == null || fname == null || lname == null || gender == null || username == null) {
			request.setAttribute("ERROR", "Input fields can not be null");
		} else {
			email = email.trim();
			password = password.trim();
			confirmPassword = confirmPassword.trim();
			fname = fname.trim();
			lname = lname.trim();
			gender = gender.trim();
			username = username.trim();
			if(fname.isEmpty()) {
				request.setAttribute("ERROR","First Name can not be empty");
			} else if(lname.isEmpty()) {
				request.setAttribute("ERROR", "Last Name can not be empty");
			} else if(gender.isEmpty()) {
				request.setAttribute("ERROR", "Gender Input is Invalid");
			} else if(email.isEmpty()) {
				request.setAttribute("ERROR", "Email can not be empty");
			} else if(username.isEmpty()) {
				request.setAttribute("ERROR", "Username can not be empty");
			} else if(password.isEmpty()) {
				request.setAttribute("ERROR", "Password can not be empty");
			} else if(confirmPassword.isEmpty()) {
				request.setAttribute("ERROR", "Confirm Password can not be empty");
			}  else if(!isValidUserNameFormat(username)) {
				request.setAttribute("ERROR", "Username can only contain not alphanumerics and underscores(_)");
			} else if(password.length()<8){
				request.setAttribute("ERROR", "Password length is less than 8 characters");
			} else if(!password.equals(confirmPassword)) {
//				System.out.println("Not Equal");
				request.setAttribute("ERROR", "Password and Confirm Password must match");
			} else if(gender.charAt(0)!='m' && gender.charAt('f')!='f') {
				request.setAttribute("ERROR", "Incorrect value for Gender");
			} else if(dbUtil.emailExists(email)) {
				request.setAttribute("ERROR", "Email already exists");
			} else if(dbUtil.usernameExists(username)) {
				request.setAttribute("ERROR", "Username already exists");
			} else {
				User user = new User(fname, lname, email, username, password, gender.charAt(0));
				dbUtil.registerUser(user);
				
				request.setAttribute("MESSAGE","Registration Successful. You can continue to login.");
			}
		}
//		System.out.println(request.getAttribute("ERROR"));
		RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
		rd.forward(request, response);
	}
	
	private void recoverAccount(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String email = request.getParameter("fg_email");
//		System.out.println(getBaseUrl(request));
		if(email == null) {
			//Skip
		} else {
			email = email.trim();
			if(dbUtil.emailExists(email)) {
				String subject = "Codecozy-Password Recovery";
				String content = "Here is your reset password link: \n";
				String link = CloudConfig.getBaseUrl(request)+"/accounts?action=resetPassword&user="
						+ new CryptoUtil().urlSafeEncrypt(CryptoUtil.KEY, email);
				content += link;
				MailUtil.send(email, subject, content); 
			}
		}
		//Adding mode 3 for identifying forgot password tab on accounts.jsp
		request.setAttribute("MODE", 3);
		RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
		rd.forward(request, response);
	}
	
	private void resetPasswordFromLink(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String user = request.getParameter("user");
		String password = request.getParameter("rst_password");
		String confirmPassword = request.getParameter("rst_cpassword");
		if(user == null) {
			request.setAttribute("ERROR", "Orphan request");
		} else if(confirmPassword == null || password == null) {
			request.setAttribute("ERROR", "Input fields can not be null");
		} else {
			user = new CryptoUtil().urlSafeDecrypt(CryptoUtil.KEY, user);
			if(password.length()<8) {
				request.setAttribute("ERROR", "Password must at least be of 8 characters");
			} else if(!password.equals(confirmPassword)) {
				request.setAttribute("ERROR", "Password and Confirm Password must match");
			} else if(!dbUtil.emailExists(user)) {
				request.setAttribute("ERROR", "Unable to detect user");
			} else {
				dbUtil.updatePassword(user, password);
				request.setAttribute("MESSAGE", "Password Reset Successful");
			}
		}
		RequestDispatcher rd = request.getRequestDispatcher("resetpasswordpage.jsp");
		rd.forward(request, response);
	}
	
	private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.getSession().invalidate();
		doGet(request, response);
	}
	
	private void getStoredUserNames(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<String> usernames = dbUtil.getUsernames();
		Gson gson = new Gson();
//		System.out.println("received");
		String json = gson.toJson(usernames);
//		System.out.println(json);
		response.getWriter().write(json);
	}
		
	private boolean isValidUserNameFormat(String username) {
		for(int i=0;i<username.length();++i) {
			if(username.charAt(i)=='_' || 
					username.charAt(i)>='0' && username.charAt(i)<='9' || 
					username.charAt(i)>='a' && username.charAt(i)<='z' || 
					username.charAt(i)>='A' && username.charAt(i)<='Z') {
				//valid
			} else {
				return false;
			}
		}
		return true;
	}

}
