package com.example.team05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentFailed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_failed);
        Intent incomingIntent = getIntent();

        String incomingReason = incomingIntent.getStringExtra("Reason");

        TextView reason = (TextView) findViewById(R.id.reasonTV);
        reason.setText(incomingReason);

    }
}