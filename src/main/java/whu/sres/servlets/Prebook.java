package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import whu.sres.model.Record;
import whu.sres.util.CommonUtil;

/**
 * Servlet implementation class Prebook
 */
@WebServlet("/servlets/prebook")
public class Prebook extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Prebook() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*JsonObject result = new JsonObject();
		response.setCharacterEncoding("UTF-8");
		PrintWriter pt = response.getWriter();
		Record r = new Record();

		Calendar c = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String room = request.getParameter("bookingRoom");
		String bookingDate = request.getParameter("bookingDate");

		String roomStime = request.getParameter("roomStime");
		Gson gson = new Gson();

		if (roomStime != null) {
			room = roomStime.substring(0, 3);
			String startTime = CommonUtil.index2StartTime(roomStime.substring(3, 5));
			String endTime = CommonUtil.index2StartTime(Integer.parseInt(roomStime.substring(3, 5))+4);

			Object selectedDateO = request.getSession().getAttribute("selectedDate");
			if (selectedDateO == null) {
				// Booking today's meeting room.
				Date todayDate = new Date();
				c.setTime(todayDate);

			} else {
				Date curDate = (Date) selectedDateO;
				c.setTime(curDate);
			}

			result.addProperty("bookingDate", sdf.format(c.getTime()));
			result.addProperty("bookingRoom", room);
			result.addProperty("startTime", startTime);
			result.addProperty("endTime", endTime);
			result.addProperty("unaRooms", gson.toJson(r.getUnavailableRoomsByDateRoom(sdf.format(c.getTime()), room)));

		}else if(room != null && bookingDate != null) {
			result.addProperty("unaRooms", gson.toJson(r.getUnavailableRoomsByDateRoom(bookingDate, room)));
		}
		

		r.close();
		pt.write(result.toString());
		System.out.println(" prebook reuslt: " + result.toString());
		pt.flush();
		pt.close();*/

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
