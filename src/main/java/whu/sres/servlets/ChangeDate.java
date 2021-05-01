package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
 * Servlet implementation class ChangeDate
 */
@WebServlet("/changeDate")
public class ChangeDate extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeDate() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /* Object selectedDateO = request.getParameter("selectedDate");
        Object changedDayO = request.getParameter("changedDay");

        JsonObject result = new JsonObject();
        response.setCharacterEncoding("UTF-8");
        PrintWriter pt = response.getWriter();
        Record r = new Record();

        Calendar c = Calendar.getInstance();
        Date todayDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Gson gson = new Gson();
        if (selectedDateO == null) {
            if (changedDayO != null) {//Changing date by previous day, next day or today.
                Object currentSelectedDateO = request.getSession().getAttribute("selectedDate");
                if (currentSelectedDateO != null) {
                    Date currentDate = (Date) currentSelectedDateO;
                    c.setTime(currentDate);
                } else {
                    c.setTime(todayDate);
                }

                int changedDay = Integer.parseInt((String) changedDayO);
                if (changedDay != 0) {
                    c.add(Calendar.DATE, changedDay);
                } else {

                    c.setTime(todayDate);
                }

                Date curDate = c.getTime();
                List<Record> rooms = r.getRoomByDate(curDate);
                result.addProperty("currentDate", sdf.format(curDate));
                result.addProperty("dayOfWeek", CommonUtil.getDay(c.get(Calendar.DAY_OF_WEEK)));
                result.addProperty("records", gson.toJson(rooms));

                request.getSession().setAttribute("selectedDate", curDate);

            }

        } else {// change date by selected date.

            String strDate = (String) selectedDateO;
            ArrayList<Record> rooms = r.getRoomByDate(strDate);


            try {
                Date d = sdf.parse(strDate);
                c.setTime(d);
                request.getSession().setAttribute("selectedDate", d);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            result.addProperty("currentDate", strDate);
            result.addProperty("dayOfWeek", CommonUtil.getDay(c.get(Calendar.DAY_OF_WEEK)));
            result.addProperty("records", gson.toJson(rooms));

        }


        r.close();
        pt.write(result.toString());
        System.out.println(" changed reuslt: " + result.toString());
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
