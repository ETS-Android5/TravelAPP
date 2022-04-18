/**
 * This is the class to define the Journey object
 *
 * created by Harry Akitt 04/04/2022
 * **/

package com.example.team05;

public class Journey {
    private String departureT1;
    private String arrivalT1;
    private String departureT2;
    private String arrivalT2;
    private String departureT3;
    private String arrivalT3;
    private String arrivalTime;
    private String price;
    private String legs;

    public Journey(String departureT1, String arrivalT1, String price, String totalT) {
        this.departureT1 = departureT1;
        this.arrivalTime = arrivalT1;
        this.price = price;
        this.legs = totalT;
    }

    public Journey(String departureT1, String arrivalT1, String departureT2, String arrivalT2, String price, String totalT) {
        this.departureT1 = departureT1;
        this.arrivalT1 = arrivalT1;
        this.price = price;
        this.legs = totalT;

        this.departureT2 = departureT2;
        this.arrivalTime = arrivalT2;
    }


    public Journey(String departureT1, String arrivalT1, String departureT2, String arrivalT2, String departureT3, String arrivalT3, String price, String totalT) {
        this.departureT1 = departureT1;
        this.arrivalT1 = arrivalT1;
        this.price = price;
        this.legs = totalT;

        this.departureT2 = departureT2;
        this.arrivalT2 = arrivalT2;
        this.departureT3 = departureT3;
        this.arrivalTime = arrivalT3;
    }


    public String getPrice() {
        return price;
    }

    public String getArrivalT() {
        return arrivalTime;
    }

    public String getDepartureT1() {
        return departureT1;
    }

    public String getTotalT() {
        return legs;
    }
}
