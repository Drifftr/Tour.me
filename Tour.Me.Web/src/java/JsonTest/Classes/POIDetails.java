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
public class POIDetails {
    private String pid;
    private String latitude;
    private String Longitude;
    private String rating;
    private String description;
    private String picture;
    private String name;
    private String votes;
    private String address;
    private String category;
    private ArrayList<Comment> comments;

    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(String pid) {
        this.pid = pid;
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
     * @return the Longitude
     */
    public String getLongitude() {
        return Longitude;
    }

    /**
     * @param Longitude the Longitude to set
     */
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    /**
     * @return the rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(String rating) {
        this.rating = rating;
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
     * @return the picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture the picture to set
     */
    public void setPicture(String picture) {
        this.picture = picture;
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
     * @return the comments
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return the votes
     */
    public String getVotes() {
        return votes;
    }

    /**
     * @param votes the votes to set
     */
    public void setVotes(String votes) {
        this.votes = votes;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}
