/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

import JsonTest.Classes.User;
import JsonTest.Connection.DBConn;
import JsonTest.Connection.DBHandle;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Heshitha
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            Gson gson = new Gson();
            //  String message = "didnt recieve";
            ///message = gson.fromJson(request.getParameter("name"), String.class);
            // out.print(gson.toJson(message));
            //  String fromJson = gson.fromJson((String)request.getParameter("success"),String.class); // this parses the json
            //gets all the keys

            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str,id = null,userName = null;
            while ((str = br.readLine()) != null) {
                sb.append(str);
                User user = gson.fromJson(sb.toString(), User.class);
                 java.sql.Connection con = new DBConn().getConnection();
                 
            String getquery = "SELECT id,username FROM test WHERE username='"+user.getUserName()+"' AND password='"+user.getPassWord()+"'";
                ResultSet res = DBHandle.getData(con, getquery);

               while(res.next()){
               id=res.getString("id");
               userName=res.getString("username");
                }
                
               if(id==null || id.trim().equals("")){
                   out.print("false");
                  
               }else{
                   out.print(id+":"+userName);
                    System.out.print(id+":"+userName);
               }
                
               
                

            }
        } catch (Exception ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
