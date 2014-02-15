package JsonTest.test;

import JsonTest.Classes.User;
import JsonTest.Connection.DBConn;
import JsonTest.Connection.DBHandle;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	   
	        Gson gson = new Gson();
	        PrintWriter out = response.getWriter();
	        
	        try {
	            StringBuilder sb = new StringBuilder();
	            String str;
	            while ((str = request.getReader().readLine()) != null) {
	                sb.append(str);
	            }
	            User user = (User) gson.fromJson(sb.toString(), User.class);
	            System.out.print(gson.toJson(user));
	           
	          java.sql.Connection con = new DBConn().getConnection();
	          String setquery = "INSERT INTO test(username,password) VALUES('"+user.getUserName()+"','"+user.getPassWord()+"')";
	          //DBHandle.setData(con, setquery);
	          
	          if(DBHandle.setData(con, setquery) == 1 ){
	          response.setContentType("text/html"); 
	          out.println("Registered");} 	
	          
	          else{
	        	  response.setContentType("text/html"); 
		          out.println("Failed");}
	          
	          System.out.print(response);
	          
	        } catch (Exception ex) {
	            Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            
				out.close();
	        }
	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }

	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        processRequest(request, response);
	    }

	    @Override
	    public String getServletInfo() {
	        return "Short description";
	    }

}
