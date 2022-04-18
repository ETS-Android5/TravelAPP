/**
 * This is the page to set the format of the list view of journeys
 *
 * created by Harry Akitt 04/04/2022
 *
 * UPDATE:
 * -OP Made non static so that class could accept different instances of journeys
 * **/

package com.example.team05;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class JourneyAdapter extends ArrayAdapter<Journey> {

    private Context mContext;
    int mResource;

    private final String TAG = "JA";
    private ArrayList<Journey> journeyList = new ArrayList<>();

    private String journeyTime;

    public JourneyAdapter(Context context, int resource, ArrayList<Journey> objects){
        super(context, resource, objects);
        mContext = context;
        mResource = resource;;
        journeyList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String departT = setTimeFormat(getItem(position).getDepartureT1());
        String arrivalT = setTimeFormat(getItem(position).getArrivalT());

        String journeyStartFinish = departT +" ⮕ "+ arrivalT;
        journeyTime = setJourneyTime(departT, arrivalT);

        String price = getItem(position).getPrice();
        String totalT = getItem(position).getTotalT();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvDep = (TextView) convertView.findViewById(R.id.tv1);
        TextView tvPri = (TextView) convertView.findViewById(R.id.tv2);
        TextView tvJourneyTime = (TextView) convertView.findViewById(R.id.tv3);
        TextView tvTot = (TextView) convertView.findViewById(R.id.tv4);


        tvDep.setText(journeyStartFinish);
        tvJourneyTime.setText(journeyTime);
        tvPri.setText(price);
        tvTot.setText(totalT);

        //debugging
//        Log.d(TAG,"Position is "+position+", depTime = "+departT+", arrTime = "+ArrivalT);


        return convertView;
    }

    private String setTimeFormat(String providedTime){

        if(providedTime.length()==3){
            providedTime = "0"+providedTime;
        }
        return providedTime.substring(0,2) + ":" + providedTime.substring(2,4);

    }

    private String setJourneyTime(String departT, String arriveT){
        int departAsMinutes = 60 * Integer.parseInt(departT.substring(0,2)) + Integer.parseInt(departT.substring(3,5));
        int arrivalAsMinutes = 60*Integer.parseInt(arriveT.substring(0,2)) + Integer.parseInt(arriveT.substring(3,5));

        int totalMinutes = arrivalAsMinutes - departAsMinutes;
        int minutes = totalMinutes%60;
        int hours = (totalMinutes - minutes)/60;

        if(hours==1){
            return (hours + " hour, "+minutes+" minutes.");
        }else{
            return (hours + " hours, "+minutes+" minutes.");
        }
    }

}
