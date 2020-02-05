package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static Toolbar toolbar;
    static String serverIP = "192.168.43.236:8070";
    TabLayout tabLayout;
    TabItem menuTabItem;
    TabItem orderMenuItem;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    AlertDialog alertDialog;

    public static Order order;
    public static OrderFragment orderFragment = null;
    static CategoryFragment categoryFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActionBar(toolbar);

        order = new Order();

        // alert dialog to connect to the server IP
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View popUp = getLayoutInflater().inflate(R.layout.ip_popup, null);
        final EditText ipEditText = (EditText) popUp.findViewById(R.id.ipEditText);
        Button connectButton = (Button) popUp.findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serverIP = ipEditText.getText().toString();
                alertDialog.dismiss();
            }
        });
        builder.setView(popUp);
        alertDialog = builder.create();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        menuTabItem = (TabItem) findViewById(R.id.menuTabItem);
        orderMenuItem = (TabItem) findViewById(R.id.orderTabItem);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        toolbar.setTitle("Menu");
                        break;
                    case 1:
                        toolbar.setTitle("Order");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        categoryFragment.listView.setVisibility(ListView.VISIBLE);
    }
}

class Order {
    HashMap<MenuItem, Integer> order;

    Order() {
        order = new HashMap<>();
    }

    void addToOrder(MenuItem menuItem, Integer qty) {
        if (!order.containsKey(menuItem)) {
            order.put(menuItem, qty);
        } else {
            order.put(menuItem, order.get(menuItem) + qty);
        }
        MainActivity.orderFragment.updateList(order);
    }

    ArrayList<Integer> getOrder() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (MenuItem item: order.keySet()) {
            arrayList.add(item.getId());
        }
        return arrayList;
    }
}