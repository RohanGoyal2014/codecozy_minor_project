package tech.codecozy.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import kong.unirest.json.JSONObject;

@WebServlet("/leaderboard")
public class LeaderboardControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	
    @Override
	public void init() throws ServletException {
		super.init();
		dbUtil = new DBUtil();
	}

	public LeaderboardControllerServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String id = request.getParameter("id");
		if(session.getAttribute("user") == null) {
			response.sendRedirect("dashbaord");
		}else if(id == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else if(!dbUtil.contestExists(id)){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			request.setAttribute("CONTEST", id);
			RequestDispatcher rd = request.getRequestDispatcher("leaderboard.jsp");
			rd.forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		String command = request.getParameter("command");
		if(session.getAttribute("user") == null || command == null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.append("error", true);
			response.getWriter().write(jsonObject.toString());
		} else {
			try {
				switch(command) {
				case "get_ranklist":
					getRankList(request, response);
					break;
				default:
					doGet(request, response);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void getRankList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contestId = request.getParameter("contest");
		if(contestId == null || !dbUtil.contestExists(contestId)) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.append("error", true);
			response.getWriter().write(jsonObject.toString());
		} else {
			
			ArrayList<Pair<User,Pair<Integer,Long>>> rankList = new ArrayList<>();
			HashMap<String,Pair<Integer,Long>> scoreMap = new HashMap<>();
			
				
			ArrayList<Problem> problems= dbUtil.getProblems(contestId);
			
			for(Problem p:problems) {
				ArrayList<Submission> submissions = dbUtil.getAllSubmissions(p.getId());
				HashMap<String,Pair<Integer,Long>> problemWiseScoreMap = new HashMap<>();
				for(Submission i:submissions) {
					String email = i.getEmail();
					if(!problemWiseScoreMap.containsKey(email)) {
						problemWiseScoreMap.put(email,new Pair<Integer,Long>(0,0L));
					}
					Pair<Integer,Long> curr = problemWiseScoreMap.get(email);
					if(i.getScore() > curr.first) {
						problemWiseScoreMap.replace(email, new Pair<Integer,Long>(i.getScore(), i.getTime()));
					} else if(i.getScore() == curr.first) {
						if(i.getTime()<curr.second) {
							problemWiseScoreMap.replace(email, new Pair<Integer,Long>(i.getScore(), i.getTime()));
						}
					}
				}
				for(String i:problemWiseScoreMap.keySet()) {
					if(!scoreMap.containsKey(i)) {
						scoreMap.put(i, new Pair<Integer,Long>(0,0L));
					}
					Pair<Integer,Long> curr = scoreMap.get(i);
					scoreMap.replace(i, new Pair<Integer,Long>(curr.first+problemWiseScoreMap.get(i).first,curr.second+problemWiseScoreMap.get(i).second));
				}
			}
			for(String i:scoreMap.keySet()) {
				User user = dbUtil.getUserByEmail(i);
				rankList.add(new Pair<User,Pair<Integer,Long>>(user,scoreMap.get(i)));
			}
			
			Collections.sort(rankList, new Comparator<Pair<User,Pair<Integer,Long>>>() {

				@Override
				public int compare(Pair<User, Pair<Integer, Long>> arg0, Pair<User, Pair<Integer, Long>> arg1) {
					
					if(arg0.second.first == arg1.second.first) {
						long val= arg0.second.second-arg1.second.second;
						if(val<0L) return -1;
						else if(val==0L) return 0;
						return 1;
					}
					return arg0.second.first-arg1.second.first;
				}
				
			});
			
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			Pair<String,ArrayList<Pair<User,Pair<Integer,Long>>>> res;
			res = new Pair<String,ArrayList<Pair<User,Pair<Integer,Long>>>>(user.getEmail(),rankList);
			response.getWriter().write(new Gson().toJson(res));
//			response.getWriter().write(new Gson().toJson(user));
		}
	}

}
