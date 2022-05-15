/**
 ***** Description *****
 * This is the page to view and select the outbound journey to the castle.
 *
 ***** Key Functionality *****
 * -Retrieve information about outbound journey times from database
 * -Display journey times to user
 * -Pass selected journey as intent to return journey
 *
 ***** Author(s)  *****
 * Harry Akitt (Created 16/03/22)
 * -Displaying journey times
 * -Passing information to intent via Journey object
 * -Error handling on no database results
 * - UI
 * Oli Presland
 * -Retrieving journey information from database
 *
 ***** Changelog: *****
 * - OLI pulled in booking information from call
 * - OLI added calls to database. Currently not working. 03/04
 * - OLI Update, database call now fixed 04/04
 * - page now returns selected journeys to the screen and can be clicked on to take to next page
 * **/

package com.example.team05;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;


public class BookOutbound extends AppCompatActivity {
    private final String TAG = "DayCheck";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private View popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_outbound);
        Intent incomingIntent = getIntent();

        //hide action bar
        ActionBar bar = getSupportActionBar();
        bar.hide();

        //set title
        TextView title = (TextView) findViewById(R.id.book_title);
        title.setText("Outbound");

        //set back button
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookOutbound.this,Booking.class);
                startActivity(intent);
            }
        });

        //All incoming intents
        String castleName = incomingIntent.getStringExtra("Castle"); //castle name
        String quantity = incomingIntent.getStringExtra("quantity"); //quantity
        int currentTime = incomingIntent.getIntExtra("CurrentTime", 0); //current time
        String dayName = incomingIntent.getStringExtra("DayName"); //day of week
        String currentDate = incomingIntent.getStringExtra("currentDate"); //current
        String searchedDate = incomingIntent.getStringExtra("selectedDate"); //searched

        //turns dayName into dayType (Weekday/Saturday/Sunday)
        if (dayName.equals("Monday") || dayName.equals("Tuesday") || dayName.equals("Wednesday") || dayName.equals("Thursday") || dayName.equals("Friday") || dayName.equals("Weekday")) {
            dayName = "Weekday";
        }
        //finalDayName required for intent
        String finalDayName = dayName;

        //turns castleName into castleName without 'Castle'
        String castleNameShortened = castleName.substring(0, castleName.length() - 7);

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.getMenu().setGroupCheckable(0,false,true);
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
                        Intent intent2 = new Intent(BookOutbound.this, ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        //Defines list view
        ListView lv = (ListView) findViewById(R.id.lv1);

        //create array list
        ArrayList<Journey> list = new ArrayList<Journey>();

        // List Adapter for format
        JourneyAdapter adapter = new JourneyAdapter(this, R.layout.adapter, list);

        //Loads instance of FireBase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Conditional collection reference based on if selected date is current date
        Query collectionReference = null;
        if (currentDate.equalsIgnoreCase(searchedDate)) { //only display times after current if the day searched is today
            collectionReference = fStore.collection("/DepartureDays/" + dayName + "/Castles/" + castleNameShortened + "/OutboundBuses/").whereGreaterThan("Leg1DepartureTime", currentTime);
        } else {
            collectionReference = fStore.collection("/DepartureDays/" + dayName + "/Castles/" + castleNameShortened + "/OutboundBuses/");
        }

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().isEmpty()) {
                        displayError();
                    }

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lv.setAdapter(adapter);




                        //legs as int for required logic
                        int legsNumber = Integer.parseInt(String.valueOf(document.getData().get("Legs")));


                        //return values from database as Strings, to be passed to intent
                        String LegsInfo = "Legs: " + String.valueOf(document.getData().get("Legs"));
                        String legs = String.valueOf(document.getData().get("Legs"));
                        String departureT1 = String.valueOf(document.getData().get("Leg1DepartureTime"));
                        String arrivalT1 = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                        String price = money(String.valueOf(document.getData().get("TicketPrice")));
                        String op1 = String.valueOf(document.getData().get("Leg1Operator"));
                        String bus1 = String.valueOf(document.getData().get("Leg1Bus"));
                        String departureStation = String.valueOf(document.getData().get("Leg1DepartureStation"));
                        String arrivalStation = String.valueOf(document.getData().get("Leg1ArrivalStation"));
                        String departureT2 = String.valueOf(document.getData().get("Leg2DepartureTime"));
                        String arrivalT2 = String.valueOf(document.getData().get("Leg2ArrivalTime"));
                        String op2 = String.valueOf(document.getData().get("Leg2Operator"));
                        String bus2 = String.valueOf(document.getData().get("Leg2Bus"));
                        String departureStation2 = String.valueOf(document.getData().get("Leg2DepartureStation"));
                        String arrivalStation2 = String.valueOf(document.getData().get("Leg2ArrivalStation"));
                        String departureT3 = String.valueOf(document.getData().get("Leg3DepartureTime"));
                        String arrivalT3 = String.valueOf(document.getData().get("Leg3ArrivalTime"));
                        String op3 = String.valueOf(document.getData().get("Leg3Operator"));
                        String bus3 = String.valueOf(document.getData().get("Leg3Bus"));
                        String departureStation3 = String.valueOf(document.getData().get("Leg3DepartureStation"));
                        String arrivalStation3 = String.valueOf(document.getData().get("Leg3ArrivalStation"));
                        String ticketType = String.valueOf(document.getData().get("TicketType"));

                        //Creates journey objects to be used in list
                        if (legsNumber == 1) {
                            list.add(new Journey(departureT1, arrivalT1, price, legs, op1, bus1, departureStation, arrivalStation));
                        }
                        if (legsNumber == 2) {
                            list.add(new Journey(departureT1, arrivalT1, departureT2, arrivalT2, price, legs, op1, bus1, op2, bus2, departureStation, arrivalStation, departureStation2, arrivalStation2));
                        }
                        if (legsNumber == 3) {
                            list.add(new Journey(departureT1, arrivalT1, departureT2, arrivalT2, departureT3, arrivalT3, price, legs, op1, bus1, op2, bus2, op3, bus3, departureStation, arrivalStation, departureStation2, arrivalStation2, departureStation3, arrivalStation3));
                        }


                        //listener for when user clicks on journey
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                //return selected journey
                                Journey journey = (Journey) adapterView.getItemAtPosition(i);
                                int legsNumber = Integer.valueOf(journey.getLegs());

                                // inflate the layout of the popup window
                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                                // width for popup window
                                int width = 1000;

                                //inflate layout for popup
                                if (legsNumber == 1) {
                                    popupView = inflater.inflate(R.layout.journey_details_1_leg, null);
                                }
                                if (legsNumber == 2) {
                                    popupView = inflater.inflate(R.layout.journey_details_2_leg, null);
                                }
                                if (legsNumber == 3) {
                                    popupView = inflater.inflate(R.layout.journey_details_3_leg, null);
                                }

                                //popup location and parameters
                                PopupWindow pw = new PopupWindow(popupView, width, (ViewGroup.LayoutParams.WRAP_CONTENT), true);
                                pw.showAtLocation(view, Gravity.CENTER, 0, 0);
                                pw .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

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

                                if (legsNumber > 1) {
                                    //leg 2 text views
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

                                if (legsNumber>2) {
                                    // leg 3 text views
                                    TextView tv_op3 = pw.getContentView().findViewById(R.id.leg3_operator);
                                    tv_op3.setText(journey.getOperator3() + " " + journey.getBus3());
                                    TextView leg3_info = pw.getContentView().findViewById(R.id.leg3_info);
                                    leg3_info.setText("Departure: " + setTimeFormat(journey.getDepartureT3()) + " from " + journey.getDepartureStation3());
                                    TextView leg3_end = pw.getContentView().findViewById(R.id.leg3_end);
                                    leg3_end.setText("Arrival Time: " + setTimeFormat(journey.getArrivalT3()) + " at " + journey.getArrivalStation3());
                                    TextView leg3_time = pw.getContentView().findViewById(R.id.leg3_time);
                                    String leg3_total = setJourneyTime(setTimeFormat(journey.getArrivalT3()), setTimeFormat(journey.getDepartureT3()));
                                    leg3_time.setText(leg3_total);
                                }

                                //set leg total time values
                                    TextView total_time = pw.getContentView().findViewById(R.id.total_time);
                                    total_time.setText("Total Time: " + setJourneyTime(setTimeFormat(journey.getArrivalT()),setTimeFormat(journey.getDepartureT1())));


                                // set return button
                                pw.getContentView().findViewById(R.id.return_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        pw.dismiss();
                                    }
                                });

                                // set book button
                                pw.getContentView().findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(BookOutbound.this, BookReturn.class);
                                        intent.putExtra("Castle", castleNameShortened); //retains castle searched
                                        intent.putExtra("DayName", finalDayName); //retains day type
                                        intent.putExtra("searchedDate",searchedDate);
                                        intent.putExtra("quantity",quantity);
                                        intent.putExtra("JourneyDetails", journey);
                                        intent.putExtra("currentDate",currentDate);
                                        intent.putExtra("currentTime",currentTime);
                                        intent.putExtra("TicketType",ticketType);


                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        });

                    //on unsuccessful pull
                }} else {
                    databaseError();
                }
            }
        });


    }

    //method to change number value in database to money
    public String money(String s) {
        String str = null;
        if (s.length() ==3) {
            str = "£" + s.charAt(0) + "." + s.charAt(1) + s.charAt(2);
        }
        if (s.length() ==4){
            str = "£" + s.charAt(0) + s.charAt(1) + "." + s.charAt(2) + s.charAt(3);
        }
        return str;
    }

    //error for no journeys
    public void displayError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        Log.d(TAG,"Called123");
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

    //turns time string into correct format
    private String setTimeFormat(String providedTime){

        if(providedTime.length()==3){
            providedTime = "0"+providedTime;
        }
        return providedTime.substring(0,2) + ":" + providedTime.substring(2,4);

    }

    //calculates time for a journey
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

    //error for no journeys
    public void databaseError() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Error connecting to the database - please check your internet connection and try again");
        alertDialogBuilder.setTitle("Error - No Connection");
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
