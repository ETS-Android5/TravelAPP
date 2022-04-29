/**
 * This is the screen to show more details on the first castle (Auckland)
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
 * - created by Harry Akitt 16/03/2022
 *
 * - Google API/Introduction Pages
 * - updated by Qingbiao Song 03/04/2022
 *
 * - Ruipeng (Dennis) completes Navmore button function the page jump 12/04/2022
 *
 * - Ruipeng (Dennis) Add website jump function on the castle information page 23/04/2022
 * **/

package com.example.team05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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

public class CastleScreen2 extends AppCompatActivity implements OnMapReadyCallback{
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
        mon1.setText("Closed");
        TextView mon2 = (TextView) findViewById(R.id.mon2);
        mon2.setText("Closed");

        TextView tues1 = (TextView) findViewById(R.id.tues1);
        tues1.setText("Closed");
        TextView tues2 = (TextView) findViewById(R.id.tues2);
        tues2.setText("Closed");

        TextView wed1 = (TextView) findViewById(R.id.wed1);
        wed1.setText("11:00am");
        TextView wed2 = (TextView) findViewById(R.id.wed2);
        wed2.setText("4:00pm");

        TextView thu1 = (TextView) findViewById(R.id.thu1);
        thu1.setText("11:00am");
        TextView thu2 = (TextView) findViewById(R.id.thu2);
        thu2.setText("4:00pm");

        TextView fri1 = (TextView) findViewById(R.id.fri1);
        fri1.setText("11:00am");
        TextView fri2 = (TextView) findViewById(R.id.fri2);
        fri2.setText("4:00pm");

        TextView sat1 = (TextView) findViewById(R.id.sat1);
        sat1.setText("11:00am");
        TextView sat2 = (TextView) findViewById(R.id.sat2);
        sat2.setText("4:00pm");

        TextView sun1 = (TextView) findViewById(R.id.sun1);
        sun1.setText("11:00am");
        TextView sun2 = (TextView) findViewById(R.id.sun2);
        sun2.setText("4:00pm");

        //set price
        TextView price = (TextView) findViewById(R.id.price);
        price.setText("Adult: £14.00");

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
        int imageResource = getResources().getIdentifier("@drawable/auckland_castle", null, this.getPackageName());
        iTopImage.setImageResource(imageResource);

        // set main text field
        TextView mMessageWindow = (TextView) findViewById(R.id.messageWindow);
        StringBuilder stringBuilder = new StringBuilder();
        String message = "Auckland Castle, which is also known as Auckland Palace and to people that live locally as the Bishop's Castle or Bishop's Palace, is located in the town of Bishop Auckland in County Durham, England. " +
                "In 1832, this castle replaced Durham Castle as the official residence of the Bishops of Durham.It is now a tourist attraction, but still houses the Bishop's offices; the Castle is a Grade I listed building.";
        stringBuilder.append(message);
        mMessageWindow.setText(stringBuilder.toString());

        //set visit Website button
        Button button = findViewById(R.id.WebSite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://aucklandproject.org/venues/auckland-castle/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        //set Book button
        Button book = (Button) findViewById(R.id.BookCastle);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CastleScreen2.this, Booking.class);
                intent.putExtra("castle","Auckland Castle");
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
                        Intent intent3 = new Intent(CastleScreen2.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(CastleScreen2.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(CastleScreen2.this, More.class);
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
        LatLng Auckland_Castle  = new LatLng(54.67153810776224, -1.6712613553567615);
        mMap.addMarker(new MarkerOptions().position(Auckland_Castle).title("Auckland Castle"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Auckland_Castle));
    }
}
