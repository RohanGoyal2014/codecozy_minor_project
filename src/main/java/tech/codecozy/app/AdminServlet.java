package tech.codecozy.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/admin")
@MultipartConfig
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private DBUtil dbUtil;
	
    public AdminServlet() {
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
			if(!dbUtil.emailExists(email) || !dbUtil.isAdmin(email)) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
				rd.forward(request, response);
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
				case "add_contest":
					addContest(request, response);
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
	
	private void addContest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contestName = request.getParameter("ct_name");
		String contestStart = request.getParameter("ct_start");
		String contestEnd = request.getParameter("ct_end");
		String editorialLink = request.getParameter("ct_editorial");
		String problem1Name = request.getParameter("pb_name_1");
		String problem1Link = request.getParameter("pb_link_1");
		String problem2Name = request.getParameter("pb_name_2");
		String problem2Link = request.getParameter("pb_link_2");
		
		if(contestName == null || contestStart == null || contestEnd == null || editorialLink == null ||
				problem1Name == null || problem1Link == null || problem2Name == null || problem2Link == null) {
			request.setAttribute("ERROR", "Input fields can not be null");
		} else {
			
			ArrayList<UploadedIoFile> problem1Inputs = new ArrayList<>();
			ArrayList<UploadedIoFile> problem1Outputs = new ArrayList<>();
			ArrayList<UploadedIoFile> problem2Inputs = new ArrayList<>();
			ArrayList<UploadedIoFile> problem2Outputs = new ArrayList<>();
			
			List<Part> fileParts = request.getParts().stream().filter(part -> "input1".equals(part.getName())).collect(Collectors.toList());
		    for (Part filePart : fileParts) {
		        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		        InputStream fileContent = filePart.getInputStream();
		        problem1Inputs.add(new UploadedIoFile(fileName,fileContent));
		    }
		    
		    HashSet<String> inputFileFormats = getAllowedFileFormat("input");
		    HashSet<String> outputFileFormats = getAllowedFileFormat("output");
		    
		    fileParts.clear();
		    fileParts = request.getParts().stream().filter(part -> "output1".equals(part.getName())).collect(Collectors.toList());
		    for (Part filePart : fileParts) {
		        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		        InputStream fileContent = filePart.getInputStream();
		        problem1Outputs.add(new UploadedIoFile(fileName,fileContent));
		    }
		    
		    fileParts.clear();
		    fileParts = request.getParts().stream().filter(part -> "input2".equals(part.getName())).collect(Collectors.toList());
		    for (Part filePart : fileParts) {
		        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		        InputStream fileContent = filePart.getInputStream();
		        problem2Inputs.add(new UploadedIoFile(fileName,fileContent));
		    }
		    
		    fileParts.clear();
		    fileParts = request.getParts().stream().filter(part -> "output2".equals(part.getName())).collect(Collectors.toList());
		    for (Part filePart : fileParts) {
		        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
		        InputStream fileContent = filePart.getInputStream();
		        problem2Outputs.add(new UploadedIoFile(fileName,fileContent));
		    }
		    
		    if(problem1Inputs.size()<5) {
		    	request.setAttribute("ERROR", "Incorrect number of inputs for Problem 1");
		    } else if(problem1Outputs.size()<5) {
		    	request.setAttribute("ERROR", "Incorrect number of outputs for Problem 1");
		    } else if(problem2Inputs.size()<5) {
		    	request.setAttribute("ERROR", "Incorrect number of inputs for Problem 2");
		    } else if(problem2Outputs.size()<5) {
		    	request.setAttribute("ERROR", "Incorrect number of outputs for Problem 2");
		    } else if(isValidFormat(problem1Inputs,inputFileFormats)){
		    	request.setAttribute("ERROR", "Incorrect format files provided in inputs for problem 1 inputs");
		    } else if(isValidFormat(problem1Outputs,outputFileFormats)){
		    	request.setAttribute("ERROR", "Incorrect format files provided in inputs for problem 1 outputs");
		    } else if(isValidFormat(problem1Outputs,inputFileFormats)){
		    	request.setAttribute("ERROR", "Incorrect format files provided in inputs for problem 2 inputs");
		    } else if(isValidFormat(problem1Outputs,outputFileFormats)){
		    	request.setAttribute("ERROR", "Incorrect format files provided in inputs for problem 2 outputs");
		    } else {
		    	//Validation Success
		    	
		    }
		}
		//Mode 1 for first tab
		request.setAttribute("MODE", 1);
		RequestDispatcher rd = request.getRequestDispatcher("admin");
		rd.forward(request, response);
	}
	
	private boolean isValidFormat(ArrayList<UploadedIoFile> files, HashSet<String> hs) {
		for(int i=0;i<files.size();++i) {
			if(!hs.contains(files.get(i).getName())) {
				return false;
			}
		}
		return true;
	}
	
	private HashSet<String> getAllowedFileFormat(String io) {
		String base=io+"_";
		HashSet<String> format = new HashSet<>();
		for(int i=1;i<=5;++i) {
			format.add(base+String.valueOf(i)+".txt");
		}
		return format;
	}

}
