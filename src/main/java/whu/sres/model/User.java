package whu.sres.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import whu.sres.util.ConnectionUtil;
import whu.sres.util.Encrypt;

public class User {
	private static final Logger logger = LoggerFactory.getLogger(User.class);
	private String id;
	private String password;
	private String phone;
	private String role;
	private String name;
	
	private Connection conn;
	
	
	public User(String id, String password, String phone, String role, String name) {
		super();
		this.id = id;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.name = name;
	}
	
	public User() {
		this.conn = ConnectionUtil.getInstance().getConnection();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 判断密码和账户名是否正确
	 * @param userID
	 * @param userPassword  密码
	 * @return
	 */
	public boolean validate(String userID,String userPassword){
		if(userID != null && userPassword != null){
			String encryptPWD = Encrypt.getMD5Str(userPassword);
			String sql ="SELECT * FROM USERS WHERE U_ID = " + userID + " AND U_PASSWORD = '"+encryptPWD+"'";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					return true;
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			
		}	
		
		return false;
	}
	
	/**
	 * 根据工号来获取名称和角色
	 * @param userID 工号
	 * @return HashMap<"NAME|ROLE",value>
	 */
	public HashMap<String, String> getNameAndRole(String userID){
		HashMap<String, String> result = new HashMap<String, String>();
		if(userID != null){		
			String sql ="SELECT U_NAME,U_ROLE FROM USERS WHERE U_ID = " + userID ;
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				if(rs.next()){
					result.put("NAME", rs.getString("U_NAME"));
					result.put("ROLE", rs.getString("U_ROLE"));
				}
				rs.close();
				ps.close();
			} catch (SQLException e) {
				logger.error(e.getMessage());
			}
			
		}	
		return result;
	}
	
	/**
	 * 插入用户
	 * @return
	 */
	public boolean userInsert( ){
		//验证是否存在改用户
		String sqlDetect = "SELECT * FROM USERS WHERE U_ID = '" + this.id + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sqlDetect);
			if(rs.next())
				return false;
		} catch (SQLException e1) {
			logger.error(e1.getMessage());
		}
		
		
		String sql = "INSERT INTO USERS (U_ID, U_NAME,PHONE_NUM,U_PASSWORD,U_ROLE) " +
		"VALUES (?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.id);
			ps.setString(2, this.name);
			ps.setString(3, this.phone);
			ps.setString(4, Encrypt.getMD5Str(this.password));
			ps.setString(5, this.role);
			int i =ps.executeUpdate();
			if(ps != null)
				ps.close();
			if(i != 0)
				return true;
			else 
				return false;

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Get infomation of a user by its user ID.
	 * @param uid
	 * @return
	 */
	public ArrayList<User> getUserByUid(String uid){
		return getUserByField("U_ID",uid.trim());
	}
	
	/**
	 * 获取满足某种需求的用户
	 * @param field
	 * @param id
	 * @return
	 */
	public ArrayList<User> getUserByField(String field, String id){
		ArrayList<User> users = new ArrayList<User>();
		
		String sql = "SELECT * FROM USERS WHERE "+field+  "=" +id+"";
		//System.out.println("sql: " + sql);
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				User u = new User(Integer.toString(rs.getInt("U_ID")), rs.getString("U_PASSWORD"),rs.getString("PHONE_NUM"),rs.getString("U_ROLE"), rs.getString("U_NAME"));
				users.add(u);
			}
			if(ps != null)
				ps.close();
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return users;
	}
	
	
	/**
	 * 获取所有需求的用户
	 * @return
	 */
	public ArrayList<User> getUserAll(){
		ArrayList<User> users = new ArrayList<User>();
	
		String sql = "SELECT * FROM USERS";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				User u = new User(Integer.toString(rs.getInt("ID")), rs.getString("PASSWORD"),rs.getString("TEL"),rs.getString("ROLE"), rs.getString("NAME"));
				users.add(u);
			}
			if(ps != null)
				ps.close();
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		return users;
	}
	
	public boolean deleteUserByID(String id){
		
		String sql = "DELETE FROM USERS WHERE U_ID = " +id;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = ps.executeUpdate();
			if(i != 0)
				return true;
			else 
				return false;
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	
	}
	
	/**
	 * Updates phone number.
	 * @param phoneNum
	 * @return
	 */
	public boolean updatePhone(String phoneNum, String uid) {
		String sql = "UPDATE USERS SET PHONE_NUM=? WHERE U_ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, phoneNum);
			ps.setString(2, uid);
			int i = ps.executeUpdate();
			if(i != 0)
				return true;
			else 
				return false;			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Update phone number and password.
	 * @param phoneNum
	 * @param pwd
	 * @param uid
	 * @return
	 */
	public boolean updatePhoneAndPwd(String phoneNum,String pwd, String uid) {
		String sql = "UPDATE USERS SET PHONE_NUM=?,U_PASSWORD=? WHERE U_ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, phoneNum);
			ps.setString(2, Encrypt.getMD5Str(pwd));
			ps.setString(3, uid);
			int i = ps.executeUpdate();
			if(i != 0)
				return true;
			else 
				return false;			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	}
	
	public boolean userUpdate(){
		
		String sql = "UPDATE USERS SET U_NAME=?,PHONE_NUM=?,U_PASSWORD=?,U_ROLE=? WHERE U_ID = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, this.name);
			ps.setString(2, this.phone);
			ps.setString(3, Encrypt.getMD5Str(this.password));
			ps.setString(4, this.role);
			ps.setString(5, this.id);
			int i = ps.executeUpdate();
			if(i != 0)
				return true;
			else 
				return false;			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			return false;
		}
	
	}
	
	
	public void close() {
		/*
		if(conn != null) {
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
