package tech.codecozy.app;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.google.gson.JsonObject;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

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
		HttpSession session = request.getSession(false);
		if(session == null || session.getAttribute("user") == null) {
			JSONObject json = new JSONObject("{error:Invalid Session}");
			response.getWriter().write(json.toString());
			return;
		} else {
			User user = (User) session.getAttribute("user");
			String email = user.getEmail();
			if(!dbUtil.emailExists(email)) {
				JSONObject json = new JSONObject("{error:Invalid Session}");
				response.getWriter().write(json.toString());
				return;
			}
			String command = request.getParameter("command");
			if(command == null) {
				JSONObject json = new JSONObject("{error:Invalid Request}");
				response.getWriter().write(json.toString());
				return;
			}
			try {
				switch(command) {
				case "compile_code":
					compileCode(request, response);
					break;
				default:
					doGet(request, response);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void compileCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sourceCode = request.getParameter("sourceCode");
		String stdin = request.getParameter("stdin");
		String language = request.getParameter("lang");
		if(sourceCode == null || stdin == null || language == null) {
			JSONObject json = new JSONObject("{error:Invalid Request}");
			response.getWriter().write(json.toString());
			return;
		}
//		System.out.println(sourceCode);
//		System.out.println(stdin);
//		System.out.println(language);
		
		if(language.equals("c_c++")) language = "Cpp14";
		else if(language.equals("java")) language="Java";
		else language="Python3";
		String apiResponse = Unirest.post("https://ide.geeksforgeeks.org/main.php")
	       .field("lang", language)
	       .field("code", sourceCode)
	       .field("input", stdin)
	       .field("save", "false")
	       .asString().getBody();
		
		Unirest.shutDown();
		
		JSONObject json = new JSONObject(apiResponse);
		response.getWriter().write(json.toString());
	}

}
