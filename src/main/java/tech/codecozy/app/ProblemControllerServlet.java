package tech.codecozy.app;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@WebServlet("/problem")
public class ProblemControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	private HashMap<String,String> langMap;
	
    public ProblemControllerServlet() {
        super();
    }
    
	@Override
	public void init() throws ServletException {
		super.init();
		dbUtil = new DBUtil();
		langMap = new HashMap<>();
		langMap.put("c_c++","Cpp14");
		langMap.put("java", "Java");
		langMap.put("python","Python3");
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
				case "submit_code":
					submitCode(request, response);
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
		
		language = langMap.getOrDefault(language, "Cpp14");
		
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
	
	private void submitCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long problemId = Integer.parseInt(request.getParameter("problem"));
		String language = request.getParameter("lang");
		String sourceCode = request.getParameter("sourceCode");
		if(!dbUtil.problemExists(String.valueOf(problemId)) || language==null || sourceCode == null ) {
			JSONObject json = new JSONObject("{error:Invalid Request}");
			response.getWriter().write(json.toString());
			return;
		}
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		String email = user.getEmail();
		
		language = langMap.getOrDefault(language, "Cpp14");
		
		ArrayList<TestCase> tcs = dbUtil.getTestCasesByProblem(problemId);
		ArrayList<String> results = new ArrayList<>();
		
		String link="";
		
		ArrayList<Long> tcIds = new ArrayList<>();
		ArrayList<String> verdicts = new ArrayList<>();
		
		for(TestCase tc:tcs) {
			tcIds.add(tc.getTcID());
		    String input = new String(Files.readAllBytes(Paths.get(tc.getInputPath()))); 
		    String output = new String(Files.readAllBytes(Paths.get(tc.getOutputPath())));	
		    
			String apiResponse = Unirest.post("https://ide.geeksforgeeks.org/main.php")
				       .field("lang", language)
				       .field("code", sourceCode)
				       .field("input", input)
				       .field("save", String.valueOf(link.isEmpty()))
				       .asString().getBody();
			Unirest.shutDown();
			JSONObject json = new JSONObject(apiResponse);
			if(link.isEmpty()) {
				link = "http://ide.geeksforgeeks.org/"+json.getString("id");
			}
//			System.out.println(Arrays.asList(json.getString("output").split("\\n")).toString());
//			response.getWriter().write(json.getString("output"));
//			return;
			
			JSONObject jsonObject = new JSONObject();
			if(json.getString("compResult").equals("S")) {
				jsonObject.append("cmpFailed", false);
				if(json.getString("rntError").isEmpty()) {
					boolean match = compareOutput(json.getString("output"),output);
					if(match) {
						jsonObject.append("result","AC");
						verdicts.add("AC");
					} else {
						jsonObject.append("result","WA");
						verdicts.add("WA");
					}
				} else {
					jsonObject.append("rntError",json.getString("rntError"));
					verdicts.add("RE");
				}
			} else {
				jsonObject.append("cmpFailed", true);
				jsonObject.append("cmpError", json.getString("cmpError"));
				verdicts.add("CE");
			}
		
			results.add(jsonObject.toString());
			
			
		}
		
		dbUtil.makeSubmission(new Submission(
				-1,problemId,System.currentTimeMillis(),link,email,tcIds,verdicts));
//		System.out.println(results.toString());
		response.getWriter().write(new Gson().toJson(results).toString());
				
		
		
	}
	
	private boolean compareOutput(String generated, String output) {
		
		List<String> a1 = new ArrayList<>(Arrays.asList(generated.split("\\n")));
		List<String> a2 = new ArrayList<>(Arrays.asList(output.split("\\n")));
		for(int i=0;i<a1.size();++i) {
			String line = a1.get(i);
			a1.set(i, line.trim());
		}
		for(int i=0;i<a2.size();++i) {
			String line = a2.get(i);
			a2.set(i, line.trim());
		}
		while(!a1.isEmpty() && a1.get(a1.size()-1).isEmpty()) {
			int sz = a1.size()-1;
			a1.remove(sz);
		}
		while(!a2.isEmpty() && a2.get(a2.size()-1).isEmpty()) {
			int sz=a2.size()-1;
			a2.remove(sz);
		}
		if(a1.size()!=a2.size()) return false;
		for(int i=0;i<a1.size();++i) {
			if(!a1.get(i).equals(a2.get(i))) return false;
		}
		return true;
	}

}
