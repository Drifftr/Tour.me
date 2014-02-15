/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Classes;

/**
 *
 * @author Heshitha
 */
public class Path {
    private String userID;
    private String journeyID;
    private String geoPoints;

    /**
     * @return the userID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * @return the journeyID
     */
    public String getJourneyID() {
        return journeyID;
    }

    /**
     * @param journeyID the journeyID to set
     */
    public void setJourneyID(String journeyID) {
        this.journeyID = journeyID;
    }

    /**
     * @return the geoPoints
     */
    public String getGeoPoints() {
        return geoPoints;
    }

    /**
     * @param geoPoints the geoPoints to set
     */
    public void setGeoPoints(String geoPoints) {
        this.geoPoints = geoPoints;
    }
}
