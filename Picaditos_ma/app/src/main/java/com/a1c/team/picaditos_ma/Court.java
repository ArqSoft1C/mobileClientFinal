package com.a1c.team.picaditos_ma;

public class Court {

    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private int price_hour;

    public Court(int id, String name, double latitude, double longitude, String address, int price_hour) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.price_hour = price_hour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice_hour() {
        return price_hour;
    }

    public void setPrice_hour(int price_hour) {
        this.price_hour = price_hour;
    }
}


