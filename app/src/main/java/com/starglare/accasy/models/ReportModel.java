package com.starglare.accasy.models;

/**
 * Created by MuhammadAmin on 7/20/2017.
 */

public class ReportModel {

    private int id;
    private String category;
    private String subCategory;
    private String coordinates;
    private String comment;
    private byte[] image;
    private String phoneNumber;
    private long time;
    private int posted;

    private static ReportModel singleton;

    private ReportModel() {

    }

    public static ReportModel getModelInstance() {
        /*if (singleton == null) {
            singleton = new ReportModel();
        }*/
        return  new ReportModel();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setPosted(int posted) {
        this.posted = posted;
    }

    public int getPosted() {
        return posted;
    }
}
