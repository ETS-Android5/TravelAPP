/**
 ***** Description:  *****
 * This class is called to verify a payment with HorsePay
 *
 ***** Author(s):  *****
 * Oli Presland
 * -Key functionality
 *
 ***** References:  *****
 * Daniel Nesbitt - CSC8019 HorsePay (http://homepages.cs.ncl.ac.uk/daniel.nesbitt/CSC8019/HorsePay/)
 */

package com.example.team05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PaymentTask extends AsyncTask<String, Void, String> {

    String paymentData;
    boolean success;
    private Context context;

    public PaymentTask(Context context){
        this.context=context;
    }

    protected String doInBackground(String... data) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        try {
            URL url = new URL("http://homepages.cs.ncl.ac.uk/daniel.nesbitt/CSC8019/HorsePay/HorsePay.php");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            byte[] out = data[0].getBytes(StandardCharsets.UTF_8);
            OutputStream stream = connection.getOutputStream();
            stream.write(out);

            //Test connection
            int status = connection.getResponseCode();
            //System.out.println(status);

            //Unsuccessful connection displays error message. Successful adds to StringBuffer.
            if(status>299){ //if unsuccessful connection
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                Log.d("Confirm","Unsuccessful Request.");
            }else{ //if successful connection
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
            }
            Log.d("Confirm",responseContent.toString());
            paymentData = responseContent.toString();

            Log.d("Confirm","Returned: "+connection.getResponseCode() + " " + connection.getResponseMessage());

            connection.disconnect();



        } catch (Exception e) {
            Log.d("Payment","Payment Failed");
            return null;
        }
        return paymentData;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //will find t or f for status
        int indexStatus = paymentData.indexOf("paymetSuccess") + 25;
        char status  = paymentData.charAt(indexStatus);

        //will find reason for failure
        int indexReason = paymentData.indexOf("reason") + 9;
        String returnSub = paymentData.substring(indexReason);
        int indexEnd = returnSub.indexOf("\"");
        String reason  = returnSub.substring(0,indexEnd);
        Log.d("Confirm",reason);

        //will find customerID
        int indexCustomerID = paymentData.indexOf("customerID") + 13;
        String customerID = paymentData.substring(indexCustomerID,indexCustomerID+5);
        Log.d("Confirm",customerID);


        if(status=='t'){
            Intent intent = new Intent(context, PaymentSuccess.class);
            intent.putExtra("CustomerID",customerID);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context, PaymentFailed.class);
            intent.putExtra("Reason",reason);
            context.startActivity(intent);
        }


    }
}

