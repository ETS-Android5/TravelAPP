/**
 * This is the main activity page with a view of all the castles on one page
 *
 * Functionalities include a clickable image of each castle which takes the user
 * to the specific castle page with more details, as well as a book button which takes
 * the user directly to the book page.
 *
 * The page is swipable so the user can view all the castles.
 *
 * TO DO:
 * - DENNIS TO COMPLETE UI
 *
 * created by Harry Akitt 16/03/2022
 *
 * - Ruipeng (Dennis) completes Navmore button function the page jump 12/04/2022
 * **/

package com.example.team05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //hide title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }



        //set action bar
        ActionBar bar = getSupportActionBar();
        if(bar!=null){
            TextView tv = new TextView(getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, // Width of TextView
                    RelativeLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
            tv.setLayoutParams(lp);
            tv.setText(bar.getTitle());
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(tv);
    }


        //set image button image and intent
        ImageButton iTopImage = (ImageButton) findViewById(R.id.topImage);
        int imageResource = getResources().getIdentifier("@drawable/home_alnwick", null, this.getPackageName());
        iTopImage.setImageResource(imageResource);
        iTopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CastleScreen1.class);
                startActivity(intent);
            }
        });

        //set Book Alnwick button
        Button bookAln = (Button) findViewById(R.id.BookAlnwick);
        bookAln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("castle","Alnwick Castle");
                startActivity(intent);
            }
        });

        //set second image button image and intent
        ImageButton iSecondImage = (ImageButton) findViewById(R.id.secondImage);
        int imageResource2 = getResources().getIdentifier("@drawable/home_auckland", null, this.getPackageName());
        iSecondImage.setImageResource(imageResource2);
        iSecondImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CastleScreen2.class);
                startActivity(intent);
            }
        });

        //set Book Auckland button
        Button bookAuck = (Button) findViewById(R.id.BookAuckland);
        bookAuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("castle","Auckland Castle");
                startActivity(intent);
            }
        });


        //set third image button image and intent
        ImageButton iThirdImage = (ImageButton) findViewById(R.id.thirdImage);
        int imageResource3 = getResources().getIdentifier("@drawable/home_barnard", null, this.getPackageName());
        iThirdImage.setImageResource(imageResource3);
        iThirdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CastleScreen3.class);
                startActivity(intent);
            }
        });

        //set Book Barnard button
        Button bookBar = (Button) findViewById(R.id.BookBarnard);
        bookBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("castle","Barnard Castle");
                startActivity(intent);
            }
        });

        //set fourth image button image and intent
        ImageButton iFourthImage = (ImageButton) findViewById(R.id.fourthImage);
        int imageResource4 = getResources().getIdentifier("@drawable/home_bamburgh", null, this.getPackageName());
        iFourthImage.setImageResource(imageResource4);
        iFourthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CastleScreen4.class);
                startActivity(intent);
            }
        });

        //set Book Barnard button
        Button bookBam = (Button) findViewById(R.id.BookBamburgh);
        bookBam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Booking.class);
                intent.putExtra("castle","Bamburgh Castle");
                startActivity(intent);
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
                        Intent intent3 = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(MainActivity.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(MainActivity.this, /*More*/ ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(6.8f);
        mMap.setMaxZoomPreference(15.0f);
        LatLng Alnwick_Castle  = new LatLng(55.41571066816451, -1.7058452995980735);
        LatLng Auckland_Castle  = new LatLng(54.67153810776224, -1.6712613553567615);
        LatLng Barnard_Castle  = new LatLng(54.5456698230093, -1.9236628163269331);
        LatLng Bamburgh_Castle  = new LatLng(55.609080781406995, -1.7099322879491325);
        mMap.addMarker(new MarkerOptions().position(Bamburgh_Castle ).title("Bamburgh Castle"));
        mMap.addMarker(new MarkerOptions().position(Barnard_Castle).title("Barnard Castle"));
        mMap.addMarker(new MarkerOptions().position(Auckland_Castle).title("Auckland Castle"));
        mMap.addMarker(new MarkerOptions().position(Alnwick_Castle).title("Alnwick Castle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Auckland_Castle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Alnwick_Castle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Barnard_Castle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Bamburgh_Castle));
    }



}