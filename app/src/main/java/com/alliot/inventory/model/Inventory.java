package com.alliot.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Inventory implements Parcelable {

    @SerializedName("available_quantity")
    private double availableQuantity;

    public Inventory() {
    }

    public double getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(double availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    protected Inventory(Parcel in) {
        availableQuantity = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(availableQuantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Inventory> CREATOR = new Creator<>() {
        @Override
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };
}

