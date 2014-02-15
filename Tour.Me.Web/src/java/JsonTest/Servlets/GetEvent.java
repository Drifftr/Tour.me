/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

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
@WebServlet(name = "GetEvent", urlPatterns = {"/GetEvent"})
public class GetEvent extends HttpServlet {

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
            Event eventf=gson.fromJson(sb.toString(), Event.class);
            java.sql.Connection con = new DBConn().getConnection();
            ResultSet res2 = DBHandle.getData(con,"Select * from event c where eventid="+eventf.getEventid()+"");
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
                event.setRefpoi(res2.getString("refpoi"));
                eventList.add(event);
            }
            String JsonString = gson.toJson(eventList, ArrayList.class);
             if (JsonString.trim().equals("")) {
                    out.print("false");

                } else {
                    out.print(JsonString);

                }
        } catch (Exception ex) {            
            Logger.getLogger(GetEvent.class.getName()).log(Level.SEVERE, null, ex);
        }finally {            
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
