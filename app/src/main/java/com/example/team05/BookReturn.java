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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
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
        String outDepartureT1 = incomingIntent.getStringExtra("departureT1");
        String outFinalArrive = incomingIntent.getStringExtra("finalArrive");
        String price = incomingIntent.getStringExtra("outPrice");
        String searchedDate = incomingIntent.getStringExtra("searchedDate");
        int outLegsNumber = incomingIntent.getIntExtra("legsNumber",1);
        String quantity = incomingIntent.getStringExtra("quantity");

        String outArrivalT1 = null;
        String outDepartureT2 = null;
        String outArrivalT2 = null;
        String outDepartureT3 = null;
        //Additional details from multiple legs if necessary
        if (outLegsNumber > 1) {
             outArrivalT1 = incomingIntent.getStringExtra("arrivalT1");
             outDepartureT2 = incomingIntent.getStringExtra("departureT2");
            if (outLegsNumber > 2) {
                 outArrivalT2 = incomingIntent.getStringExtra("arrivalT2");
                 outDepartureT3 = incomingIntent.getStringExtra("departureT3");
            }
        }

//        CollectionReference collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/");
        Query collectionReference = fStore.collection("/DepartureDays/"+dayName+"/Castles/"+castleNameShortened+"/ReturnBuses/").whereGreaterThan("Leg1DepartureTime",((Integer.parseInt(outFinalArrive)))+200);
        String finalOutArrivalT1 = outArrivalT1;
        String finalOutDepartureT2 = outDepartureT2;
        String finalOutArrivalT = outArrivalT2;
        String finalOutDepartureT = outDepartureT3;
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        lv.setAdapter(adapter);

                        ///////
                        //legs as int for required logic
                        int retLegsNumber = Integer.parseInt(String.valueOf(document.getData().get("Legs")));
//
//                        //return values from database as Strings, to be passed to intent
                        String retLegsInfo = "Legs: " + String.valueOf(document.getData().get("Legs"));
                        String retArrivalTime = null;
                        String retDepartureT1 = String.valueOf(document.getData().get("Leg1DepartureTime"));
                        String retArrivalT1 = String.valueOf(document.getData().get("Leg1ArrivalTime"));
                        String retDepartureT2 = null;
                        String retArrivalT2 = null;
                        String retDepartureT3 = null;
                        String retArrivalT3 = null;

//                        //returns additional info when multiple legs
                        if (retLegsNumber > 1) {
                            retDepartureT2 = String.valueOf(document.getData().get("Leg2DepartureTime"));
                            retArrivalT2 = String.valueOf(document.getData().get("Leg2ArrivalTime"));
                            if (retLegsNumber > 2) {
                                retDepartureT3 = String.valueOf(document.getData().get("Leg3DepartureTime"));
                                retArrivalT3 = String.valueOf(document.getData().get("Leg3ArrivalTime"));
                            }
                        }

                        //Creates journey objects to be used in list
                        if (retLegsNumber == 1) {
                            retArrivalTime = retArrivalT1;
                            list.add(new Journey(retDepartureT1, retArrivalT1, price, retLegsInfo));
                        }
                        if (retLegsNumber == 2) {
                            retArrivalTime = retArrivalT2;
                            list.add(new Journey(retDepartureT1, retArrivalT1, retDepartureT2, retArrivalT2, price, retLegsInfo));
                        }
                        if (retLegsNumber == 3) {
                            retArrivalTime = retArrivalT3;
                            list.add(new Journey(retDepartureT1, retArrivalT1, retDepartureT2, retArrivalT2, retDepartureT3, retArrivalT3, price, retLegsInfo));
                        }


                        // create listener for when user clicks on desired trip
                        // retains info for next page
                        String finalRetarrive = retArrivalTime;
                        String finalRetArrivalTime = retArrivalTime;
                        String finalRetDepartureT2 = retDepartureT2;
                        String finalRetArrivalT2 = retArrivalT2;
                        String finalRetDepartureT3 = retDepartureT3;
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                Bundle bd = new Bundle();
                                Intent intent = new Intent(BookReturn.this, ConfirmationPage.class);



                                //shared
                                intent.putExtra("outPrice", price); //retains price
                                intent.putExtra("Castle", castleNameShortened); //retains castle searched
                                intent.putExtra("DayName", dayName); //retains day type
                                intent.putExtra("searchedDate",searchedDate);
                                intent.putExtra("quantity",quantity);

                                //outbound
                                intent.putExtra("outLegsNumber",outLegsNumber); //retains no. legs
                                intent.putExtra("outDepartureT1", outDepartureT1); //retains outbound depart time
                                intent.putExtra("outFinalArrive", outFinalArrive); //retains outbound arrive time

                                    //retains additional information if multiple legs:
                                if (outLegsNumber > 1) {
                                    intent.putExtra("outArrivalT1", finalOutArrivalT1);
                                    intent.putExtra("outDepartureT2", finalOutDepartureT2);
                                    if (outLegsNumber > 2) {
                                        intent.putExtra("outArrivalT2", finalOutArrivalT);
                                        intent.putExtra("outDepartureT3", finalOutDepartureT);
                                    }
                                }

                                //return
                                intent.putExtra("retLegsNumber",retLegsNumber);
                                intent.putExtra("retDepartureT1", retDepartureT1); //retains outbound depart time
                                intent.putExtra("retFinalArrive", finalRetArrivalTime); //retains outbound arrive time

                                    //retains additional information if multiple legs:
                                if (outLegsNumber > 1) {
                                    intent.putExtra("retArrivalT1", retArrivalT1);
                                    intent.putExtra("retDepartureT2", finalRetDepartureT2);
                                    if (outLegsNumber > 2) {
                                        intent.putExtra("retArrivalT2", finalRetArrivalT2);
                                        intent.putExtra("retDepartureT3", finalRetDepartureT3);
                                    }
                                }

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
