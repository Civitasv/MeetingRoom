package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import whu.sres.model.User;

@WebServlet("/servlets/login")
public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public LoginServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		//Get the user name and password 
		String userID = request.getParameter("userName");
		String password = request.getParameter("password");
		//System.out.println("name:" + userID + "password: " + password);
		
		
		JsonObject result = new JsonObject();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pt = response.getWriter();
		
		User u = new User();
		//Validate the user 
		boolean isValidate = u.validate(userID, password);
		//System.out.println("validate: " + isValidate);
		if(!isValidate){//failed to validate
			result.addProperty("status", 0);
		}else{
			String role = u.getNameAndRole(userID).get("ROLE");
			String name = u.getNameAndRole(userID).get("NAME");
			
			HttpSession session = request.getSession();
			session.setAttribute("yh", name);
			session.setAttribute("userID", userID);
			session.setAttribute("role", role);
			
			result.addProperty("status", 1);
			result.addProperty("uid", userID);
			result.addProperty("userName", name);
		}
		
		pt.write(result.toString());
		System.out.println("login reuslt: "+result.toString());
		pt.flush();
		pt.close();
		u.close();
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
