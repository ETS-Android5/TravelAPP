/**
 ***** Description *****
 * This class creates a Journey Object
 *
 ***** Key Functionality *****
 * -Create an accessible journey object that can have either 1, 2 or 3 legs
 *
 ***** Author(s)  *****
 * Harry Akitt
 * -Main functionality (Created 04/04/22)
 * Oli Presland
 * -Intial overloaded constructor tweak
 *
 * **/

package com.example.team05;


import java.io.Serializable;

public class Journey implements Serializable {
    private String departureT1;
    private String arrivalT1;
    private String departureT2;
    private String arrivalT2;
    private String departureT3;
    private String arrivalT3;
    private String price;
    private String legs;
    private String operator1;
    private String bus1;
    private String operator2;
    private String bus2;
    private String operator3;
    private String bus3;
    private String departureStation1;
    private String departureStation2;
    private String departureStation3;
    private String arrivalStation1;
    private String arrivalStation2;
    private String arrivalStation3;

    public Journey(String departureT1, String arrivalT1, String price, String legs, String operator1, String bus1, String departureStation1, String arrivalStation1){
        this.departureT1 = departureT1;
        this.arrivalT1 = arrivalT1;
        this.price = price;
        this.legs = legs;

        this.operator1 = operator1;
        this.bus1 = bus1;

        this.arrivalStation1 = arrivalStation1;
        this.departureStation1 = departureStation1;
    }

    public Journey(String departureT1, String arrivalT1, String departureT2, String arrivalT2, String price, String legs, String operator1, String bus1, String operator2, String bus2, String departureStation1, String arrivalStation1, String departureStation2, String arrivalStation2) {
        this.departureT1 = departureT1;
        this.arrivalT1 = arrivalT1;
        this.price = price;
        this.legs = legs;

        this.departureT2 = departureT2;
        this.arrivalT2 = arrivalT2;

        this.operator1 = operator1;
        this.bus1 = bus1;
        this.operator2 = operator2;
        this.bus2 = bus2;

        this.arrivalStation1 = arrivalStation1;
        this.departureStation1 = departureStation1;
        this.arrivalStation2 = arrivalStation2;
        this.departureStation2 = departureStation2;
    }


    public Journey(String departureT1, String arrivalT1, String departureT2, String arrivalT2, String departureT3, String arrivalT3, String price, String legs, String operator1, String bus1, String operator2, String bus2, String operator3, String bus3, String departureStation1, String arrivalStation1, String departureStation2, String arrivalStation2, String departureStation3, String arrivalStation3) {
        this.departureT1 = departureT1;
        this.arrivalT1 = arrivalT1;
        this.price = price;
        this.legs = legs;

        this.departureT2 = departureT2;
        this.arrivalT2 = arrivalT2;
        this.departureT3 = departureT3;
        this.arrivalT3 = arrivalT3;

        this.operator1 = operator1;
        this.bus1 = bus1;
        this.operator2 = operator2;
        this.bus2 = bus2;
        this.operator3 = operator3;
        this.bus3 = bus3;

        this.arrivalStation1 = arrivalStation1;
        this.departureStation1 = departureStation1;
        this.arrivalStation2 = arrivalStation2;
        this.departureStation2 = departureStation2;
        this.arrivalStation3 = arrivalStation3;
        this.departureStation3 = departureStation3;
    }


    public String getPrice() {
        return price;
    }

    public String getDepartureT1() {
        return departureT1;
    }

    public String getLegs() {
        return legs;
    }

    public String getArrivalT1() {
        return arrivalT1;
    }

    public String getArrivalT2() {
        return arrivalT2;
    }

    public String getArrivalT3() {
        return arrivalT3;
    }

    public String getBus1() {
        return bus1;
    }

    public String getBus2() {
        return bus2;
    }

    public String getBus3() {
        return bus3;
    }

    public String getDepartureT2() {
        return departureT2;
    }

    public String getDepartureT3() {
        return departureT3;
    }

    public String getOperator1() {
        return operator1;
    }

    public String getOperator2() {
        return operator2;
    }

    public String getOperator3() {
        return operator3;
    }

    public String getArrivalStation1() {
        return arrivalStation1;
    }

    public String getArrivalStation2() {
        return arrivalStation2;
    }

    public String getArrivalStation3() {
        return arrivalStation3;
    }

    public String getDepartureStation1() {
        return departureStation1;
    }

    public String getDepartureStation2() {
        return departureStation2;
    }

    public String getDepartureStation3() {
        return departureStation3;
    }

    public String getArrivalT(){
        String ArrivalT = null;
        if (getLegs().equalsIgnoreCase("1"))
            ArrivalT = getArrivalT1();
        if (getLegs().equalsIgnoreCase("2"))
            ArrivalT = getArrivalT2();
        if (getLegs().equalsIgnoreCase("3"))
            ArrivalT = getArrivalT3();

        return ArrivalT;
    }

}
