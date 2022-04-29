package com.example.team05;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class GetPlaceData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private String url;


    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetPlaceData", "doInBackground entered");
            url = (String) params[0];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GetPlaceData", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GetPlaceData", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GetPlaceData", "onPostExecute Entered");
//        HashMap<String, String> nearbyPlacesList = null;
//        nearbyPlacesList =  parse(result);
        HashMap info = parse(result);


//        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GetPlaceData", "onPostExecute Exit");

        ThingsToDo.placeName = String.valueOf(info.get("name"));
        ThingsToDo.placeUrl = String.valueOf(info.get("website"));
        ThingsToDo.placePhoneNo = String.valueOf(info.get("phoneNo"));
        ThingsToDo.placeRating = String.valueOf(info.get("rating"));
        Log.d("GetPlaceData",Thread.currentThread().getName());
        Log.d("GetPlaceData", String.valueOf(this.getStatus()));

        ThingsToDo.showPlaceInfo();
    }

    private HashMap<String,String> parse(String jsonData) {
        JSONObject jsonObject;
        HashMap<String, String> placeInfo = new HashMap<>();

        try {
            Log.d("GetPlaceData", "parse");
            jsonObject = new JSONObject((String) jsonData);
            Log.d("GetPlaceData", "objectCreated");
            Log.d("GetPlaceData", jsonObject.toString());
            jsonObject = new JSONObject(String.valueOf(jsonObject.get("result")));

            String name = jsonObject.getString("name");
            String website = jsonObject.getString("website");
            String formattedNumber = jsonObject.getString("formatted_phone_number");
            String rating = jsonObject.getString("rating");

            placeInfo.put("name",name);
            placeInfo.put("website",website);
            placeInfo.put("phoneNo",formattedNumber);
            placeInfo.put("rating",rating);

            Log.d("GetPlaceData","Success");
            Log.d("GetPlaceData",placeInfo.get("phoneNo"));




        } catch (JSONException e) {
            Log.d("GetPlaceData", "parse error");
            e.printStackTrace();
        }
        return placeInfo;
    }

}
