/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

import JsonTest.Classes.POI;
import JsonTest.Classes.POIDetails;
import JsonTest.Connection.DBConn;
import JsonTest.Connection.DBHandle;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CreatePOI", urlPatterns = {"/CreatePOI"})
public class CreatePOI extends HttpServlet {

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
            System.out.println("Gos");
           Gson gson = new Gson();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            
                POIDetails poi= gson.fromJson(sb.toString(), POIDetails.class);
                java.sql.Connection con = new DBConn().getConnection();
                
                String setquery="INSERT INTO poi(latitude,longitude,picture,description,rating,name,votes,category,address) VALUES("+poi.getLatitude()+","+poi.getLongitude()+",'"+poi.getPicture()+"','"+poi.getDescription()+"',0,'"+poi.getName()+"',0,'"+poi.getCategory()+"','"+poi.getAddress()+"')";
                int res = DBHandle.setData(con, setquery);
                if(res==1)
                out.print("success");
                else{
                out.print("Error");
                }
        } catch (Exception ex) {
            Logger.getLogger(CreatePOI.class.getName()).log(Level.SEVERE, null, ex);
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
