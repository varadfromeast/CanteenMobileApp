package com.example.canteenapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements CategoryFragment.GetCategory {

    String category = "";
    public ArrayList<MenuItem> menuItems;
    ArrayList<MenuItem> menuItemsForCategory;
    MenuCustomAdapter adapter = null;

    ListView menuListView;
    AlertDialog dialog = null;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        // Initialize
        menuItems = new ArrayList<>();
        menuItemsForCategory = new ArrayList<>();
        menuListView = (ListView) view.findViewById(R.id.menuList);
        adapter = new MenuCustomAdapter(getContext(), menuItemsForCategory);
        menuListView.setAdapter(adapter);
        // Fetch the list of menuItems
        FetchData fetchData = new FetchData(this, category);
        fetchData.execute();
        // Show the AlertDialog
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View popUpView = getLayoutInflater().inflate(R.layout.popup, null);
                TextView nameTextView = (TextView) popUpView.findViewById(R.id.itemNameText);
                TextView descriptionTextView = (TextView) popUpView.findViewById(R.id.detailTextView);
                TextView priceTextView = (TextView) popUpView.findViewById(R.id.priceTextView);
                Button addToOrderButton = (Button) popUpView.findViewById(R.id.addButton);
                Button minusQtyButton = (Button) popUpView.findViewById(R.id.minusPopupButton);
                ImageView imageView = (ImageView) popUpView.findViewById(R.id.popupImageView);
                final TextView qtyTextView = (TextView) popUpView.findViewById(R.id.numberPopupTextView);
                Button plusQtyButton = (Button) popUpView.findViewById(R.id.plusPopupButton);
                // reduce number of items
                minusQtyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer prevValue = Integer.parseInt(qtyTextView.getText().toString());
                        if (prevValue != 1) {
                            qtyTextView.setText(String.valueOf(prevValue - 1));
                        }
                    }
                });
                // increase number of items
                plusQtyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer prevValue = Integer.parseInt(qtyTextView.getText().toString());
                        qtyTextView.setText(String.valueOf(prevValue + 1));
                    }
                });
                // add to order
                addToOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MenuItem menuItem = menuItemsForCategory.get(position);
                        Integer qty = Integer.valueOf(qtyTextView.getText().toString());
                        MainActivity.order.addToOrder(menuItem, qty);
                        Toast.makeText(getContext(), "Item successfully added!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                nameTextView.setText(menuItemsForCategory.get(i).getName());
                descriptionTextView.setText(menuItemsForCategory.get(i).getDescription());
                priceTextView.setText(String.format("Rs.%.2f", menuItemsForCategory.get(i).getPrice()));
                imageView.setImageBitmap(menuItemsForCategory.get(i).getImage_bmp());
                builder.setView(popUpView);
                dialog = builder.create();
                dialog.show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void updateView() {
        if (menuItems == null) return;
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getCategory().equals(category)) {
                menuItemsForCategory.add(menuItems.get(i));
            }
        }
        adapter.updateList(menuItemsForCategory);
    }

    @Override
    public void updateCategory(String category) {
        this.category = category.toLowerCase();
    }
}

class MenuCustomAdapter extends BaseAdapter {

    ArrayList<MenuItem> menuItemRows;
    Context context;
    LayoutInflater inflater;

    MenuCustomAdapter(Context context, ArrayList<MenuItem> menuItemRows) {
        this.context = context;
        this.menuItemRows = menuItemRows;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void updateList(ArrayList<MenuItem> menuItemRows) {
        this.menuItemRows = menuItemRows;
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
        View row = inflater.inflate(R.layout.menu_cell, viewGroup, false);
        MenuItem menuItem = menuItemRows.get(i);
        TextView name = (TextView) row.findViewById(R.id.menuTitleText);
        TextView detail = (TextView) row.findViewById(R.id.detailText);
        ImageView imageView = (ImageView) row.findViewById(R.id.menuImageView);
        name.setText(menuItem.getName());
        detail.setText(String.format("Rs.%.2f", menuItem.getPrice()));
        imageView.setImageBitmap(menuItem.getImage_bmp());
        return row;
    }
}