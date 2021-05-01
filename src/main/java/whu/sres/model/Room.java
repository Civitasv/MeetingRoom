package whu.sres.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import whu.sres.util.CommonUtil;
import whu.sres.util.ConnectionUtil;
import whu.sres.util.SystemDate;

public class Room {
	private  Date date;
	private  String startTime;
	private String endTime;
	private String  room;
	private String user;
	private String state;
	private String phoneNum;
	private String userID;
	private Integer rowId;
	
	private String actualUser;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


	private Connection conn;
	private final static Logger logger = LoggerFactory.getLogger(Room.class);


	public Room() {
		this.conn = ConnectionUtil.getInstance().getConnection();
	}


	public Room(String date, String room, String startTime,String endTime,
			String user, String state, String phoneNum,String userID,String actualUser) {
		if(this.conn == null) {
			this.conn = ConnectionUtil.getInstance().getConnection();
		}
		
		try {
			this.date = sdf.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		this.room = room;
		this.startTime = startTime;
		this.endTime  = endTime;
		
		this.user = user;
		this.state = state;
		this.phoneNum = phoneNum;
		this.userID = userID;
		this.actualUser = actualUser;
	}

	//Room(date,room,start_time,end_time,user,actural_user,phoneNum,state,rid);
	private Room(Date date, String room, String start_time, String end_time, String user, String actual_user,
			String phoneNum, String state, int rid) {
		
		this.date =date;
		this.room = room;
		this.startTime = start_time;
		this.endTime = end_time;
		this.user = user;
		this.actualUser = actual_user;
		this.phoneNum = phoneNum;
		this.state = state;
		this.rowId = rid;
	}
	
	//Room(date,room,start_time,end_time,userID,actural_user,phoneNum,state,rid);
		private Room(String date, String room, String start_time, String end_time, String userID, String actual_user,
				String phoneNum, String state, int rid) {
			
			try {
				this.date =sdf.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.room = room;
			this.startTime = start_time;
			this.endTime = end_time;
			this.userID = userID;
			this.actualUser = actual_user;
			this.phoneNum = phoneNum;
			this.state = state;
			this.rowId = rid;
		}
	
	//User(room,start_time,end_time, user, actualUser, phoneNum)
	private Room(String room, String start_time, String end_time, String user, String actualUser, String phoneNum) {
		if(this.conn == null) {
			this.conn = ConnectionUtil.getInstance().getConnection();
		}
		this.room = room;
		this.startTime = start_time;
		this.endTime = end_time;
		this.user  = user;
		this.actualUser = actualUser;
		this.phoneNum = phoneNum;
	}
	
	
	
	
	
	/**
	 * Get all the data in a specific day.
	 * @param d
	 * @return
	 */
   public ArrayList<Room> getRoomByDate(Date d) {
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   String date = sdf.format(d);
	   return getRoomByDate(date);
   }
	
	/**
	 * Get all the data in a specific day.
	 * @param date
	 * @return
	 */
   public ArrayList<Room> getRoomByDate(String date) {
	   
	   ArrayList<Room> result = new ArrayList<Room>();
	   String sql = "SELECT * FROM BOOKED_RECORDS WHERE DATE_USE=?";
	  
		PreparedStatement ps = null;
		try {
			
			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String start_time = rs.getString("START_TIME");
				String end_time = rs.getString("END_TIME");
				String user = rs.getString("ROOM_USER");
				String phoneNum = rs.getString("PHONENUM");
				String actualUser = rs.getString("REAL_USER");
				String room = rs.getString("ROOM");
				Room r = new Room(room,start_time,end_time, user, actualUser, phoneNum);
				result.add(r);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
	   return result;
   }
	
	public String getContent(String date, String time, String room ){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		Date d=null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			logger.error(e.getMessage());
		}
		
		return getContent(d, time, room);
		
	}
	

	/**
	 * 	
	 * @param date  Date 日期
	 * @param time  时间段  上午，下午，晚上
	 * @param room  房间号
	 * @return  返回申请人名称，时间段，理由，状态值
	 */
	public String getContent(Date date, String time, String room ){

		StringBuilder  str = new StringBuilder() ;
		
		String sql = "SELECT ROOM_USER,STATE,PHONENUM,REAL_USER FROM BOOKED_RECORDS WHERE DATE_USE = ? AND TIME_USE = ? AND ROOM = ? AND STATE = 'Y'";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			java.sql.Date d = new java.sql.Date(date.getTime());
			ps.setDate(1, d);
			ps.setString(2, time);		
			ps.setString(3, room);

			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				str.append(rs.getString("ROOM_USER"));
				str.append("<br>");
				str.append(rs.getString("PHONENUM"));
				str.append("(");
				str.append(rs.getString("REAL_USER"));
				str.append(")");
				str.append("<br>");
				String state = rs.getString("STATE");
				if(state.equals("Y")){
					state = "已批准";
				}else if(state.equals("D")){
					state = "待审核";
				}else{
					state = "审核未通过";
				}
				str.append(state);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return str.toString();
	}

	/**
	 * 检测房间是否可用	 
	 * @return  可以用则返回true，否则放回false
	 */
	public  boolean isAvailable(){
		String sql = "SELECT START_TIME,END_TIME FROM BOOKED_RECORDS WHERE DATE_USE=? AND ROOM= ? ";
		TreeSet<Integer> set = new TreeSet<Integer>();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, sdf.format(this.date));
			ps.setString(2, this.room);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String start_time = rs.getString("START_TIME");
				String end_time = rs.getString("END_TIME");
				set.addAll(CommonUtil.period2seq(start_time, end_time));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		TreeSet<Integer> inSet = CommonUtil.period2seq(this.startTime, this.endTime);
		for(Integer ele:inSet) {
			if(set.contains(ele)) {
				return false;
			}
		}
		
		return true;
		
	}
	
	
	/**
	 * 检测房间是否可用	 
	 * @return  可以用则返回true，否则放回false
	 */
	public  TreeSet<Integer> getUnavailableRoomsByDateRoom(String date, String room){
		String sql = "SELECT START_TIME,END_TIME FROM BOOKED_RECORDS WHERE DATE_USE=? AND ROOM= ? ";
		TreeSet<Integer> set = new TreeSet<Integer>();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, date);
			ps.setString(2, room);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String start_time = rs.getString("START_TIME");
				String end_time = rs.getString("END_TIME");
				set.addAll(CommonUtil.period2seq(start_time, end_time));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return set;
		
	}
	
	

	/**
	 * 插入记录到数据库中
	 * @return 成功返回true,否则返回false
	 */
	public boolean insertRoom(){
		if(!this.isAvailable()) {
			return false;
		}
		String sql = "INSERT INTO BOOKED_RECORDS (DATE_USE,ROOM,START_TIME,END_TIME,ROOM_USER,PHONENUM,USER_ID,REAL_USER) " +
		"VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement ps=null;
		try {
			ps = conn.prepareStatement(sql);
			
			ps.setString(1,  sdf.format(this.date));
			ps.setString(2, this.room);
			ps.setString(3, this.startTime);
			ps.setString(4, this.endTime);
			ps.setString(5, this.user);
			ps.setString(6, this.phoneNum);
			ps.setString(7, this.userID);
			ps.setString(8, this.actualUser);
			
			ps.execute();
			System.out.println(ps.getUpdateCount());
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			if(ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
	
	
	
	/**
	 * 根据申请者ID，提取相关房间信息
	 * @param uid
	 * @return
	 */
	public ArrayList<Room> getRoomByRoomUser(String uid){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * FROM BOOKED_RECORDS WHERE USER_ID = ? ORDER BY DATE_USE DESC";
		
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uid);			
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				Date d = sdf.parse(rs.getString("DATE_USE"));
			
				String start_time = rs.getString("START_TIME");
				String end_time = rs.getString("END_TIME");
				String room = rs.getString("ROOM");
				String user = rs.getString("ROOM_USER");
				String actural_user = rs.getString("REAL_USER");
				int rid = rs.getInt("ROW_ID");
				String state = rs.getString("STATE");
				String phoneNum = rs.getString("PHONENUM");
				
				Room r = new Room(d,room,start_time,end_time,user,actural_user,phoneNum,state,rid);
				rooms.add(r);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}
	
	/**
	 * 获取可以撤销的房间
	 * @param uid
	 * @return
	 */
	
	public  ArrayList<Room> getRoomByRoomUserCanCancel(String uid){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * FROM BOOKED_RECORDS WHERE USER_ID = ? ORDER BY DATE_USE DESC";
		
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, uid);			
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				String room = rs.getString("ROOM");
				String d = rs.getString("DATE_USE");
				String startTime = rs.getString("START_TIME");
				String endTime = rs.getString("END_TIME");
				String phone = rs.getString("PHONENUM");
				String actualUser = rs.getString("REAL_USER");
				int id = rs.getInt("ROW_ID");
				String state = rs.getString("STATE");
				//Room(date,room,start_time,end_time,userID,actural_user,phoneNum,state,rid);
				if(!SystemDate.isEarlierThanNow(d,startTime)){
					Room r = new Room(d,room,startTime,endTime,null,actualUser,phone,state,id);
					rooms.add(r);
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return rooms;
	}
	
	
	public boolean setRecordState(String state,String id){
		String sql = "UPDATE  BOOKED_RECORDS  SET STATE = ? WHERE ID = ?";
		
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, state);
			ps.setInt(2, Integer.parseInt(id));
			int i = ps.executeUpdate();
			if(i != 0){
				return true;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		
		return false;
	}

	/**
	 * Delete a record according to its record id.
	 * @param id
	 * @return
	 */
	public  boolean deleteRecord(String id){
		String sql = "DELETE FROM BOOKED_RECORDS  WHERE ROW_ID = ?";
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);		
			ps.setInt(1, Integer.parseInt(id));
			int i = ps.executeUpdate();
			if(i != 0){
				return true;
			}

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public Integer getRowId() {
		return rowId;
	}


	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}


	public String getActualUser() {
		return actualUser;
	}


	public void setActualUser(String actualUser) {
		this.actualUser = actualUser;
	}
	
	
	public String getDateStr() {
		return sdf.format(this.date);
	}
	

	public void close() {
		/*
		if (this.conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}
	
	
	



}
