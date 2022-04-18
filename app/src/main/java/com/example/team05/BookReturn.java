/**
 * This is the page to view and select the return journey to the castle.
 *
 *
 * Changelog:
 * - page now returns selected journeys to the screen and can be clicked on to take to next page
 *
 * created by Harry Akitt 16/03/2022
 * **/

package com.example.team05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class BookReturn extends AppCompatActivity {
    private final String TAG = "ReturnCheck";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    public String dayName;
    public String castleNameShortened;

    //method to change number value in database to money
    public String money(String s){
        String str;
        str = "£" + s.charAt(0) + "." + s.charAt(1) + s.charAt(2);
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_outbound);
        Intent incomingIntent = getIntent();

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(BookReturn.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(BookReturn.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(BookReturn.this, More.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        //Defines list view
        ListView lv = (ListView) findViewById(R.id.lv1);
        ArrayList<Journey> list= new ArrayList<Journey>();

        // List Adapter for format
        JourneyAdapter adapter = new JourneyAdapter(this, R.layout.adapter,list);
        lv.setAdapter(adapter);

        //Loads instance of FireBase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //day and castle intents from search
        dayName = incomingIntent.getStringExtra("DayName");
        castleNameShortened = incomingIntent.getStringExtra("Castle");

        //previous details selected from outbound journey
        String outDepart = incomingIntent.getStringExtra("departureT1");
        String outArrive = incomingIntent.getStringExtra("finalArrive");

//        CollectionReference collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/");
        Query collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/").whereGreaterThan("Leg1DepartureTime",((Integer.parseInt(outArrive)))+200);
        String finalDayName = dayName;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lv.setAdapter(adapter);

                        //return values from database as Strings
                        String retdepart = String.valueOf(document.getData().get("Leg1DepartureTime")) + "                   --->";
                        String retarrive = "";
                        String price = incomingIntent.getStringExtra("outPrice");
                        String leg = "Legs: " + String.valueOf(document.getData().get("Legs"));

                        //cases for number of legs
                        if (leg.equalsIgnoreCase("Legs: 1")){
                            retarrive = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                            retarrive = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                            list.add(new Journey(retdepart, retarrive,price,leg));
                        }
                        if (leg.equalsIgnoreCase("Legs: 2")){
                            retarrive = String.valueOf(document.getData().get("Leg2ArrivalTime"));
                            list.add(new Journey(retdepart, retarrive,price,leg));
                        }
                        if (leg.equalsIgnoreCase("Legs: 3")){
                            retarrive = String.valueOf(document.getData().get("Leg3ArrivalTime"));
                            list.add(new Journey(retdepart, retarrive,price,leg));
                        }

                        list.add(new Journey(retdepart,retarrive,price,leg));

                        // create listener for when user clicks on desired trip
                        // retains info for next page
                        String finalRetarrive = retarrive;
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(BookReturn.this, ConfirmationPage.class);
                                intent.putExtra("retDepart",retdepart); //retains return depart time
                                intent.putExtra("retArrive", finalRetarrive); //retains return arrive time
                                intent.putExtra("outDepart",outDepart); //retains return depart time
                                intent.putExtra("outArrive",outArrive); //retains return arrive time
                                intent.putExtra("Price",price); //retains price
                                intent.putExtra("Castle",castleNameShortened); //retains castle searched
                                intent.putExtra("dayName",dayName);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


//    https://stackoverflow.com/questions/39532594/android-onbackpressed-is-not-being-called
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"this one Pressed");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //allows back button to return information to BookOutbound
    @Override
    public void onBackPressed() {
        Log.d(TAG,"back Pressed");
        super.onBackPressed();
        Intent intent = new Intent(BookReturn.this,BookOutbound.class);
        intent.putExtra("Castle", (castleNameShortened+" Castle"));
        intent.putExtra("DayName", dayName);
        startActivity(intent);
        finish();
    }

}
