/**
 ***** Description: *****
 * This is the booking page the user will be sent to when they click on any of the book buttons
 *
 ***** Key functionalities: *****
 * -Functionalities include three spinners to select castle, quantity of tickets and date.
 * -There is also a search button that takes the user to the BookOutbound page to select their
 * bus time options
 * -The castle spinner will autofill will a selection if the user has accessed the page via
 * the information page of a specific castle.
 *
 ***** Author(s): *****
 * Harry Akitt
 * -Created 16/03/2022
 * -Key functionality
 * Oli Presland
 * -Bug fixes
 * Ruipeng Jiao
 * -Add Image display
 *
 ***** References: *****
 * JavaPoint - Alert Dialog (https://www.javatpoint.com/android-alert-dialog-example)
 *
 ***** TO DO:  *****
 * - DENNIS TO COMPLETE UI
 *
 ***** Changelog:  *****
 * - OLI fixed bug where date did not correctly identify day of week
 * - OLI updated date display for current date
 * - OLI error handling for if date is in past.
 * - Ruipeng (Dennis) completes Navmore button function the page jump
 * - Harry add intents to BookOutbound
 * - Harry error for castle closed
 * - Ruipeng add a function of change image
 *
 * **/

package com.example.team05;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Booking extends AppCompatActivity {
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Calendar cal = Calendar.getInstance();
    private final String TAG = "DebuggingDayOfWeek";
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");

    //Alert dialog for if date is before current time
    AlertDialog.Builder builder;

    //making variable dayOfWeek to be passed to outbound bookings
    String selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        //Sets display date to current date
        displayDate = (TextView) findViewById(R.id.date_input);
        resetDate();

        //Setting up error message if day before current time
        builder = new AlertDialog.Builder(this);

        //Getting values for currentTime and currentDate. These are passed to intent
        int currentTime =Integer.parseInt(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))
                +String.valueOf(cal.get(Calendar.MINUTE)));

        //change format for comparison
        String currentDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if (currentDay.length() ==1){
            currentDay = "0" + currentDay;
        }
        String currentMon =String.valueOf(cal.get(Calendar.MONTH)+1);
        if (currentMon.length() ==1){
            currentMon = "0" + currentMon;
        }
        String currentYear= String.valueOf(cal.get(Calendar.YEAR));
        String currentDate = currentDay+"/"+ currentMon + "/"
                + currentYear;

        // set action bar
        ActionBar bar = getSupportActionBar();
        bar.hide();

        //region //create spinner with ticket quantities
        //value must be between 1 and 5
        Spinner numberSpinner = (Spinner) findViewById(R.id.quantity);
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        ArrayAdapter<Integer> numAdapter = new ArrayAdapter<>(Booking.this,
                android.R.layout.simple_list_item_1, numbers);
        numberSpinner.setAdapter(numAdapter);


        //create spinner with list of castles
        //default will be Alnwick, unless accessed through a specific castle page
        Spinner mySpinner = (Spinner) findViewById(R.id.castleList);
        ArrayList<String> CastleName = new ArrayList<>();
        CastleName.add("Alnwick Castle");
        CastleName.add("Auckland Castle");
        CastleName.add("Bamburgh Castle");
        CastleName.add("Barnard Castle");

        //list adapter for castle spinner
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_list_item_1, CastleName);
        mySpinner.setAdapter(myAdapter);

        // get intent from specific castle page to autofill chosen castle
        Intent incomingIntent = getIntent();
        String castleName = incomingIntent.getStringExtra("castle");
        int spinnerPosition = myAdapter.getPosition(castleName);
        mySpinner.setSelection(spinnerPosition);

        // set spinner to chose date
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Booking.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //date listener. Checks if before or after current date and time
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String monthNew = null;
                String dayNew = null;

                if(day<10){
                    dayNew = "0"+day;
                }else{
                    dayNew = String.valueOf(day);
                }

                if(month<10){
                    monthNew = "0"+month;
                }else{
                    monthNew = String.valueOf(month);
                }
                String date = dayNew + "/"+monthNew+"/" + year;

                Log.d(TAG, String.valueOf(day));

                //Corrects problem with finding day
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Calendar calendar = new GregorianCalendar(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),0,0,0);
                selectedDay = sdf.format(calendar.getTime());

                calendar.add(Calendar.DATE,+1);
                if(calendar.before(cal)){
                    Log.d(TAG,"Before");
                    displayError();
                }else{
                    Log.d(TAG, "After");
                }
                displayDate.setText(date);
            }
        };


        // Listen the choice of castle and change images
        ImageView imageview=(ImageView) findViewById(R.id.imageView);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String castleImgName=adapterView.getItemAtPosition(i).toString();
                if(castleImgName.equals("Alnwick Castle")){
                    imageview.setImageResource(R.drawable.home_alnwick);
                }else if(castleImgName.equals("Auckland Castle")){
                    imageview.setImageResource(R.drawable.home_auckland);
                }else if(castleImgName.equals("Bamburgh Castle")){
                    imageview.setImageResource(R.drawable.home_bamburgh);
                }else if(castleImgName.equals("Barnard Castle")){
                    imageview.setImageResource(R.drawable.home_barnard);
                }else{
                    return;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // search button
        // passes the chosen castle and day of week from the spinners to be searched in the
        // database
        Button searchBtn = (Button) findViewById(R.id.search_button);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Booking.this, BookOutbound.class);
                Spinner spinner = (Spinner) findViewById(R.id.castleList);
                String text = spinner.getSelectedItem().toString();
                Spinner spinner2 = (Spinner) findViewById(R.id.quantity);
                String quantity = spinner2.getSelectedItem().toString();

                if (text.equalsIgnoreCase("Auckland Castle") && (selectedDay.equalsIgnoreCase("Monday") || selectedDay.equalsIgnoreCase("Tuesday"))) {
                    castleError();
                } else {

                    //All relevant values
                    intent.putExtra("Castle", text);
                    intent.putExtra("DayName", selectedDay);
                    intent.putExtra("CurrentTime", currentTime);
                    intent.putExtra("currentDate", currentDate);
                    intent.putExtra("selectedDate", String.valueOf(displayDate.getText()));
                    intent.putExtra("quantity", quantity);
                    startActivity(intent);
                }
            }
        });

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.getMenu().setGroupCheckable(0,false,true);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(Booking.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(Booking.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(Booking.this, ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });


    }


    //Sets date to current day
    public void resetDate(){
        selectedDay = String.valueOf(sdf.format(cal.getTime()));

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH)+1;

        String dayNew = null;
        if(day<10){
            dayNew = "0"+day;
        }else{
            dayNew = String.valueOf(day);
        }

        String monthNew = null;
        if(month<10){
            monthNew = "0"+month;
        }else{
            monthNew = String.valueOf(month);
        }

        displayDate.setText(dayNew+"/"+monthNew+"/"+String.valueOf(cal.get(Calendar.YEAR)));


    }

    //error for user trying to search dates in the past
    public void displayError(){
        builder.setMessage("Bookings cannot be placed in the past.")
                .setCancelable(false)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        resetDate();
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"Date reset to today.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error - Date is in the past.");
        alert.show();

    }

    //error for castle closed
    public void castleError(){
        builder.setMessage("Auckland Castle is closed on Mondays and Tuesday. Please select a different castle or day")
                .setCancelable(false)
                .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Error - Castle closed.");
        alert.show();
    }
}