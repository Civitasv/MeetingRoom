package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import whu.sres.model.Record;

/**
 * Servlet implementation class UserManageServlet
 */
@WebServlet("/servlets/UserManage")
public class UserManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserManageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*JsonObject result = new JsonObject();
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
        	int serviceNum = Integer.parseInt(service.trim());
        	if(serviceNum == 0) {
        		//init.
        		result.addProperty("uid", uid);
                result.addProperty("role", role);
        	}else if(serviceNum == 1 || serviceNum == 2){
        		Record r = new Record();
        		ArrayList<Record>  rooms= r.getRoomByRoomUserCanCancel(uid);//search the rooms that can be revoked.
        		if(serviceNum == 2) {
        			//history bookings.
        			rooms= r.getRoomByRoomUser(uid);
        		}
        		
        		JsonArray arr = new JsonArray();
        		for(Record rm:rooms) {
        			JsonObject o = new JsonObject();
        			o.addProperty("date", rm.getDateStr());
        			o.addProperty("room", rm.getRoom());
        			o.addProperty("startTime", rm.getStartTime());
        			o.addProperty("endTime", rm.getEndTime());
        			o.addProperty("actualUser", rm.getActualUser());
        			o.addProperty("phone", rm.getPhoneNum());
        			if(serviceNum == 1) {
        				o.addProperty("rowId", rm.getRowId());
        			}
        			arr.add(o);
        		}
        		result.add("records", arr);
        		        	
        	
        		
        	}else if(serviceNum ==3) {
        		//Revoke a room.
        		
        		String rowId = request.getParameter("rowID");
        		
        		
        		System.out.println("going to revoke" +rowId);
        		
        		Record r = new Record();
        		
        		if(r.deleteRecord(rowId)) {
        			result.addProperty("revokeState", 1);
        			result.addProperty("rowID", rowId);
        		}else {
        			result.addProperty("revokeState", 0);
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
		pt.close();
*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
