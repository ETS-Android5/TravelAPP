/**
 ***** Description *****
 * This is the page to all the user to view their booking and confirm.
 *
 ***** Key Functionality *****
 * -Retrieve information about selected journey
 * -Display overall price
 * -Allow 'booking' of journey
 *
 ***** Author(s)  *****
 * Harry Akitt (Created 16/03/22)
 * -Retrieves information from passed intent and displays to user
 * -Add intents
 * - Added back button with functionality
 * - Added popups
 * Qingbiao Song
 * -Calculates price
 * -display information(Destination,time of Outbound and Return And total price)
 * -User Interface design
 * -added payment button
 * Oli Presland
 * -Create request to send to HorsePay via PaymentTask
 *
 ***** Changelog: *****
 * -page now returns selected journeys to the screen and can be clicked on to take to next page
 * - RETURN AND OUTBOUND JOURNEY WILL SHOWN, ALONG WITH QUANTITY AND TOTAL COST
 * Qingbiao Song
 * -Calculates price
 * -display information(Destination,time of Outbound and Return And total price)
 * -User Interface design
 * **/


package com.example.team05;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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

        //set page layout
        setContentView(R.layout.confirm);

        //identify text views
        TextView tv = (TextView) findViewById(R.id.textView); //castle name
        TextView tv2 = (TextView) findViewById(R.id.textView2); //outbound journey
        TextView tv3 = (TextView) findViewById(R.id.textView3); //return journey
        TextView tv5 = (TextView) findViewById(R.id.tv5); //ticket price for travel
        TextView tv6 = (TextView) findViewById(R.id.tv6); //castle ticket price
        TextView priceTv = (TextView) findViewById(R.id.price); //total price
        TextView tv7 = (TextView) findViewById(R.id.textViewDepart); //departure station

        //set Address
        TextView DepartAddress = (TextView) findViewById(R.id.textViewDepartAddress); //bus station address
        TextView CastleAddress = (TextView) findViewById(R.id.textViewCastleAddress); //castle station address

        //ticket price(in the box)
        TextView busPriceBlock = (TextView) findViewById(R.id.busPriceBlock);
        TextView castlePriceBlock = (TextView) findViewById(R.id.castlePriceBlock);

        //get intents from previous page
        Intent incomingIntent = getIntent();

        //extra info buttons
        Button btn1 = (Button) findViewById(R.id.btn1);
        Button btn2 = (Button) findViewById(R.id.btn2);

        //ALL INCOMING INFO
        //SHARED
        String price = incomingIntent.getStringExtra("outPrice");
        String searchedDate = incomingIntent.getStringExtra("searchedDate");
        String castleFull = incomingIntent.getStringExtra("Castle");
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

        //pop up when extra button clicked
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
                        Intent intent2 = new Intent(ConfirmationPage.this, ThingsToDo.class);
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

        //set departure station dependant on destination
        String departure = "Eldon Square";
        if(castleFull.equals("Bamburgh")){
            departure = "Haymarket";}
        if (castleFull.equals("Alnwick")){
            departure = "Haymarket";}

        //Castle ticket price
        Double priceTicket = 0.0;
        if(castleFull.equals("Alnwick")){
            priceTicket = 15.75;
        } else if(castleFull.equals("Auckland")){
            priceTicket = 14.00;
        } else if(castleFull.equals("Barnard")){
            priceTicket = 8.10;
        } else {
            priceTicket = 14.10;
        }

        //Number of student
        int Number = Integer.parseInt(incomingIntent.getStringExtra("quantity"));

        //calculate total price
        Double totalPrice = (priceTicket+priceBus)*Number;

        //set text
        tv7.setText("Departure: " + departure);
        if(departure.equals("Eldon Square")){
            DepartAddress.setText("Postcode :  " + "NE1 7XW");
        }else{
            DepartAddress.setText("Postcode :  " + "NE1 7PF");
        }
        busPriceBlock.setText("Ticket Price: £ "+String.format("%.2f",priceBus));

        tv.setText(castleFull+" Castle");
        if(castleFull.equals("Alnwick")){
            CastleAddress.setText("Postcode :  "+"NE66 1NQ");
        }else if (castleFull.equals("Auckland")){
            CastleAddress.setText("Postcode :  "+"DL14 7NR");
        }else if (castleFull.equals("Barnard")){
            CastleAddress.setText("Postcode :  "+"DL12 8BH");
        }else{
            CastleAddress.setText("Postcode :  "+"NE69 7DF");
        }
        castlePriceBlock.setText("Ticket Price: £ "+ String.format("%.2f",priceTicket));

        tv2.setText(""+outDepart+"             "+outArrive);
        tv3.setText(""+retDepart+"               "+retArrive);
        priceTv.setText("£ " + String.format("%.2f",totalPrice)); //price
        tv5.setText("   " + Number + " x " + TicketType + " @ £" + String.format("%.2f",priceBus));
        tv6.setText("   " + Number + " x " + "Castle Ticket" + " @ £" + String.format("%.2f",priceTicket));

        //Extra intents to return to previous page if back button pressed
        String DayName = incomingIntent.getStringExtra("DayName");
        String currentDate = incomingIntent.getStringExtra("currentDate");
        int currentTime = incomingIntent.getIntExtra("currentTime",0);

        //set pay button
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
                String timeZone;

                int indexST = currentTime.toString().indexOf("mUseDst");
                String useDst = currentTime.toString().substring(indexST + 8,indexST+9);
                if(useDst.equals("t")){
                    timeZone = "BST";
                }else{
                    timeZone = "GMT";
                }

                String request = "{\"storeID\":\"Team05\", \"customerID\":\""+customerID+"\", \"date\":\""+searchedDate+"\", \"time\":\""+transactionTime+"\", \"timeZone\":\""+timeZone+"\", \"transactionAmount\":\""+totalPrice+"\", \"currencyCode\":\"GBP\"}";
                Log.d("Confirm",request);
                String[] data = new String[1];
                data[0] = request;
                new PaymentTask(ConfirmationPage.this).execute(data);

            }
        });

        //set back button
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmationPage.this, BookReturn.class);
                intent.putExtra("Castle", castleFull); //retains castle searched
                intent.putExtra("DayName", DayName); //retains day type
                intent.putExtra("searchedDate",searchedDate);
                intent.putExtra("quantity",Number);
                intent.putExtra("JourneyDetails", journeyOut);
                intent.putExtra("currentDate",currentDate);
                intent.putExtra("currentTime",currentTime);
                intent.putExtra("TicketType",TicketType);
                startActivity(intent);
            }
        });

    }

    //method to create random customer ID
    private String createID(){
        char[] chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        Random rnd = new Random();

        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 5; i++){
            id.append(chars[rnd.nextInt(chars.length)]);
        }
        return id.toString();
    }

    //set time as string
    private String setTimeFormat(String providedTime){

        if(providedTime.length()==3){
            providedTime = "0"+providedTime;
        }
        return providedTime.substring(0,2) + ":" + providedTime.substring(2,4);
    }

    //calculate total journey time
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

    //set popup window details
    private void popUp(Journey journey, View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        int legs = Integer.valueOf(journey.getLegs());

        // parameters for popup window
        int width = 1000;

        if (legs == 1) {
            popupView = inflater.inflate(R.layout.confirm_details_1_leg, null);
        }
        if (legs == 2) {
            popupView = inflater.inflate(R.layout.confirm_details_2_leg, null);
        }
        if (legs == 3) {
            popupView = inflater.inflate(R.layout.confirm_details_3_leg, null);
        }

        PopupWindow pw = new PopupWindow(popupView, width, (ViewGroup.LayoutParams.WRAP_CONTENT), true);
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
