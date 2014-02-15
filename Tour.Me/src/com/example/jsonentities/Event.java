package com.example.jsonentities;

public class Event {
    private String eventid;
    private String journeyid;
    private String name;
    private String description;
    private String date;
    private String time;
    private String latitude;
    private String longitude;
    private String refpoi;
    
    public String getRefpoi() {
	return refpoi;
}

public void setRefpoi(String refpoi) {
	this.refpoi = refpoi;
}

	/**
     * @return the eventid
     */
    public String getEventid() {
        return eventid;
    }

    /**
     * @param eventid the eventid to set
     */
    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return the latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
