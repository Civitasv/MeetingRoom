package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import whu.sres.util.ConnectionUtil;
import whu.sres.util.Encrypt;

public class PasswordReset extends HttpServlet {

	private static final Logger logger = LoggerFactory.getLogger(PasswordReset.class);

	/**
	 * Constructor of the object.
	 */
	public PasswordReset() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String uname = request.getParameter("user");
		String pspre = Encrypt.getMD5Str(request.getParameter("ps1"));
		String psnew = Encrypt.getMD5Str(request.getParameter("ps2"));
		String psnew1 = Encrypt.getMD5Str(request.getParameter("ps3"));
	
		Connection con = null;
		Statement stmt = null;
		try {				
			con = ConnectionUtil.getInstance().getConnection();
			stmt = con.createStatement();
			String sql = "select PASSWORD from studentmsg where ID=" + uname;
			System.out.println("sql: " + sql);
			ResultSet rs1 = stmt.executeQuery(sql);
			if (!rs1.next()) {
				session.setAttribute("message","�޸�ʧ�ܣ�ԭ���û�������!" );				
			} else {
				String ps = rs1.getString("PASSWORD");
				if (!pspre.equals(ps)) {
					session.setAttribute("message","�޸�ʧ�ܣ�ԭ�򣺳�ʼ���벻�ԣ�" );
				} else {
					String sql1 = "update studentMsg set password='"
							+ psnew + "' where ID="+uname;
					System.out.print(sql1);
					stmt.executeUpdate(sql1);
					session.setAttribute("message","��ϲ�����޸ĳɹ���" );
					
				}
			}
			
			if(stmt != null)
				stmt.close();
			response.sendRedirect("./psSet.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} 
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
