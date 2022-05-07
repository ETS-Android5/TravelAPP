/**
 ***** Description *****
 * This class runs as a separate thread which calls DownloadUrl and DataParser to collect information on nearby places
 *
 ***** Key Functionality *****
 * -Run simultaneously as main thread to retrieve nearby place data
 *
 ***** Author(s)  *****
 * Oli Presland
 * -Key functionality
 *
 ***** References *****
 * Andy Point - Google Maps search feature into JSON (https://www.codeproject.com/Articles/1121102/Google-Maps-Search-Nearby-Displaying-Nearby-Places)
 *
 * **/
package com.example.team05;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    private String url;


    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
//        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
        Log.d("GooglePlacesReadTast", String.valueOf(nearbyPlacesList.size()));

        ThingsToDo.ShowNearbyPlaces(nearbyPlacesList);


    }

}
