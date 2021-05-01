import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import whu.sres.util.SystemDate;

public class TestSystemDate {

	@Test
	public void testGetPreviousDay() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse("2013-1-1");
		System.out.println("before date: " + sdf.format(d));
		Date d2 = SystemDate.getPreviousDay(d);
		System.out.println("after date: " + sdf.format(d2));
	}
	
	@Test
	public void testGetDaysBetweenTwoDate() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startD = sdf.parse("2013-05-11");
		Date endD = sdf.parse("2013-05-13");
		System.out.println(SystemDate.getHoursBetweenTwoDate(startD, new Date()));
	}
	
	@Test
	public void testDate() throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startD = sdf.parse("2013-05-12");
		System.out.println(" : " + (startD.getTime()-(new Date()).getTime())/(1000*60*60));
	}
	
	@Test
	public void testIsEarlierThanNow() {
		String date = "2017-09-10";
		String startTime = "21:52";
		System.out.println(SystemDate.isEarlierThanNow(date, startTime));
	}

}
