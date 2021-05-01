import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ConnectionPool {

	

	private static Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
	
	
	private  Connection conn = null;


	private ConnectionPool() {}
	

	public  Connection getConnection(){
		
		if(conn != null)
			return conn;
		try{
			String dataSource = this.getClass().getClassLoader().getResource("data.db").getPath();;
			Class.forName("org.sqlite.JDBC");
			conn=DriverManager.getConnection("jdbc:sqlite:"+dataSource);
			return conn;
		}catch( SQLException | ClassNotFoundException e){
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	public boolean isNumber(String str){
		   Pattern pat=Pattern.compile("[0-9]*");
		   Matcher isNum=pat.matcher(str);
		   if(!isNum.matches())
			   return false;
		   else return true;
	   }

}
