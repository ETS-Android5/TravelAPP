/**
 * This is the page to view and select the outbound journey to the castle.
 *
 *
 * Changelog:
 * - OLI pulled in booking information from call
 * - OLI added calls to database. Currently not working. 03/04
 * - OLI Update, database call now fixed 04/04
 * - page now returns selected journeys to the screen and can be clicked on to take to next page
 *
 * created by Harry Akitt 16/03/2022
 * **/

package com.example.team05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BookOutbound extends AppCompatActivity {
    private final String TAG = "DayCheck";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    //method to change number value in database to money
    public String money(String s) {
        String str;
        str = "£" + s.charAt(0) + "." + s.charAt(1) + s.charAt(2);
        return str;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_outbound);
        Intent incomingIntent = getIntent();
        String castleName = incomingIntent.getStringExtra("Castle");
        String castleNameShortened = castleName.substring(0, castleName.length() - 7);
        int currentTime = incomingIntent.getIntExtra("CurrentTime", 0);

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(BookOutbound.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(BookOutbound.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(BookOutbound.this, More.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        // extra info button
        Button info_btn = (Button) findViewById(R.id.info_btn);


        //Defines list view
        ListView lv = (ListView) findViewById(R.id.lv1);

        //create array list
        ArrayList<Journey> list = new ArrayList<Journey>();

        // List Adapter for format
        JourneyAdapter adapter = new JourneyAdapter(this, R.layout.adapter, list);

        //gets day number from previous page.
        String dayName = incomingIntent.getStringExtra("DayName");
//        Log.d(TAG, String.valueOf(dayName));
//        Log.d(TAG, castleNameShortened);
        if (dayName.equals("Monday") || dayName.equals("Tuesday") || dayName.equals("Wednesday") || dayName.equals("Thursday") || dayName.equals("Friday") || dayName.equals("Weekday")) {
            dayName = "Weekday";
        }

        //Loads instance of FireBase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        //sort out issue of not showing times past current on any day
        String currentDate = incomingIntent.getStringExtra("currentDate"); //current date
        String searchedDate = incomingIntent.getStringExtra("selectedDate"); //searched date
        //Legacy collectionReference incase query type does not work
//        CollectionReference collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/OutboundBuses/");

        Query collectionReference = null;
        // Query collection reference that searches only for bus times after current time
        if (currentDate.equalsIgnoreCase(searchedDate)) { //only display times after current if the day searched is today
            collectionReference = fStore.collection("/DepartureDays/" + dayName + "/Castles/" + castleNameShortened + "/OutboundBuses/").whereGreaterThan("Leg1DepartureTime", currentTime);
        } else {
            collectionReference = fStore.collection("/DepartureDays/" + dayName + "/Castles/" + castleNameShortened + "/OutboundBuses/");
        }
        String finalDayName = dayName;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lv.setAdapter(adapter);

                        //legs as int for required logic
                        int legsNumber = Integer.parseInt(String.valueOf(document.getData().get("Legs")));

                        //return values from database as Strings
                        String leg = "Legs: " + String.valueOf(document.getData().get("Legs"));
                        String arrivalTime = null;

                        String departureT1 = String.valueOf(document.getData().get("Leg1DepartureTime"));
                        String arrivalT1 = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                        String departureT2 = null;
                        String arrivalT2 = null;
                        String departureT3 = null;
                        String arrivalT3 = null;

                        if (legsNumber > 1) {
                            departureT2 = String.valueOf(document.getData().get("Leg2DepartureTime"));
                            arrivalT2 = String.valueOf(document.getData().get("Leg2ArrivalTime"));
                            if (legsNumber > 2) {
                                departureT3 = String.valueOf(document.getData().get("Leg3DepartureTime"));
                                arrivalT3 = String.valueOf(document.getData().get("Leg3ArrivalTime"));
                            }
                        }
                        String price = money(String.valueOf(document.getData().get("TicketPrice")));

                        //cases for number of legs
                        if (legsNumber == 1) {
                            arrivalTime = arrivalT1;
                            list.add(new Journey(departureT1, arrivalT1, price, leg));
                        }
                        if (legsNumber == 2) {
                            arrivalTime = arrivalT2;
                            list.add(new Journey(departureT1, arrivalT1, departureT2, arrivalT2, price, leg));
                        }
                        if (legsNumber == 3) {
                            arrivalTime = arrivalT3;
                            list.add(new Journey(departureT1, arrivalT1, departureT2, arrivalT2, departureT3, arrivalT3, price, leg));
                        }

                        // create listener for when user clicks on desired trip
                        // retains info for next page
                        String finalArrive = arrivalTime;
                        String finalArrivalT1 = arrivalT1;
                        String finalDepartureT2 = departureT2;
                        String finalArrivalT2 = arrivalT2;
                        String finalDepartureT3 = departureT3;

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(BookOutbound.this, BookReturn.class);
                                intent.putExtra("departureT1", departureT1); //retains outbound depart time
                                intent.putExtra("finalArrive", finalArrive); //retains outbound arrive time
                                intent.putExtra("outPrice", price); //retains price
                                intent.putExtra("Castle", castleNameShortened); //retains castle searched
                                intent.putExtra("DayName", finalDayName); //retains day type


                                //retains additional information if multiple legs:
                                if (legsNumber > 1) {
                                    intent.putExtra("arrivalT1", finalArrivalT1);
                                    intent.putExtra("departureT2", finalDepartureT2);
                                    if (legsNumber > 2) {
                                        intent.putExtra("arrivalT2", finalArrivalT2);
                                        intent.putExtra("departureT3", finalDepartureT3);
                                    }
                                }

                                startActivity(intent);
                                finish();
                            }
                        });

                    }

                    // add error if no search results
                    if (list.isEmpty()) {
                        displayError();
                    }

                    //on unsuccessful pull
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }

    //error for no journeys
    public void displayError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("No further journeys on this day. Please select a different castle or day.");
        alertDialogBuilder.setTitle("Error - No Journeys");
        alertDialogBuilder.setNegativeButton("return", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(BookOutbound.this, Booking.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
