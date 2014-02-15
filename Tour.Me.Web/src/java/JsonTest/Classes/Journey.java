/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsonTest.Classes;

import java.util.ArrayList;

/**
 *
 * @author Heshitha
 */
public class Journey {
    private String journeyid;
    private String name;
    private String travelstate;
    private String userid;
    private String date;
    private String time;
    private String startlat;
    private String startlng;
    private String endlat;
    private String endlng;
    private ArrayList<Event> events;
    /**
     * @return the journeyid
     */
    public String getJourneyid() {
        return journeyid;
    }

    /**
     * @param journeyid the journeyid to set
     */
    public void setJourneyid(String journeyid) {
        this.journeyid = journeyid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the travelstate
     */
    public String getTravelstate() {
        return travelstate;
    }

    /**
     * @param travelstate the travelstate to set
     */
    public void setTravelstate(String travelstate) {
        this.travelstate = travelstate;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the startlat
     */
    public String getStartlat() {
        return startlat;
    }

    /**
     * @param startlat the startlat to set
     */
    public void setStartlat(String startlat) {
        this.startlat = startlat;
    }

    /**
     * @return the startlng
     */
    public String getStartlng() {
        return startlng;
    }

    /**
     * @param startlng the startlng to set
     */
    public void setStartlng(String startlng) {
        this.startlng = startlng;
    }

    /**
     * @return the endlat
     */
    public String getEndlat() {
        return endlat;
    }

    /**
     * @param endlat the endlat to set
     */
    public void setEndlat(String endlat) {
        this.endlat = endlat;
    }

    /**
     * @return the endlng
     */
    public String getEndlng() {
        return endlng;
    }

    /**
     * @param endlng the endlng to set
     */
    public void setEndlng(String endlng) {
        this.endlng = endlng;
    }

    /**
     * @return the events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * @param events the events to set
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
