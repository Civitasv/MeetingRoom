package whu.sres.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class ConnectionUtil {

	private final static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);

	private static ConnectionUtil connUtil;
	private Connection conn;

	private ConnectionUtil() {
	}

	// Register the driver only once.
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public static ConnectionUtil getInstance() {
		if (connUtil == null) {
			synchronized (ConnectionUtil.class) {
				if (connUtil == null) {
					connUtil = new ConnectionUtil();
				}
			}
		}
		return connUtil;
	}

	// Get the connection.
	public Connection getConnection() {

		if (conn == null) {

			String dataSource = ConnectionUtil.class.getResource("/data.db").getPath();

			// TODO: Comment out the blow code when in production.
			//dataSource = "/home/tianqin/projects/meetingRoom/workspace/MeetingRoom/src/resources/data.db";
			System.out.println("data source: " + dataSource);
			try {
				conn = DriverManager.getConnection("jdbc:sqlite:" + dataSource);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return conn;

	}

	public void close() {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
