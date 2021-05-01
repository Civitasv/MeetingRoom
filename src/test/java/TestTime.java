import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import whu.sres.util.ConnectionUtil;


public class TestTime {
	Connection conn;
	Date date;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void init(){
		conn = ConnectionUtil.getInstance().getConnection();
		Calendar c = Calendar.getInstance();
		c.set(2012, 2, 26);
		date = c.getTime();
		System.out.println("date: "+date);
		System.out.println(sdf.format(date));
		
	}
	
	@Test
	public void testTime() throws SQLException{
		String sql= "INSERT INTO TEST_TIME VALUES (?,?)";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, 5);
		java.sql.Date d = new java.sql.Date(date.getTime());
		System.out.println("insert date : "+d);
		ps.setDate(2, d);
		ps.execute();
	}
	
    @Test
    public void testTimeIsExisted() throws SQLException{
    	String sql= "SELECT * FROM TEST_TIME WHERE TIME_TEST = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
	
		java.sql.Date d = new java.sql.Date(date.getTime());
		System.out.println("second date : "+d);
		ps.setDate(1, d);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			System.out.println(rs.getInt("ID"));
		}
    }
    
    @Test
    public void testCalendar(){
    	Calendar c = Calendar.getInstance();
    	System.out.println(c.get(Calendar.DAY_OF_WEEK));
    	
    	
    }
 
	

}
