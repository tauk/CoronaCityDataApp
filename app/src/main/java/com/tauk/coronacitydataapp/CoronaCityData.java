package com.tauk.coronacitydataapp;

public class CoronaCityData {
    public String city;
    public int cases;
    public int recoveries;

    //Default constructor
    public CoronaCityData() {

    }

    //parameterized constructor
    public CoronaCityData(String city, int cases, int recoveries) {
        this.city = city;
        this.cases = cases;
        this.recoveries = recoveries;
    }

    @Override
    public String toString() {
        return "\nCity:" +city + " Cases:" + cases + " Recoveries:" + recoveries;
    }
}
