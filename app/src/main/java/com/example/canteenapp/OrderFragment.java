package com.example.canteenapp;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    ListView listView;
    OrderCustomAdapter adapter = null;
    Button submitButton;
    TextView totalPrice;
    int preparationTime;
    double total;

    public OrderFragment() {
        // Required empty public constructor
        MainActivity.orderFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        submitButton = (Button) view.findViewById(R.id.submitOrderButton);
        listView = (ListView) view.findViewById(R.id.orderList);
        totalPrice = (TextView) view.findViewById(R.id.totalPriceText);
        adapter = new OrderCustomAdapter(getContext(), MainActivity.order.order);
        listView.setAdapter(adapter);
        final OrderFragment thisObject = this;
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FetchData fetchData = new FetchData(thisObject, total);
                fetchData.execute();
            }
        });
        return view;
    }

    public void updateList(HashMap<MenuItem, Integer> order) {
        adapter.updateList(order);
        total = 0;
        for (MenuItem menuItem : order.keySet()) {
            total += menuItem.getPrice() * order.get(menuItem);
        }
        totalPrice.setText(String.format("Rs.%.2f", total));
    }

    public void updateView() {
        MainActivity.order.order.clear();
        BottomSheetFragment orderConfirmation = new BottomSheetFragment();
        orderConfirmation.preparationTime = preparationTime;
        orderConfirmation.total = total;
        updateList(MainActivity.order.order);
        orderConfirmation.show(getFragmentManager(), "OrderConfirmation");
    }
}

class OrderCustomAdapter extends BaseAdapter {

    HashMap<MenuItem, Integer> menuItemRows;
    Context context;
    LayoutInflater inflater;
    Iterator<MenuItem> iterator;

    OrderCustomAdapter(Context context, HashMap<MenuItem, Integer> menuItemRows) {
        this.context = context;
        this.menuItemRows = menuItemRows;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iterator = this.menuItemRows.keySet().iterator();
    }

    void updateList(HashMap<MenuItem, Integer> menuItemRows) {
        this.menuItemRows = menuItemRows;
        iterator = this.menuItemRows.keySet().iterator();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return menuItemRows.size();
    }

    @Override
    public Object getItem(int i) {
        return menuItemRows.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = inflater.inflate(R.layout.order_cell, viewGroup, false);
        MenuItem menuItem = iterator.next();
        TextView name = (TextView) row.findViewById(R.id.orderTitleText);
        TextView detail = (TextView) row.findViewById(R.id.orderDetailText);
        TextView qty = (TextView) row.findViewById(R.id.orderQtyText);
        ImageView imageView = (ImageView) row.findViewById(R.id.orderImageView);
        name.setText(menuItem.getName());
        detail.setText(String.format("Rs.%.2f", menuItem.getPrice()));
        qty.setText(String.valueOf(menuItemRows.get(menuItem)));
        imageView.setImageBitmap(menuItem.getImage_bmp());
        return row;
    }
}