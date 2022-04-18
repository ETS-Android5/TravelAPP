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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class ConfirmationPage extends AppCompatActivity {

    public String dayName;
    public String castleNameShortened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        Intent incomingIntent = getIntent();
        TextView tv = (TextView) findViewById(R.id.textView);
        Bundle bd = incomingIntent.getExtras();

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
        String castleName = bd.getCharSequence("CastleName").toString();
        String outDepart = incomingIntent.getStringExtra("outDepart");
        String outArrive = incomingIntent.getStringExtra("outArrive");;
        String retDepart = incomingIntent.getStringExtra("retDepart");;
        String retArrive = incomingIntent.getStringExtra("retArrive");;
        String price = incomingIntent.getStringExtra("Price");

        //intent in-case requiring new search
        String dayName = incomingIntent.getStringExtra("dayName");

        String str = "Your trip to " + castleName +"\n"
                +"Outbound: " + outDepart + outArrive +"\n"
                + "Return: " + retDepart + retArrive +"\n"
                + "Price " + price+ "\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        tv.setText(stringBuilder.toString());


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