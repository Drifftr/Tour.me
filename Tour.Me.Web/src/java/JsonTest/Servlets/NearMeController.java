/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Servlets;

import JsonTest.Classes.POI;
import JsonTest.Classes.POIDetails;
import JsonTest.Classes.User;
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
@WebServlet(name = "NearMeController", urlPatterns = {"/NearMeController"})
public class NearMeController extends HttpServlet {

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
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            Gson gson = new Gson();
            StringBuilder sb = new StringBuilder();
            BufferedReader br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
                POI poi= gson.fromJson(sb.toString(), POI.class);
                java.sql.Connection con = new DBConn().getConnection();
                //String getquery = "SELECT *, ( 3959 * acos( cos( radians("+Double.parseDouble(poi.getLatitude())+") ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians("+Double.parseDouble(poi.getLongitude())+") ) + sin( radians("+Double.parseDouble(poi.getLatitude())+") ) * sin( radians( latitude ) ) ) ) AS distance FROM poi HAVING distance < 10 ORDER BY distance;";
               //test
                String getquery="SELECT * FROM poi";
                if(!poi.getCategory().contains("all")){
                getquery= "SELECT * FROM poi WHERE category='"+poi.getCategory()+"'";
                }
                ResultSet res = DBHandle.getData(con, getquery);
                ArrayList<POIDetails> poiList;
                poiList = new ArrayList<POIDetails>();
               
                while (res.next()) {
                   
                   POIDetails poid=new POIDetails();
                   poid.setPid(res.getString("pid"));
                   poid.setName(res.getString("name"));
                   poid.setLatitude(res.getString("latitude"));
                   poid.setLongitude(res.getString("longitude"));
                   poid.setCategory(res.getString("category"));
                   poid.setRating(res.getString("rating"));
                   poid.setDescription(res.getString("description"));
                   poid.setPicture(res.getString("picture"));
                   poiList.add(poid);
                    
                }
                String JsonString = gson.toJson(poiList, ArrayList.class);
                
                
                
                if (JsonString.trim().equals("")) {
                    out.print("false");

                } else {
                    out.print(JsonString);

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
