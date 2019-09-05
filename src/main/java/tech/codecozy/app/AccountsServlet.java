package tech.codecozy.app;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/accounts")
public class AccountsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	DBUtil dbUtil;
	
    public AccountsServlet() {
        super();
    }

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		try {
			dbUtil = new DBUtil();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {	
			RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
			rd.forward(request, response);
		} else {
			if(session.getAttribute("user") == null) {
				RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
				rd.forward(request, response);
			} else {
				response.sendRedirect("dashboard");
				return;
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
				case "login":
					performLogin(request,response);
					break;
				case "register":
					performRegister(request,response);
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
			email = email.strip();
			password = password.strip();
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
			email = email.strip();
			password = password.strip();
			confirmPassword = confirmPassword.strip();
			fname = fname.strip();
			lname = lname.strip();
			gender = gender.strip();
			username = username.strip();
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
		RequestDispatcher rd = request.getRequestDispatcher("accounts.jsp");
		rd.forward(request, response);
	}
		
	boolean isValidUserNameFormat(String username) {
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
