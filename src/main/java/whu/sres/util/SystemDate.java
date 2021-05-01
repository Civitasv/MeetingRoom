package whu.sres.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SystemDate {
	Date date=new Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static final Logger logger = LoggerFactory.getLogger(SystemDate.class);
	
	private Date dateuse ;
	private String str;
	private String[] str_day=new String[7];
	
    
	public int getMonth(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH)+1;
	}
	
	

	public static int getTodayWeekOfYear(){
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.WEEK_OF_YEAR);
	}
   
   

   private  int getDaysFromSunday(){
	   Calendar cl = Calendar.getInstance();
	   int dayofweek = cl.get(Calendar.DAY_OF_WEEK);
	   int p = 1-dayofweek;
	   return p;
   }
   

   public String[] getWeekDate(int weekOffset){
	   
	   String[] weekDate = new String[7];
	   Calendar cl = Calendar.getInstance();
	   int day = Calendar.DAY_OF_MONTH;
	   cl.add(day, getDaysFromSunday()+7*weekOffset);	  
	   for(int i=0;i<7;i++){	
		   Date date=cl.getTime();
		   weekDate[i] = formatter.format(date);	
		   cl.add(day, 1);
	   }
	   return weekDate;	   
   }
   
   public String getMonthDay(int j){
	   Calendar cl = Calendar.getInstance();
	   int day = Calendar.DAY_OF_MONTH;
	   for(int i=0;i<30;i++){
		   cl.add(day, 1);
		   dateuse=cl.getTime();
		   str_day[i]=formatter.format(dateuse);
	   }	   
	   return str_day[j];
   }

   public void getCalendar(String[] str)
   {
	   int n=0,i=0;
	   Calendar cl = Calendar.getInstance();
	   int y = cl.get(Calendar.YEAR);
	   int m = cl.get(Calendar.MONTH);
	   int d = cl.get(Calendar.DATE);
	   int dd=Calendar.DAY_OF_MONTH;
	   int year = cl.get(Calendar.YEAR);
	   int month=cl.get(Calendar.MONTH);
	   int wday =cl.get(Calendar.DAY_OF_WEEK);
	   int days = cl.getActualMaximum(Calendar.DATE);

	   int fday =(dd-wday)%7;
	   for(i=0;i<7-fday;i++){
		   str[i]="-";
	   }
	   i=i-1;
	   for(n=1;n<=days;n++,i++){
		   str[i]="\t"+n;
		   if((i+1)%7==0) str[i]=n+"<br>";
		   if(year==y&&month==m&&n==d){
			   str[i]=n+"*";
		   }		   
	   }    

       int kk=(42-i)/7;
       for(;i<42-7*kk;i++){
       	str[i]="-";
	   }
   }
	

	public static Date getPreviousDay(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	

	public static Date getNextDay(Date d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}
	

	public static String getDateBySelect(String select){
		int i = Integer.parseInt(select);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		cal.setTime(date);
		cal.add(Calendar.DATE, i-1);
		return sdf.format(cal.getTime());
		
	}
	

	public static String getTimeBySelect(String select){
		if(select.trim().equals("morning"))
			return "����";
		else if(select.trim().equals("afternoon"))
			return "����";
		else 
			return "����";
	}
	

	public String getToday(){
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}
	

	public static Date getYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	

	public static boolean compareDate(Date date1,Date date2) {
		
		if(date1.getTime()>date2.getTime())
			return true;
		else 
			return false;
		
	}
	

	public static boolean isEarlierThanNow(String date, String startTime) {
		Calendar now = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    startTime = startTime.trim().replace("：", ":");
	    String[] minsec = startTime.split(":");
	    int min = Integer.parseInt(minsec[0]);
	    int sec = Integer.parseInt(minsec[1]);
	    try {
			Date startDate = sdf.parse(date);
			start.setTime(startDate);
			start.set(Calendar.HOUR_OF_DAY, min);
			start.set(Calendar.MINUTE,sec);
			if(now.getTimeInMillis() > start.getTimeInMillis()) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	    
		return false;
	}
	

	public static String getWeekByCalendar(Calendar c){
		String week = null;
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			week = "日";
			break;
		case 2:
			week = "一";
			break;
		case 3:
			week = "二";
			break;
		case 4:
			week = "三";
			break;
		case 5:
			week = "四";
			break;
		case 6:
			week = "五";
			break;
		case 7:
			week = "六";
			break;
			default:

		}
		return week;
	}
	

	public static String formatDate(Date d){
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d);
	}
	

	public static String formatDate2HH_MM_SS(Date d){
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	

	public static Date str2Date(String d){
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(d);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	

	public static int getHoursBetweenTwoDate(Date startDate,Date endDate){
		return (int)(Math.abs(startDate.getTime()-endDate.getTime()))/(1000*60*60);
	}
	

	public static String getStatus(Date startDate,Date endDate){
		if(getHoursBetweenTwoDate(startDate, endDate)>48)
			return "N";
		else
			return "Y";
	}
	

	public static String getAttendanceRepeal(Date d1, Date d2) {
		if (compareDate(d1, d2))
		{
			System.out.println("<a href='./attendanceRepeal'>����</a>");
			return "<a href='./attendanceRepeal'>����</a>";
		}
		else
			return "���ܳ���";

	}
	
}
