package com.example.canteenapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class FetchData extends AsyncTask<Void, Void, Void> {

    String data = "";
    String category = "";

    String[] dataParsed;
    CategoryFragment categoryFragment = null;

    ArrayList<MenuItem> menuItems;
    MenuFragment menuFragment = null;

    HashMap<MenuItem, Integer> orderItems;
    int preparationTime;
    double total_amt = 0;
    OrderFragment orderFragment = null;

    String BaseURL = "http://192.168.43.236:8070/";

    FetchData(CategoryFragment fragment) {
        this.categoryFragment = fragment;
    }

    FetchData(MenuFragment fragment, String category) {
        this.menuFragment = fragment;
        this.category = category;
    }

    FetchData(OrderFragment fragment, double total_amt) {
        this.orderFragment = fragment;
        this.total_amt = total_amt;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        BaseURL = "http://" + MainActivity.serverIP + "/";

        if (categoryFragment != null) {
            fetchCategories();
        } else if (menuFragment != null) {
            fetchMenu();
        } else if (orderFragment != null) {
            submitOrder();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (categoryFragment != null) {
            updateCategooryFragment();
        } else if (menuFragment != null) {
            updateMenu();
        } else if (orderFragment != null) {
            updateOrder();
        }
    }

    private void fetchCategories() {
        try {
            URL url = new URL(BaseURL + "categories");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONArray categories = (JSONArray) jsonObject.get("categories");
            dataParsed = new String[categories.length()];
            for (int i = 0; i < categories.length(); i++) {
                dataParsed[i] = (String) categories.get(i);
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchMenu() {
        try {
            URL url = new URL(BaseURL + "menu?category=" + category);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONObject jsonObject = new JSONObject(data);
            JSONArray items = (JSONArray) jsonObject.get("items");
            menuItems = new ArrayList<>();
            String imageURL = "images/default.png";
            URL imageUrlObject;
            Bitmap image_bmp;
            for (int i=0; i < items.length(); i++) {
                JSONObject item = (JSONObject) items.get(i);
                // create the menu object
                MenuItem menuItem = new MenuItem();
                menuItem.setCategory((String) item.get("category"));
                menuItem.setDescription((String) item.get("description"));
                menuItem.setPrice(Double.parseDouble((String) item.get("price")));
                menuItem.setName((String) item.get("name"));
                menuItem.setId((Integer) item.get("id"));
                // fetch the image
                imageURL = (String) item.get("image_url");
                imageUrlObject = new URL(BaseURL + imageURL);
                image_bmp = BitmapFactory.decodeStream(imageUrlObject.openConnection().getInputStream());
                menuItem.setImage_bmp(image_bmp);
                // add the object to the list
                menuItems.add(menuItem);
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submitOrder() {
        try {
            // get connection
            URL url = new URL(BaseURL + "order");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // create json object
            JSONObject jsonObject;
            JSONArray jsonArray = new JSONArray();
            orderItems = new HashMap<>();
            orderItems = MainActivity.order.order;
            int i = 0;
            for (MenuItem menuItem: orderItems.keySet()) {
                jsonObject = new JSONObject();
                jsonObject.put("id", menuItem.getId());
                jsonObject.put("qty", orderItems.get(menuItem));
                jsonArray.put(i++, jsonObject);
            }
            jsonObject = new JSONObject();
            jsonObject.put("total_amt", total_amt);
            jsonObject.put("menuIds", jsonArray);
            // write the jsonObject to the server
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(jsonObject.toString() + "\n");
            outputStream.flush();
            outputStream.close();
            // Get preparation time from the server
            preparationTime = 0;
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                data += line;
            }
            JSONObject receivedJson = new JSONObject(data);
            preparationTime = receivedJson.getInt("preparation_time");
            // close the connection
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateCategooryFragment() {
        if (dataParsed == null) return;
        for (int i = 0; i < dataParsed.length; i++) {
            categoryFragment.categories.add(new SingleRow(capitalise(dataParsed[i])));
        }
        categoryFragment.updateList();
    }

    private void updateMenu() {
        menuFragment.menuItems = menuItems;
        menuFragment.updateView();
    }

    private void updateOrder() {
        orderFragment.preparationTime = preparationTime;
        orderFragment.updateView();
    }

    private String capitalise(String string) {
        StringBuilder res = new StringBuilder();
        String[] strArr = string.split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);

            res.append(str).append(" ");
        }
        return res.toString().trim();
    }
}
