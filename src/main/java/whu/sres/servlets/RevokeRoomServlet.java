package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.JsonObject;
import whu.sres.model.Record;

/**
 * Servlet implementation class RevokeRoomServlet
 */
@WebServlet("/servlets/revokeRoom")
public class RevokeRoomServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RevokeRoomServlet() {
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
        
        if(uidO == null || roleO == null) {        	
        	response.sendRedirect("/index.html");	
        }
        
        PrintWriter pt = response.getWriter();
   
        //Revoke a room.
        System.out.println("going to revoke");
        String rowId = request.getParameter("rowID");
        Record r = new Record();
        		
        if(r.deleteRecord(rowId)) {
        	result.addProperty("revokeState", 1);
        	result.addProperty("rowID", rowId);
        }else {
        	result.addProperty("revokeState", 0);
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
