package com.example.canteenapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    ListView listView;
    ProgressBar spinner;
    public ArrayList<SingleRow> categories;
    CategoriesCell cell = null;

    public CategoryFragment() {
        // Required empty public constructor
        MainActivity.categoryFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.categoryListView);
        spinner = (ProgressBar) view.findViewById(R.id.spinner);

        // Initialize the ListView
        categories = new ArrayList<>();
        cell = new CategoriesCell(getContext(), categories);
        listView.setAdapter(cell);

        // Fetch data in the background queue
        spinner.setVisibility(View.VISIBLE);
        FetchData fetchData = new FetchData(this);
        fetchData.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listView.setVisibility(ListView.GONE);
                MenuFragment menuFragment = new MenuFragment();
                menuFragment.updateCategory(categories.get(i).title);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.CategoryFragment, menuFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void updateList() {
        cell.updateView(categories);
        spinner.setVisibility(View.GONE);
    }

    public interface GetCategory {
        void updateCategory(String category);
    }
}

class SingleRow {
    String title = "";

    SingleRow(String title) {
        this.title = title;
    }
}

class CategoriesCell extends BaseAdapter {

    Context context;
    ArrayList<SingleRow> categoriesList;
    LayoutInflater layoutInflater = null;

    CategoriesCell(Context context, ArrayList<SingleRow> list) {
        this.context = context;
        categoriesList = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void updateView(ArrayList<SingleRow> list) {
        categoriesList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoriesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = layoutInflater.inflate(R.layout.categories_cell, viewGroup, false);
        TextView title = (TextView) row.findViewById(R.id.tileText);
        SingleRow singleRow = categoriesList.get(i);
        title.setText(singleRow.title);

        return row;
    }
}
