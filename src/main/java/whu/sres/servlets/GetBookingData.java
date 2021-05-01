package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * Servlet implementation class GetBookingData
 */
@WebServlet("/getBookingData")
public class GetBookingData extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBookingData() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*JsonObject result = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        PrintWriter pt = response.getWriter();


        Record r = new Record();
        Calendar c = Calendar.getInstance();

        Object curDateO = request.getSession().getAttribute("selectedDate");
        if (curDateO != null) {
            c.setTime((Date) curDateO);
        } else {
            Date d = new Date();
            c.setTime(d);
        }

        //TODO: return role as well.
        Object userO = request.getSession().getAttribute("yh");
        if (userO != null) {
            result.addProperty("userName", (String) userO);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Record> rooms = r.getRoomByDate(c.getTime());
        result.addProperty("currentDate", sdf.format(c.getTime()));
        result.addProperty("dayOfWeek", CommonUtil.getDay(c.get(Calendar.DAY_OF_WEEK)));
        Gson gson = new Gson();
        result.addProperty("records", gson.toJson(rooms));


        r.close();
        pt.write(result.toString());
        System.out.println(" reuslt: " + result.toString());
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
