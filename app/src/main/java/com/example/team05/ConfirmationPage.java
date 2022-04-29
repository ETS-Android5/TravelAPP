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
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class ConfirmationPage extends AppCompatActivity {

    public String dayName;
    public String castleNameShortened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        TextView tv = (TextView) findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView2);
        TextView tv3 = (TextView) findViewById(R.id.textView3);
        TextView priceTv = (TextView) findViewById(R.id.price);
        Intent incomingIntent = getIntent();

        //ALL INCOMING INFO
        //SHARED
        String price = incomingIntent.getStringExtra("outPrice");
        String searchedDate = incomingIntent.getStringExtra("searchedDate");
        String castle = incomingIntent.getStringExtra("Castle");
        String dayName = incomingIntent.getStringExtra("DayName");

        //OUTBOUND
        //previous details selected from outbound journey
        String outDepartureT1 = incomingIntent.getStringExtra("outDepartureT1");
        String outFinalArrive = incomingIntent.getStringExtra("outFinalArrive");
        int outLegsNumber = incomingIntent.getIntExtra("outLegsNumber",1);

        String outArrivalT1 = null;
        String outDepartureT2 = null;
        String outArrivalT2 = null;
        String outDepartureT3 = null;
        //Additional details from multiple legs if necessary
        if (outLegsNumber > 1) {
            outArrivalT1 = incomingIntent.getStringExtra("outArrivalT1");
            outDepartureT2 = incomingIntent.getStringExtra("outDepartureT2");
            if (outLegsNumber > 2) {
                outArrivalT2 = incomingIntent.getStringExtra("outArrivalT2");
                outDepartureT3 = incomingIntent.getStringExtra("outDepartureT3");
            }
        }

        //RETURN
        String retDepartureT1 = incomingIntent.getStringExtra("retDepartureT1");
        String retFinalArrive = incomingIntent.getStringExtra("retFinalArrive");
        int retLegsNumber = incomingIntent.getIntExtra("retLegsNumber",1);

        String retArrivalT1 = null;
        String retDepartureT2 = null;
        String retArrivalT2 = null;
        String retDepartureT3 = null;
        //Additional details from multiple legs if necessary
        if (outLegsNumber > 1) {
            retArrivalT1 = incomingIntent.getStringExtra("retArrivalT1");
            retDepartureT2 = incomingIntent.getStringExtra("retDepartureT2");
            if (outLegsNumber > 2) {
                retArrivalT2 = incomingIntent.getStringExtra("retArrivalT2");
                retDepartureT3 = incomingIntent.getStringExtra("retDepartureT3");
            }
        }
        //END ALL INCOMING INFO




        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
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
        StringBuffer outDepart = new StringBuffer(outDepartureT1);
        outDepart.insert(2,":");
        StringBuffer outArrive = new StringBuffer(outFinalArrive);
        outArrive.insert(2,":");
        StringBuffer retDepart = new StringBuffer(retDepartureT1);
        retDepart.insert(2,":");
        StringBuffer retArrive = new StringBuffer(retFinalArrive);
        retArrive.insert(2,":");
        Double priceBus = Double.parseDouble(price.substring(1))
                + Double.parseDouble(price.substring(1));

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
        tv.setText("Destination: "+castle+" Castle");
        tv2.setText("Outbound: "+outDepart+" ->"+outArrive);
        tv3.setText("Return: "+retDepart+" ->"+retArrive);
        priceTv.setText("Price: £" + String.format("%.2f",totalPrice));


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(ConfirmationPage.this,BookReturn.class);
//        intent.putExtra("Castle", (castleNameShortened));
//        intent.putExtra("DayName", dayName);
//        startActivity(intent);
//        finish();
//    }

}