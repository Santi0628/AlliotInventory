package com.alliot.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alliot.inventory.util.ParcelCompat;
import com.google.gson.annotations.SerializedName;

public class DefaultPicture implements Parcelable {

    @SerializedName("file")
    private PictureFile file;

    public DefaultPicture() {}

    public PictureFile getFile() { return file; }
    public void setFile(PictureFile file) { this.file = file; }

    // Parcelable
    protected DefaultPicture(Parcel in) {
        file = ParcelCompat.readParcelable(in, PictureFile.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(file, flags);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<DefaultPicture> CREATOR = new Creator<>() {
        @Override
        public DefaultPicture createFromParcel(Parcel in) { return new DefaultPicture(in); }
        @Override
        public DefaultPicture[] newArray(int size) { return new DefaultPicture[size]; }
    };
}
