/**
 ***** Description *****
 * This is the page to view and select the return journey to the castle.
 *
 ***** Key Functionality *****
 * -Retrieve information about return journey times from database
 * -Display journey times to user
 * -Pass selected journey as intent to booking confirmation
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
 * -page now returns selected journeys to the screen and can be clicked on to take to next page
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


public class BookReturn extends AppCompatActivity {
    private final String TAG = "ReturnCheck";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private View popupView;
    public String dayName;
    public String castleNameShortened;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_outbound);
        Intent incomingIntent = getIntent();

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
                        Intent intent3 = new Intent(BookReturn.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(BookReturn.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(BookReturn.this, ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        //set title
        TextView title = (TextView) findViewById(R.id.book_title);
        title.setText("Return");

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
        String searchedDate = incomingIntent.getStringExtra("searchedDate");
        String currentDate = incomingIntent.getStringExtra("currentDate");
        int currentTime = incomingIntent.getIntExtra("currentTime",0);
        String DayName = incomingIntent.getStringExtra("DayName");
        String TicketType = incomingIntent.getStringExtra("TicketType");


        //get Journey object intent
        Journey journeyOut = (Journey )incomingIntent.getSerializableExtra("JourneyDetails");
        String quantity = incomingIntent.getStringExtra("quantity");

        //set back button
        Button back_btn = (Button) findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookReturn.this,BookOutbound.class);
                intent.putExtra("Castle", (castleNameShortened + " Castle"));
                intent.putExtra("DayName",dayName);
                intent.putExtra("CurrentTime",currentTime);
                intent.putExtra("currentDate", currentDate);
                intent.putExtra("selectedDate", searchedDate);
                intent.putExtra("quantity",quantity);
                startActivity(intent);
            }
        });

//        CollectionReference collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/");
        Query collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/").whereGreaterThan("Leg1DepartureTime",((Integer.parseInt(journeyOut.getArrivalT())))+200);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lv.setAdapter(adapter);

                        //legs as int for required logic
                        int retLegsNumber = Integer.parseInt(String.valueOf(document.getData().get("Legs")));

                        //return values from database as Strings, to be passed to intent
                        String retLegsInfo = String.valueOf(document.getData().get("Legs"));
                        String retDepartureT1 = String.valueOf(document.getData().get("Leg1DepartureTime"));
                        String retArrivalT1 = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                        String retDepartureT2 = String.valueOf(document.getData().get("Leg2DepartureTime"));
                        String retArrivalT2 = String.valueOf(document.getData().get("Leg2ArrivalTime"));
                        String retDepartureT3 = String.valueOf(document.getData().get("Leg3DepartureTime"));
                        String retArrivalT3 = String.valueOf(document.getData().get("Leg3ArrivalTime"));
                        String op1ret = String.valueOf(document.getData().get("Leg1Operator"));
                        String op2ret = String.valueOf(document.getData().get("Leg2Operator"));
                        String op3ret = String.valueOf(document.getData().get("Leg3Operator"));
                        String bus1ret = String.valueOf(document.getData().get("Leg1Bus"));
                        String bus2ret = String.valueOf(document.getData().get("Leg2Bus"));
                        String bus3ret = String.valueOf(document.getData().get("Leg3Bus"));
                        String departureStation1Ret = String.valueOf(document.getData().get("Leg1DepartureStation"));
                        String departureStation2Ret = String.valueOf(document.getData().get("Leg2DepartureStation"));
                        String departureStation3Ret = String.valueOf(document.getData().get("Leg3DepartureStation"));
                        String arrivalStation1Ret = String.valueOf(document.getData().get("Leg1ArrivalStation"));
                        String arrivalStation2Ret = String.valueOf(document.getData().get("Leg2ArrivalStation"));
                        String arrivalStation3Ret = String.valueOf(document.getData().get("Leg3ArrivalStation"));
                        String price = journeyOut.getPrice();

                        //Creates journey objects to be used in list
                        if (retLegsNumber == 1) {
                            list.add(new Journey(retDepartureT1, retArrivalT1, price, retLegsInfo, op1ret, bus1ret, departureStation1Ret, arrivalStation1Ret));
                        }
                        if (retLegsNumber == 2) {
                            list.add(new Journey(retDepartureT1, retArrivalT1, retDepartureT2, retArrivalT2, price, retLegsInfo, op1ret, bus1ret, op2ret, bus2ret, departureStation1Ret, arrivalStation1Ret, departureStation2Ret, arrivalStation2Ret));
                        }
                        if (retLegsNumber == 3) {
                            list.add(new Journey(retDepartureT1, retArrivalT1, retDepartureT2, retArrivalT2, retDepartureT3, retArrivalT3, price, retLegsInfo, op1ret, bus1ret, op2ret, bus2ret, op3ret, bus3ret, departureStation1Ret, arrivalStation1Ret, departureStation2Ret, arrivalStation2Ret, departureStation3Ret, arrivalStation3Ret));
                        }


                        // create listener for when user clicks on desired trip
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Journey journeyRet = (Journey) adapterView.getItemAtPosition(i);
                                int retLegsNumber = Integer.valueOf(journeyRet.getLegs());

                                // inflate the layout of the popup window
                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                                // width for popup window
                                int width = 1000;

                                if (retLegsNumber == 1) {
                                    popupView = inflater.inflate(R.layout.journey_details_1_leg, null);
                                }
                                if (retLegsNumber == 2) {
                                    popupView = inflater.inflate(R.layout.journey_details_2_leg, null);
                                }
                                if (retLegsNumber == 3) {
                                    popupView = inflater.inflate(R.layout.journey_details_3_leg, null);
                                }

                                //extra parameters for popup
                                PopupWindow pw = new PopupWindow(popupView, width, (ViewGroup.LayoutParams.WRAP_CONTENT), true);
                                pw.showAtLocation(view, Gravity.CENTER, 0, 0);

                                //leg 1 text views
                                TextView tv_op1 = pw.getContentView().findViewById(R.id.leg1_operator);
                                tv_op1.setText(journeyRet.getOperator1() + " " + journeyRet.getBus1());
                                TextView leg1_start = pw.getContentView().findViewById(R.id.leg1_start);
                                leg1_start.setText("Departure: " + setTimeFormat(journeyRet.getDepartureT1()) + " from " + journeyRet.getDepartureStation1());
                                TextView leg1_end = pw.getContentView().findViewById(R.id.leg1_end);
                                leg1_end.setText("Arrival Time: " + setTimeFormat(journeyRet.getArrivalT1()) + " at " + journeyRet.getArrivalStation1());
                                TextView leg1_time = pw.getContentView().findViewById(R.id.leg1_time);
                                String leg1_total = setJourneyTime(setTimeFormat(journeyRet.getArrivalT1()),setTimeFormat(journeyRet.getDepartureT1()));
                                leg1_time.setText(leg1_total);

                                //leg 2 text views
                                if (retLegsNumber > 1) {
                                    TextView tv_op2 = pw.getContentView().findViewById(R.id.leg2_operator);
                                    tv_op2.setText(journeyRet.getOperator2() + " " + journeyRet.getBus2());
                                    TextView leg2_end = pw.getContentView().findViewById(R.id.leg2_end);
                                    leg2_end.setText("Arrival Time: " + setTimeFormat(journeyRet.getArrivalT2()) + " at " + journeyRet.getArrivalStation2());
                                    TextView leg2_info = pw.getContentView().findViewById(R.id.leg2_info);
                                    leg2_info.setText("Departure: " + setTimeFormat(journeyRet.getDepartureT2()) + " from " + journeyRet.getDepartureStation2());
                                    TextView leg2_time = pw.getContentView().findViewById(R.id.leg2_time);
                                    String leg2_total = setJourneyTime(setTimeFormat(journeyRet.getArrivalT2()),setTimeFormat(journeyRet.getDepartureT2()));
                                    leg2_time.setText(leg2_total);
                                }

                                //leg 3 text views
                                if (retLegsNumber > 2) {
                                    TextView tv_op3 = pw.getContentView().findViewById(R.id.leg3_operator);
                                    tv_op3.setText(journeyRet.getOperator3() + " " + journeyRet.getBus3());
                                    TextView leg3_end = pw.getContentView().findViewById(R.id.leg3_end);
                                    leg3_end.setText("Arrival Time: " + setTimeFormat(journeyRet.getArrivalT3()) + " at " + journeyRet.getArrivalStation3());
                                    TextView leg3_info = pw.getContentView().findViewById(R.id.leg3_info);
                                    leg3_info.setText("Departure: " + setTimeFormat(journeyRet.getDepartureT3()) + " from " + journeyRet.getDepartureStation3());
                                    TextView leg3_time = pw.getContentView().findViewById(R.id.leg3_time);
                                    String leg3_total = setJourneyTime(setTimeFormat(journeyRet.getArrivalT3()),setTimeFormat(journeyRet.getDepartureT3()));
                                    leg3_time.setText(leg3_total);
                                }

                                //set leg total time values
                                TextView total_time = pw.getContentView().findViewById(R.id.total_time);
                                total_time.setText("Total Time: " + setJourneyTime(setTimeFormat(journeyRet.getArrivalT()),setTimeFormat(journeyRet.getDepartureT1())));


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
                                        Intent intent = new Intent(BookReturn.this, ConfirmationPage.class);
                                        //shared
                                        intent.putExtra("outPrice", price); //retains price
                                        intent.putExtra("Castle", castleNameShortened); //retains castle searched
                                        intent.putExtra("DayName", dayName); //retains day type
                                        intent.putExtra("searchedDate", searchedDate);
                                        intent.putExtra("quantity", quantity);
                                        intent.putExtra("RetJourney", journeyRet);
                                        intent.putExtra("OutJourney",journeyOut);
                                        intent.putExtra("TicketType",TicketType);
                                        intent.putExtra("currentDate",currentDate);
                                        intent.putExtra("currentTime",currentTime);
                                        intent.putExtra("DayName",DayName);


                                        startActivity(intent);
                                    }


                                });
                            }
                        });
                    }
                } else {
                    databaseError();
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

    //format time string
    private String setTimeFormat(String providedTime){

        if(providedTime.length()==3){
            providedTime = "0"+providedTime;
        }
        return providedTime.substring(0,2) + ":" + providedTime.substring(2,4);
    }

    //calculate time of journey
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
                Intent intent = new Intent(BookReturn.this, Booking.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}