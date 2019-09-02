package tech.codecozy.app;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class HomeControllerServlet
 */
@WebServlet("/")
public class HomeControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeControllerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		   Connection conn = null;
		   
		   try {
			   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			   conn = DriverManager.getConnection(CloudConfig.DB_URL);
			   String schema = conn.getSchema();
			   response.getWriter().write(schema+"<br>");
			   System.out.println("Successfully connected to "+schema);
		   } catch(Exception e) {
//			   System.out.println("Exception");
			   e.printStackTrace();
		   }
		
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
