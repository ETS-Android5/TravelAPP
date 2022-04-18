/**
 * This is the screen to show more details on the first castle (Barnard)
 *
 * Functionalities include an image of the castle and a book button which takes you to the
 * BookOutbound page.
 *
 * TO DO:
 * - ADD TEXT ON THE PAGE INCLUDING OPENING TIMES, COSTS AND A BIO.
 * - GOOGLE MAP OF LOCATION?
 * - DENNIS/SONG/TIAN LOOKING INTO USING AN API
 * - DENNIS TO COMPLETE UI
 *
 * created by Harry Akitt 16/03/2022
 *
 *Google API/Introduction Pages
 *updated by Qingbiao Song 03/04/2022
 *
 * - Ruipeng (Dennis) completes Navmore button function the page jump 12/04/2022
 * **/

package com.example.team05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CastleScreen3 extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.castle_layout);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //set opening and closing times for castle
        TextView mon1 = (TextView) findViewById(R.id.mon1);
        mon1.setText("10:00am");
        TextView mon2 = (TextView) findViewById(R.id.mon2);
        mon2.setText("5:00pm");

        TextView tues1 = (TextView) findViewById(R.id.tues1);
        tues1.setText("10:00am");
        TextView tues2 = (TextView) findViewById(R.id.tues2);
        tues2.setText("5:00pm");

        TextView wed1 = (TextView) findViewById(R.id.wed1);
        wed1.setText("10:00am");
        TextView wed2 = (TextView) findViewById(R.id.wed2);
        wed2.setText("5:00pm");

        TextView thu1 = (TextView) findViewById(R.id.thu1);
        thu1.setText("10:00am");
        TextView thu2 = (TextView) findViewById(R.id.thu2);
        thu2.setText("5:00pm");

        TextView fri1 = (TextView) findViewById(R.id.fri1);
        fri1.setText("10:00am");
        TextView fri2 = (TextView) findViewById(R.id.fri2);
        fri2.setText("5:00pm");

        TextView sat1 = (TextView) findViewById(R.id.sat1);
        sat1.setText("10:00am");
        TextView sat2 = (TextView) findViewById(R.id.sat2);
        sat2.setText("5:00pm");

        TextView sun1 = (TextView) findViewById(R.id.sun1);
        sun1.setText("10:00am");
        TextView sun2 = (TextView) findViewById(R.id.sun2);
        sun2.setText("5:00pm");

        //set price
        TextView price = (TextView) findViewById(R.id.price);
        price.setText("Student: £8.10");

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
            bar.setDisplayHomeAsUpEnabled(true);
        }

        // set image
        ImageView iTopImage = (ImageView) findViewById(R.id.topImage);
        int imageResource = getResources().getIdentifier("@drawable/barnard_castle", null, this.getPackageName());
        iTopImage.setImageResource(imageResource);

        // set main text field
        TextView mMessageWindow = (TextView) findViewById(R.id.messageWindow);
        StringBuilder stringBuilder = new StringBuilder();
        String message = "Barnard Castle is a market town on the north bank of the River Tees, in County Durham, Northern England. The town is named after and built around a mediaeval castle ruin. The town's Bowes Museum's has an 18th-century Silver Swan automaton exhibit and paintings by Goya and El Greco."
                + "It sits on the opposite bank to Startforth and is 21 miles (34 km) south-west of the county town of Durham. Nearby towns include Bishop Auckland to the north-east, Darlington to the east and Richmond in North Yorkshire to the south-east. The largest employer is GlaxoSmithKline, with a manufacturing facility on the town's outskirts.";
        stringBuilder.append(message);
        mMessageWindow.setText(stringBuilder.toString());

        //set Book button
        Button book = (Button) findViewById(R.id.BookCastle);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CastleScreen3.this, Booking.class);
                intent.putExtra("castle","Barnard Castle");
                startActivity(intent);
            }
        });

        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(CastleScreen3.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(CastleScreen3.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(CastleScreen3.this, More.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Castle.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(8.0f);
        mMap.setMaxZoomPreference(15.0f);
        LatLng Barnard_Castle  = new LatLng(54.5456698230093, -1.9236628163269331);
        mMap.addMarker(new MarkerOptions().position(Barnard_Castle).title("Barnard Castle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Barnard_Castle));
    }
}
