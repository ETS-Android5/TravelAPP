/**
 ***** Description *****
 * This class displays a payment success page which is called when HorsePay accepts transaction
 *
 ***** Key Functionality *****
 * -Inform user of successful transaction and customer ID
 *
 ***** Author(s)  *****
 * Oli Presland
 * -Key functionality
 * Qingbiao Song
 * -UI design
 * -image import
 * -return booking button
 *
 * ***** Changelog: *****
 * Qingbiao Song
 * -UI design
 * -image import
 * -return booking button
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

public class PaymentSuccess extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);
        Intent incomingIntent = getIntent();

        //hide action bar
        ActionBar bar = getSupportActionBar();
        bar.hide();

        String incomingReason = incomingIntent.getStringExtra("CustomerID");

        TextView customerIDText = (TextView) findViewById(R.id.customerIDTV);
        TextView customerID = (TextView) findViewById(R.id.customerID);
        customerIDText.setText("Your customer ID is: "+"\n\n\nKeep this information safe incase you do not receive your booking confirmation!");
        customerID.setText(incomingReason);
        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.getMenu().setGroupCheckable(0,false,true);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(PaymentSuccess.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(PaymentSuccess.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(PaymentSuccess.this, ThingsToDo.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
        Button button = findViewById(R.id.return_main);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentSuccess.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}