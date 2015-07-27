package com.joetz.domain;

import java.io.Serializable;

/**
 * This class is a representation of a camp as is on the website of Joetz.
 * It holds all the information about a camp.
 */
public class Camp implements Serializable {

    private int id;
    private String title;
    private String city;
    private String period;
    private String promotext;
    private String place;
    private String transport;
    private int maxParticipants;
    private double price;
    private double starPrice1;
    private double starPrice2;
    private String extraInfo;
    private int isDeductible;
    private int registrations;
    private int isFeatured;
    private String location;
    private int minimumAge;
    private int maximumAge;

    public Camp() {}

    public void setId(int id){ this.id = id; }

    public int getId(){ return id; }

    public void setTitle(String title){ this.title = title; }

    public String getTitle(){ return title; }

    public void setCity(String city){ this.city = city; }

    public String getCity(){ return city; }

    public void setPeriod(String period){ this.period = period; }

    public String getPeriod(){ return period; }

    public void setPromotext(String promotext){ this.promotext = promotext; }

    public String getPromotext(){ return promotext; }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public String getExtraInfo() { return extraInfo; }

    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }

    public int getMaxParticipants() { return maxParticipants; }

    public void setMaxParticipants(int maxParticipants) { this.maxParticipants = maxParticipants; }

    public int getRegistrations() { return registrations; }

    public void setRegistrations(int registrations) { this.registrations = registrations; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public double getStarPrice1() { return starPrice1; }

    public void setStarPrice1(double starPrice1) { this.starPrice1 = starPrice1; }

    public double getStarPrice2() { return starPrice2; }

    public void setStarPrice2(double starPrice2) { this.starPrice2 = starPrice2; }

    public int getIsDeductible() { return isDeductible; }

    public int getIsFeatured() {return isFeatured;}

    public void setIsFeatured(int isFeatured) { this.isFeatured = isFeatured; }

    public void setIsDeductible(int isDeductible) { this.isDeductible = isDeductible; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public String getTransport() { return transport; }

    public void setTransport(String transport) { this.transport = transport; }

    public int getMinimumAge() { return minimumAge; }

    public void setMinimumAge(int minimumAge) { this.minimumAge = minimumAge; }

    public int getMaximumAge() { return maximumAge; }

    public void setMaximumAge(int maximumAge) { this.maximumAge = maximumAge; }

    public boolean lastRegistrations() { return availableRegistrations() <= 10; }

    public int availableRegistrations() { return maxParticipants - registrations; }

}
