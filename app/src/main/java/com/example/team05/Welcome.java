/**
 ***** Description:  *****
 * This is the opening welcome page the user see when opening the app.
 *
 ***** Key functionality *****
 *  -gallery view of images of the four castles that cycle after a give time or can be swiped across
 *
 ***** Author(s):  *****
 * Harry Akitt
 * -Created 16/03/22
 * - Main functionality and button
 * Ruipeng Jiao
 * -Formatting
 *
*/

package com.example.team05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity{

    private ViewPager page;
    private List<slideItems> listItems;
    private List<slideItems> descriptionItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_main);

        //hide action bar
        getSupportActionBar().hide();

        page = findViewById(R.id.my_pager) ;

        //create object list of castle images
        listItems = new ArrayList<>() ;
        listItems.add(new slideItems(R.drawable.welcome_discover, "Castle\n"+"Information","Learn more about the North East's rich history"));  //Discover
        listItems.add(new slideItems(R.drawable.welcome_book, "Book Tickets","Get your bus tickets and castle entrance, all in one place!"));         //Book
        listItems.add(new slideItems(R.drawable.welcome_student, "Surrounding\n"+"Locations","Discover things to do nearby")); //Find Things to Do
        listItems.add(new slideItems(R.drawable.welcome_todo, "For Newcastle\n"+"Students","Made exclusively for Newcastle University students"));  //For Students in Newcastle

        //set page adapter
        slideItemsAdapter itemsPager_adapter = new slideItemsAdapter(this, listItems);
        page.setAdapter(itemsPager_adapter);

        // Set timer and time between slides in milliseconds
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(),10000,10000);

        //create Enter button to take the user to the Main page when clicked
        Button enterBtn = (Button) findViewById(R.id.welcomeBtn);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // create slide timer class
    class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            Welcome.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page.getCurrentItem()< listItems.size()-1) {
                        page.setCurrentItem(page.getCurrentItem()+1);
                    }
                    else
                        page.setCurrentItem(0);
                }
            });
        }
    }
}
