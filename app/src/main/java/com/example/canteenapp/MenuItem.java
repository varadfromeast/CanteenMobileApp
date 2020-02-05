package com.example.canteenapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

public class MenuItem {

    private String category;
    private String description;
    private double price;
    private String name;
    private Integer id;
    private Bitmap image_bmp;

    MenuItem() {
        category = "";
        description = "";
        price = 0.0;
        name = "";
        id = 0;
        image_bmp = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    }

    // Getters

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Integer getId() {
        return id;
    }

    public Bitmap getImage_bmp() {
        return image_bmp;
    }

    // Setters

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) { this.id = id; }

    public void setImage_bmp(Bitmap bmp) {
        this.image_bmp = bmp;
    }

    // Override methods for HashMap

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof MenuItem) {
            return ((MenuItem) obj).id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
