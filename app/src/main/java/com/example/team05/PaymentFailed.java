/**
 ***** Description *****
 * This class displays a payment failed page which is called when HorsePay refuses transaction
 *
 ***** Key Functionality *****
 * -Inform user of reason for declined payment
 *
 ***** Author(s)  *****
 * Oli Presland
 * -Key functionality
 * Qingbiao Song
 * -UI design
 * -image import
 * -return booking button
 *
 ****** Changelog: *****
 *Qingbiao Song
 *-UI design
 *-image import
 *-return booking button
 *
 * **/


package com.example.team05;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PaymentFailed extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
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
                        Intent intent3 = new Intent(PaymentFailed.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(PaymentFailed.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(PaymentFailed.this, ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

        String incomingReason = incomingIntent.getStringExtra("Reason");

        TextView reason = (TextView) findViewById(R.id.reasonTV);
        reason.setText(incomingReason);
        Button button = findViewById(R.id.return_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentFailed.this,Booking.class);
                startActivity(intent);
            }
        });

    }


}