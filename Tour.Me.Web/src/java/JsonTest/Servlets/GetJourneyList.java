/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

import JsonTest.Classes.Event;
import JsonTest.Classes.Journey;
import JsonTest.Classes.POIDetails;
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
@WebServlet(name = "GetJourneyList", urlPatterns = {"/GetJourneyList"})
public class GetJourneyList extends HttpServlet {

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
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            Journey journeytxt=gson.fromJson(sb.toString(), Journey.class);
            java.sql.Connection con = new DBConn().getConnection();
            String getquery = "SELECT * from journey WHERE userid="+journeytxt.getUserid()+"";
            ResultSet res = DBHandle.getData(con, getquery);
            ArrayList<Journey> journeyList;
            journeyList = new ArrayList<Journey>();
            
            while (res.next()) {
                   Journey journey=new Journey();
                   journey.setJourneyid(res.getString("journeyid"));
                   journey.setName(res.getString("name"));
                   journey.setTravelstate(res.getString("travelstate"));
                   journey.setDate(res.getString("date"));
                   journey.setTime(res.getString("time"));
                   journey.setUserid(res.getString("userid"));
                   journeyList.add(journey);
                }
                String JsonString = gson.toJson(journeyList, ArrayList.class);
                
                System.out.println(JsonString);
                
                if (JsonString.trim().equals("")) {
                    out.print("false");

                } else {
                    out.print(JsonString);

                }
   
               
               
        } catch (Exception ex) {            
            Logger.getLogger(GetJourneyList.class.getName()).log(Level.SEVERE, null, ex);
        }finally {            
            out.close();
        }
    }
    
    public ArrayList<Event> getEvents(java.sql.Connection con,String journeyid){
        try {
            ResultSet res2 = DBHandle.getData(con,"Select * from event c where journeyid="+journeyid+"");
            ArrayList<Event> eventList;
            eventList = new ArrayList<Event>();
            while(res2.next()){
                Event event=new Event();
                event.setEventid(res2.getString("eventid"));
                event.setJourneyid(res2.getString("journeyid"));
                event.setDate(res2.getString("date"));
                event.setTime(res2.getString("time"));
                event.setName(res2.getString("name"));
                event.setDescription(res2.getString("description"));
                event.setLatitude(res2.getString("latitude"));
                event.setLongitude(res2.getString("longitude"));
                eventList.add(event);
           }
            return eventList;
        } catch (Exception ex) {
            Logger.getLogger(GetPOI.class.getName()).log(Level.SEVERE, null, ex);
             return null;
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
