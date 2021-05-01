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
 * Servlet implementation class Bookingroom
 */
@WebServlet("/servlets/book")
public class Bookingroom extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bookingroom() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*String  bookingDate = request.getParameter("bookingDate");
		String  bookingRoom = request.getParameter("bookingRoom");
		String  startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String actualUser = request.getParameter("actualUser");
		String phoneNum = request.getParameter("phoneNum");
		System.out.println(bookingDate + ", " + bookingRoom + "," + startTime + "," + endTime + "," + actualUser + "," +phoneNum );
		
		
		JsonObject result = new JsonObject();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pt = response.getWriter();
		
		String user = (String) request.getSession().getAttribute("yh");
		String userID = (String) request.getSession().getAttribute("userID");

		Record r = new Record(bookingDate,bookingRoom,startTime,endTime,user,"1",phoneNum,userID,actualUser);
		boolean res = r.insertRoom();
		
		if(res) {//succeed.
			result.addProperty("status", 1);
		}else { //fail.
			result.addProperty("status", 0);
		}
		
		pt.write(result.toString());
		System.out.println("booking reuslt: "+result.toString());
		pt.flush();
		pt.close();*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
