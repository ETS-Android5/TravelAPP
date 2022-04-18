/**
 *
 * This is a screen showing information such as recommended shops and hotels near the four castles
 *
 * Features include Castle selection and shop brief information and shop links
 *
 * TO DO
 * The left sidebar shows the castle icon
 * The information of shops around the castle is displayed on the right
 * Ruipeng (Dennis) completes the UI
 *
 * Changelog:
 * - Created by Ruipeng Jiao(Dennis) 12/04/2022
 * - Ruipeng (Dennis) completes Navmore button function the page jump 12/04/2022
 *
 */

package com.example.team05;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class More extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private FrameLayout mFrame;
    private List<User> mList = new ArrayList<>();  //List on the left
    private List<Fragment> mFragmentList = new ArrayList<>();
    private FragmentManager supportFragmentManager = getSupportFragmentManager();
    private MyListViewApader apader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_recommend);
        initView();
        initData();


        // set bottom nav bar
        BottomNavigationView bottomNavBar = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mainActivityNav:
                        Intent intent3 = new Intent(More.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.bookingNav:
                        Intent intent = new Intent(More.this, Booking.class);
                        startActivity(intent);
                        break;

                    case R.id.moreNav:
                        Intent intent2 = new Intent(More.this, More.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.mListView);
        mFrame = (FrameLayout) findViewById(R.id.mFrame);
    }

    private void initData() {
        //Add data to the listview collection on the left, adapter adaptation
        listViewData();
        //Add fragments and reuse fragments
        addFragment();
        //The first item in listview is selected by default
        replese(0);
        //The select of the first item in listview is true
        mList.get(0).setSelect(true);
        //Listview click event
        mListView.setOnItemClickListener(this);
    }


    // Operations of several castles to be added
    private void listViewData() {
        //Title List
        mList.add(new User("Alnwick Castle"));
        mList.add(new User("Auckland Castle"));
        mList.add(new User("Bamburgh Castle"));
        mList.add(new User("Barnard Castle"));


        //Adapter adapter
        apader = new MyListViewApader(mList, this);
        mListView.setAdapter(apader);
    }


    private void addFragment() {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        MultiplexingFragment multiplexingFragment = new MultiplexingFragment();
        for (int i = 0; i < mList.size(); i++) {
            /**
             * The title and content are temporarily empty
             * including thumbnails and paths
             * */
            Fragment multiplexing = multiplexingFragment.getMultiplexing(mList.get(i).getName(), "");
            mFragmentList.add(multiplexing);
        }
        //添加fragment
        for (int i = 0; i < mFragmentList.size(); i++) {
            transaction.add(R.id.mFrame, mFragmentList.get(i));
        }
        transaction.commit();
    }

    private void replese(int position) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        //All fragments are hidden, and the fragments corresponding to position are displayed and submitted.
        for (int i = 0; i < mFragmentList.size(); i++) {
            Fragment fragment = mFragmentList.get(i);
            transaction.hide(fragment);
        }
        transaction.show(mFragmentList.get(position)).commit();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Toggle fragment
        replese(position);
        for (int i = 0; i < mList.size(); i++) {
            mList.get(i).setSelect(false);
        }
        mList.get(position).setSelect(true);
        apader.notifyDataSetChanged();
        Toast.makeText(More.this, "" + position, Toast.LENGTH_SHORT).show();
    }




}

