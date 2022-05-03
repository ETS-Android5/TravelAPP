/**
 * This is the page to all the user to view their booking and confirm.
 *
 * TO DO:
 * - RETURN AND OUTBOUND JOURNEY WILL BE SHOWN, ALONG WITH QUANTITY AND TOTAL COST
 *
 * Changelog:
 * - page now returns details of outbound and return journeys
 *
 * created by Harry Akitt 16/03/2022
 * **/

package com.example.team05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class ConfirmationPage extends AppCompatActivity {

    public String dayName;
    public String castleNameShortened;
    private String horsePayJson;
    private View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        TextView tv = (TextView) findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        TextView tv5 = (TextView) findViewById(R.id.tv5);
        TextView tv6 = (TextView) findViewById(R.id.tv6);
        TextView priceTv = (TextView) findViewById(R.id.price);
        TextView tv7 = (TextView) findViewById(R.id.textViewDepart);
        Intent incomingIntent = getIntent();

        //extra info buttons
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);

        //ALL INCOMING INFO
        //SHARED
        String price = incomingIntent.getStringExtra("outPrice");
        String searchedDate = incomingIntent.getStringExtra("searchedDate");
        String castle = incomingIntent.getStringExtra("Castle");
        String dayName = incomingIntent.getStringExtra("DayName");
        String TicketType = incomingIntent.getStringExtra("TicketType");

        //journey objects
        Journey journeyOut = (Journey) incomingIntent.getSerializableExtra("OutJourney");
        Journey journeyRet = (Journey) incomingIntent.getSerializableExtra("RetJourney");

        //pop up when extra button clicked
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp(journeyOut,view);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp(journeyRet,view);
            }
        });

        //hide action bar
        ActionBar bar = getSupportActionBar();
        bar.hide();

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.getMenu().setGroupCheckable(0,false,true);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(ConfirmationPage.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(ConfirmationPage.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(ConfirmationPage.this, More.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        //all intents from previous page
        String outDepart = setTimeFormat(journeyOut.getDepartureT1());
        String outArrive = setTimeFormat(journeyOut.getArrivalT());
        String retDepart = setTimeFormat(journeyRet.getDepartureT1());
        String retArrive = setTimeFormat(journeyRet.getArrivalT());
        Double priceBus = Double.parseDouble(price.substring(1));

        String departure = "Eldon Square";
        if(castle.equals("Bamburgh")){
            departure = "Haymarket";}
        if (castle.equals("Alnwick")){
            departure = "Haymarket";}

        //Castle ticket price
        Double priceTicket = 0.0;
        if(castle.equals("Alnwick")){
            priceTicket = 15.75;
        } else if(castle.equals("Auckland")){
            priceTicket = 14.00;
        } else if(castle.equals("Barnard")){
            priceTicket = 8.10;
        } else {
            priceTicket = 14.10;
        }

        //Number of student
        Integer Number = Integer.parseInt(incomingIntent.getStringExtra("quantity"));
//
        Double totalPrice = (priceTicket+priceBus)*Number;


//        String str = "Your trip to " + castleName+" Castle"+"\n"
//                +"Outbound: " + outDepart + outArrive +"\n"
//                + "Return: " + retDepart + retArrive +"\n"
//                + "Price: £" + price;
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(str);
//        tv.setText(stringBuilder.toString());
        tv7.setText("Departure: " + departure);
        tv.setText("Destination: "+castle+" Castle");
        tv2.setText("Outbound: "+outDepart+" ->"+outArrive);
        tv3.setText("Return: "+retDepart+" ->"+retArrive);
        priceTv.setText("Price: £" + String.format("%.2f",totalPrice));
        tv5.setText("   " + Number + " x " + TicketType + " @ £" + String.format("%.2f",priceBus));
        tv6.setText("   " + Number + " x " + "Castle Ticket" + " @ £" + String.format("%.2f",priceTicket));

        Button payBtn = (Button) findViewById(R.id.payButton);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hours = currentTime.getTime().getHours();
                int mins = currentTime.getTime().getMinutes();
                String hoursString = null;
                String minsString = null;
                Log.d("Test", String.valueOf(currentTime.getTimeZone().getDisplayName()));

                if(hours<10){
                    hoursString = "0" + hours;
                }
                if(mins<10){
                    minsString = "0" + mins;
                }

                String customerID = createID();


                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                String transactionTime = formatter.format(date);
                String timeZone = "BST";

                String request = "{\"storeID\":\"Team05\", \"customerID\":\""+customerID+"\", \"date\":\""+searchedDate+"\", \"time\":\""+transactionTime+"\", \"timeZone\":\""+timeZone+"\", \"transactionAmount\":\""+totalPrice+"\", \"currencyCode\":\"GBP\"}";
                Log.d("Confirm",request);
                String[] data = new String[1];
                data[0] = request;
                new PaymentTask(ConfirmationPage.this).execute(data);

            }
        });

    }

    private String createID(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Random rnd = new Random();

        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 5; i++){
            id.append(chars[rnd.nextInt(chars.length)]);
        }
        return id.toString();
    }

    private String setTimeFormat(String providedTime){

        if(providedTime.length()==3){
            providedTime = "0"+providedTime;
        }
        return providedTime.substring(0,2) + ":" + providedTime.substring(2,4);
    }

    private String setJourneyTime(String arriveT, String departT){
        int departAsMinutes = 60 * Integer.parseInt(departT.substring(0,2)) + Integer.parseInt(departT.substring(3,5));
        int arrivalAsMinutes = 60*Integer.parseInt(arriveT.substring(0,2)) + Integer.parseInt(arriveT.substring(3,5));

        int totalMinutes = arrivalAsMinutes - departAsMinutes;
        int minutes = totalMinutes%60;
        int hours = (totalMinutes - minutes)/60;

        if(hours==1){
            return (hours + " hour, "+minutes+" minutes.");
        }else{
            return (hours + " hours, "+minutes+" minutes.");
        }
    }

    private void popUp(Journey journey, View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int legs = Integer.valueOf(journey.getLegs());

        // parameters for popup window
        int height = 1000;
        int width = 1000;

        if (legs == 1) {
            popupView = inflater.inflate(R.layout.confirm_details_1_leg, null);
            height = 850;
        }
        if (legs == 2) {
            popupView = inflater.inflate(R.layout.confirm_details_2_leg, null);
            height = 1450;
        }
        if (legs == 3) {
            popupView = inflater.inflate(R.layout.confirm_details_3_leg, null);
            height = 2000;
        }

        PopupWindow pw = new PopupWindow(popupView, width, height, true);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);

        //leg 1 text views
        TextView tv_op1 = pw.getContentView().findViewById(R.id.leg1_operator);
        tv_op1.setText(journey.getOperator1() + " " + journey.getBus1());
        TextView leg1_start = pw.getContentView().findViewById(R.id.leg1_start);
        leg1_start.setText("Departure: " + setTimeFormat(journey.getDepartureT1()) + " from " + journey.getDepartureStation1());
        TextView leg1_end = pw.getContentView().findViewById(R.id.leg1_end);
        leg1_end.setText("Arrival Time: " + setTimeFormat(journey.getArrivalT1()) + " at " + journey.getArrivalStation1());
        TextView leg1_time = pw.getContentView().findViewById(R.id.leg1_time);
        String leg1_total = setJourneyTime(setTimeFormat(journey.getArrivalT1()), setTimeFormat(journey.getDepartureT1()));
        leg1_time.setText(leg1_total);

        //leg 2 text views
        if (legs > 1) {
            TextView tv_op2 = pw.getContentView().findViewById(R.id.leg2_operator);
            tv_op2.setText(journey.getOperator2() + " " + journey.getBus2());
            TextView leg2_end = pw.getContentView().findViewById(R.id.leg2_end);
            leg2_end.setText("Arrival Time: " + setTimeFormat(journey.getArrivalT2()) + " at " + journey.getArrivalStation2());
            TextView leg2_info = pw.getContentView().findViewById(R.id.leg2_info);
            leg2_info.setText("Departure: " + setTimeFormat(journey.getDepartureT2()) + " from " + journey.getDepartureStation2());
            TextView leg2_time = pw.getContentView().findViewById(R.id.leg2_time);
            String leg2_total = setJourneyTime(setTimeFormat(journey.getArrivalT2()), setTimeFormat(journey.getDepartureT2()));
            leg2_time.setText(leg2_total);
        }

        //leg 3 text views
        if (legs > 2) {
            TextView tv_op3 = pw.getContentView().findViewById(R.id.leg3_operator);
            tv_op3.setText(journey.getOperator3() + " " + journey.getBus3());
            TextView leg3_end = pw.getContentView().findViewById(R.id.leg3_end);
            leg3_end.setText("Arrival Time: " + setTimeFormat(journey.getArrivalT3()) + " at " + journey.getArrivalStation3());
            TextView leg3_info = pw.getContentView().findViewById(R.id.leg3_info);
            leg3_info.setText("Departure: " + setTimeFormat(journey.getDepartureT3()) + " from " + journey.getDepartureStation3());
            TextView leg3_time = pw.getContentView().findViewById(R.id.leg3_time);
            String leg3_total = setJourneyTime(setTimeFormat(journey.getArrivalT3()), setTimeFormat(journey.getDepartureT3()));
            leg3_time.setText(leg3_total);
        }

        //set leg total time values
        TextView total_time = pw.getContentView().findViewById(R.id.total_time);
        total_time.setText("Total Time: " + setJourneyTime(setTimeFormat(journey.getArrivalT()), setTimeFormat(journey.getDepartureT1())));


        // set return button
        pw.getContentView().findViewById(R.id.return_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });
    }

}
