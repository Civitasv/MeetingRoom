package whu.sres.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import whu.sres.model.User;

@WebServlet("/servlet/addUser")
public class AddUserServlet extends HttpServlet {

    /**
     * Constructor of the object.
     */
    public AddUserServlet() {
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
     * <p>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"), "utf-8");
        String password = request.getParameter("password");
        String tel = request.getParameter("tel");
        String role = request.getParameter("role");

        if (id == null || id.trim().equals("")) {

            out.println(" <script>alert('??????????')</script>");
            out.println(" <script>window.location.href = 'userAdd.jsp'</script>");
            out.flush();
            return;


        }
        if (name == null || name.trim().equals("")) {
            out.println(" <script>alert('????????????');window.location.href = 'userAdd.jsp'</script>");
            out.flush();
            return;
        }

        if (password == null || password.trim().equals("")) {
            out.println(" <script>alert('?????????');window.location.href = 'userAdd.jsp'</script>");
            out.flush();
            return;
        }
        if (tel == null || tel.trim().equals("")) {
            out.println(" <script>alert('?ดย???????');window.location.href = 'userAdd.jsp'</script>");
            out.flush();
            return;
        }
        if (role == null || role.trim().equals("")) {
            out.println(" <script>alert('??????????');window.location.href = 'userAdd.jsp'</script>");
            out.flush();
            return;
        }

        User u = new User(id, password, tel, role, name);
        if (u.userInsert()) {

            out.println(" <script>alert('?????????');window.location.href = 'usermanage.jsp'</script>");
            out.flush();
            out.close();
        } else {

            out.println(" <script>alert('??????????????????????????');window.location.href = 'usermanage.jsp'</script>");
            out.flush();
            out.close();
        }
    }

    /**
     * The doPost method of the servlet. <br>
     * <p>
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
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
