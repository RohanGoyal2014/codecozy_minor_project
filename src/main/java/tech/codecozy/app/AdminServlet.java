package tech.codecozy.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
				request.setAttribute("IS_SUPERADMIN", dbUtil.isSuperAdmin(email));
				request.setAttribute("CONTESTS", filterUpcomingContests(dbUtil.fetchContests()));
				RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
				rd.forward(request, response);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null || session.getAttribute("user")== null) {
			doGet(request, response);
			return;
		}
		
		String command = request.getParameter("command");
		
		if(command == null) {
			doGet(request, response);
		} else {
			try {
				switch(command) {
				case "add_contest":
					addContest(request, response);
					break;
				case "remove_contest":
					deleteContest(request, response);
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
			contestStart = contestStart.replace("T", " ");
			contestStart+=":00";
			contestEnd = contestEnd.replace("T", " ");
			contestEnd+=":00";
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long startTimestamp = format.parse(contestStart).getTime();
			long endTimestamp = format.parse(contestEnd).getTime();
			long minLimit = 15L*60L;
			long curr = System.currentTimeMillis();
			if(startTimestamp<curr) {
				request.setAttribute("ERROR", "Invalid Start Date");
			} else if(endTimestamp-startTimestamp<minLimit) {
				request.setAttribute("ERROR","Minimum contest time must be 15mins");
			} else {
			
				
				ArrayList<UploadedIoFile> problem1Inputs = new ArrayList<>();
				ArrayList<UploadedIoFile> problem1Outputs = new ArrayList<>();
				ArrayList<UploadedIoFile> problem2Inputs = new ArrayList<>();
				ArrayList<UploadedIoFile> problem2Outputs = new ArrayList<>();
				
				HashSet<String> inputFileFormats = getAllowedFileFormat("input");
			    HashSet<String> outputFileFormats = getAllowedFileFormat("output");
				
				List<Part> fileParts = request.getParts().stream().filter(part -> "input1".equals(part.getName())).collect(Collectors.toList());
			    for (Part filePart : fileParts) {
			        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); 
			        InputStream fileContent = filePart.getInputStream();
			        problem1Inputs.add(new UploadedIoFile(fileName,fileContent));
			    }
			    
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
			    } else if(!isValidFormat(problem1Inputs,inputFileFormats)){
			    	request.setAttribute("ERROR", "Incorrect format files provided for problem 1 inputs");
			    } else if(!isValidFormat(problem1Outputs,outputFileFormats)){
			    	request.setAttribute("ERROR", "Incorrect format files provided for problem 1 outputs");
			    } else if(!isValidFormat(problem2Inputs,inputFileFormats)){
			    	request.setAttribute("ERROR", "Incorrect format files provided for problem 2 inputs");
			    } else if(!isValidFormat(problem1Outputs,outputFileFormats)){
			    	request.setAttribute("ERROR", "Incorrect format files provided for problem 2 outputs");
			    } else {
			    	//Validation Success
			    	
			    	long id = dbUtil.createContestAndGetID(new Contest(-1,contestName, startTimestamp, endTimestamp, null, null, editorialLink));
			    	
			    	if(id == -1) {
			    		request.setAttribute("ERROR", "Something went wrong");
			    	} else {
 			    	
			    		long pb1ID = dbUtil.addProblemToContest(new Problem(problem1Name,problem1Link,id));
				    	long pb2ID = dbUtil.addProblemToContest(new Problem(problem2Name,problem2Link,id));
				    	
				    	String UPLOAD_DIR = "uploadedFiles"+File.separator+"contests"+ File.separator +String.valueOf(id);
						String applicationPath = getServletContext().getRealPath("");
				        String uploadPath1 = applicationPath + UPLOAD_DIR+"-1";
				        String uploadPath2 = applicationPath + UPLOAD_DIR+"-2";
		//				System.out.println("Amm:"+uploadPath);
						File fileUploadDirectory1 = new File(uploadPath1);
						File fileUploadDirectory2 = new File(uploadPath2);
						if(!fileUploadDirectory1.exists()) {
							fileUploadDirectory1.mkdirs();
						}
						if(!fileUploadDirectory2.exists()) {
							fileUploadDirectory2.mkdirs();
						}
						TestCase[] tc1 = new TestCase[5];
						TestCase[] tc2 = new TestCase[5];
						int tcNumber = 0;
				    	for(UploadedIoFile f: problem1Inputs) {
				    		String path=uploadPath1+File.separator+f.getName();
				    		tc1[tcNumber++] = new TestCase(pb1ID, path, null);
				    		copyInputStreamToFile(f.getContent(),new File(path));
				    		if(new File(path).exists()) {
//				    			System.out.println("Yes");
				    		}
				    	}
				    	tcNumber = 0;
				    	for(UploadedIoFile f: problem1Outputs) {
				    		String path = uploadPath1+File.separator+f.getName();
				    		tc1[tcNumber++].setOutputPath(path);
				    		copyInputStreamToFile(f.getContent(),new File(path));
				    	}
				    	tcNumber = 0;
				    	for(UploadedIoFile f: problem2Inputs) {
				    		String path = uploadPath2+File.separator+f.getName();
				    		tc2[tcNumber++] = new TestCase(pb2ID, path, null); 
				    		copyInputStreamToFile(f.getContent(),new File(path));
				    	}
				    	tcNumber = 0;
				    	for(UploadedIoFile f: problem2Outputs) {
				    		String path = uploadPath2+File.separator+f.getName();
				    		tc2[tcNumber++].setOutputPath(path);
				    		copyInputStreamToFile(f.getContent(),new File(path));
				    	}
				    	tcNumber = 0;
				    	while(tcNumber < 5) {
				    		dbUtil.addTestCase(tc1[tcNumber]);
				    		dbUtil.addTestCase(tc2[tcNumber]);
				    		++tcNumber;
				    	}
				    	response.sendRedirect("dashboard");
				    	return;
			    	}
			    	
				}
			}
		}
		//Mode 1 for first tab
		request.setAttribute("MODE", 1);
		
		User user = (User)request.getSession().getAttribute("user");
		request.setAttribute("IS_SUPERADMIN", dbUtil.isSuperAdmin(user.getEmail()));
		
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}
	
	private void deleteContest(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ct_id_str = request.getParameter("ct");
		if(ct_id_str == null) {
			request.setAttribute("ERROR", "Invalid Request");
		} else {
			long ct_id=-1;
			try {
				ct_id = Long.parseLong(ct_id_str);
			} catch(Exception e) {
				e.printStackTrace();
				request.setAttribute("ct_id", "Invalid Request");
			}
			if(ct_id != -1) {
				dbUtil.removeContest(ct_id);
				response.sendRedirect("dashboard");
				return;
			}
		}
		
		//Mode 2 for second tab
		request.setAttribute("MODE", 2);
		
		User user = (User)request.getSession().getAttribute("user");
		request.setAttribute("IS_SUPERADMIN", dbUtil.isSuperAdmin(user.getEmail()));
		
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
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
	
	private static void copyInputStreamToFile(InputStream inputStream, File file) 
			throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(file)) {

		    int read;
		    byte[] bytes = new byte[1024];
	
		    while ((read = inputStream.read(bytes)) != -1) {
		    	outputStream.write(bytes, 0, read);
		    }

		}

	}
	
	private ArrayList<Contest> filterUpcomingContests(ArrayList<Contest> contests) {
		ArrayList<Contest> filtered = new ArrayList<>();
		
		long currTimestamp = System.currentTimeMillis();
		
		for(Contest c: contests) {
			if(c.getStart()>currTimestamp) {
				filtered.add(c);
			}
		}
		
		return filtered;
	}
	


}
