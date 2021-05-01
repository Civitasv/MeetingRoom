package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonObject;
import whu.sres.model.User;

/**
 * Servlet implementation class UserInfoServlet
 */
@WebServlet("/servlets/userInfo")
public class UserInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * service = 0; get the personal information.
		 * service = 1; modify the personal information except its password.
		 * service = 2; modify the personal information including its password. 
		 *//*
		JsonObject result = new JsonObject();
		response.setCharacterEncoding("UTF-8");
		
		
		
        Object uidO = request.getSession().getAttribute("userID");
        Object roleO = request.getSession().getAttribute("role");
        String uid = "";
        String role = "";
        if(uidO != null && roleO != null) {
        	uid = (String)uidO;
        	role = (String)roleO;
        }else {
        	response.sendRedirect("/index.html");
        	
        }
        
        PrintWriter pt = response.getWriter();
        String service = request.getParameter("service");
        
        if(service != null) {
        	User u = new User();
        	int serviceNum = Integer.parseInt(service.trim());
        	if(serviceNum == 0) {
        		//init.
        		result.addProperty("uid", uid);
               
                ArrayList<User> users = u.getUserByUid(uid);
                result.addProperty("name", users.get(0).getName());
                result.addProperty("role", users.get(0).getRole());
                result.addProperty("phone", users.get(0).getPhone());
        	}else {
        		//Verify the uid and password.
        		String oldPassword = request.getParameter("oldPassword");
        		String phone = request.getParameter("phone");
        		if(u.validate(uid, oldPassword)) {
        			if(serviceNum ==1) {
            			//update the phone number.
        			
            			if(u.updatePhone(phone, uid)) {
            				result.addProperty("state", 1);
            			}else {
            				result.addProperty("state", 0);
            			}
            		}else if(serviceNum ==2) {
            			//update the phone number and password.
            			String pwd = request.getParameter("newPassword");
            			if(u.updatePhoneAndPwd(phone, pwd, uid)) {
            				result.addProperty("state", 1);
            			}else {
            				result.addProperty("state", 0);
            			}
            		}
        		}else {
        			result.addProperty("state", 2);//old password does not match.
        		}
        		
        		
        	}
        	
        }else{
        	pt.flush();
    		pt.close();
        	return;
        }
        
        
        
		pt.write(result.toString());
		System.out.println("reuslt: " + result.toString());
		pt.flush();
		pt.close();*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
