package com.egitim.sendmylocation;

public class LocationData {
    private String userId;//kullanıcı id'si
    private String userName;//kullanıcı adı - mail adresinin @ kısmına kadar olan bölümü
    private double latitude;// enlem bilgisi
    private double longitude;//boylam bilgisi
    private String dateTime;//tarih ve zaman bilgisini tutan değişken
    private int priority;
    private String condition;
    //parametresiz constructor
    public LocationData(){}

    //parametreli constructor
    public LocationData(String userId, String userName, double latitude, double longitude,String dateTime, int priority,String condition){
        this.userId = userId;
        this.userName = userName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
        this.priority = priority;
        this.condition = condition;
    }

    //Getter ve Setter'lar
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPriority(){return priority;}

    public void setPriority(int priority){
        this.priority = priority;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
