/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

import JsonTest.Classes.Comment;
import JsonTest.Classes.Event;
import JsonTest.Classes.Journey;
import JsonTest.Connection.DBConn;
import JsonTest.Connection.DBHandle;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
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
@WebServlet(name = "CreateJourney", urlPatterns = {"/CreateJourney"})
public class CreateJourney extends HttpServlet {

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
            Gson gson = new Gson();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str= "";
            while ((str = br.readLine()) != null) {
                sb.append(str);
                }
            Journey journey=gson.fromJson(sb.toString(), Journey.class);
            java.sql.Connection con = new DBConn().getConnection();
            String getquery="SELECT journeyid FROM journey";
            ResultSet res = DBHandle.getData(con, getquery);
            int id=0;
            while(res.next()){
                id=res.getInt("journeyid");
            
            }
            id++;
          String setquery = "INSERT INTO journey(journeyid,name,travelstate,date,time,userid,startlat,startlng,endlat,endlng) VALUES("+id+",'"+journey.getName()+"','tobe','"+journey.getDate()+"','"+journey.getTime()+"',"+journey.getUserid()+",'"+journey.getStartlat()+"','"+journey.getStartlng()+"','"+journey.getEndlat()+"','"+journey.getEndlng()+"')";
          int res2 = DBHandle.setData(con, setquery);
          ArrayList<Event> events=journey.getEvents();
          if(res2!=0){
              for(Event event:events){
                  String query = "INSERT INTO event(journeyid,name,date,time,description,latitude,longitude,refpoi) VALUES("+id+",'"+event.getName()+"','"+event.getDate()+"','"+event.getTime()+"','"+event.getDescription()+"','"+event.getLatitude()+"','"+event.getLongitude()+"','"+event.getRefpoi()+"')";
                  DBHandle.setData(con,query);
               }
              out.print("Success");
          }
        } catch (Exception ex) {
            Logger.getLogger(CreateJourney.class.getName()).log(Level.SEVERE, null, ex);
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
