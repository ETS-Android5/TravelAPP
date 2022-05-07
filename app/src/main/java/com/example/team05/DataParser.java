/**
 ***** Description *****
 * This class creates JSON Object of nearby places
 *
 ***** Key Functionality *****
 * -Reformat information pulled from Google Map API query into accessible JSON object
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

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    //creates hashmap of all found places and their IDs
    public static HashMap<Integer,String> placeIDs = new HashMap<>();

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");


        for (int i = 0; i < placesCount; i++) {
            try {
                if(placeIDs==null){
                    placeIDs.put(0,"");
                }else{
                    placeIDs.put(placeIDs.size(),""); //puts additional placeID into Array
                }

                placeMap = getPlace((JSONObject) jsonArray.get(i),placeIDs.size());
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson, int currentPos) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String phoneNo = "";
        double rating = 0;

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if(!googlePlaceJson.isNull("international_phone_number")){
                phoneNo = googlePlaceJson.getString("international_phone_number");
            }
            if(!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getDouble("rating");
            }

            String placeID = googlePlaceJson.getString("place_id");
            placeIDs.put(currentPos,placeID);


            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");

            Log.d("getPlace",phoneNo);
            Log.d("getPlace",placeID);

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            googlePlaceMap.put("phoneNo",phoneNo);
            googlePlaceMap.put("rating", String.valueOf(rating));

            //placeID used to give additional information
            googlePlaceMap.put("placeIDPos", String.valueOf(currentPos));

            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
