package com.alliot.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alliot.inventory.util.ParcelCompat;
import com.google.gson.annotations.SerializedName;

public class PictureFile implements Parcelable {

    @SerializedName("url")
    private String url;

    @SerializedName("icon")
    private ImageUrl icon;

    @SerializedName("medium")
    private ImageUrl medium;

    @SerializedName("large")
    private ImageUrl large;

    public PictureFile() {}

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public ImageUrl getIcon() { return icon; }
    public void setIcon(ImageUrl icon) { this.icon = icon; }
    public ImageUrl getMedium() { return medium; }
    public void setMedium(ImageUrl medium) { this.medium = medium; }
    public ImageUrl getLarge() { return large; }
    public void setLarge(ImageUrl large) { this.large = large; }

    public String getBestUrl() {
        if (large != null && large.getUrl() != null) return large.getUrl();
        if (medium != null && medium.getUrl() != null) return medium.getUrl();
        if (icon != null && icon.getUrl() != null) return icon.getUrl();
        return url;
    }

    public String getMediumUrl() {
        if (medium != null && medium.getUrl() != null) return medium.getUrl();
        if (large != null && large.getUrl() != null) return large.getUrl();
        if (icon != null && icon.getUrl() != null) return icon.getUrl();
        return url;
    }

    public String getLargeUrl() {
        if (large != null && large.getUrl() != null) return large.getUrl();
        if (medium != null && medium.getUrl() != null) return medium.getUrl();
        return url;
    }

    // Parcelable
    protected PictureFile(Parcel in) {
        url = in.readString();
        icon = ParcelCompat.readParcelable(in, ImageUrl.class);
        medium = ParcelCompat.readParcelable(in, ImageUrl.class);
        large = ParcelCompat.readParcelable(in, ImageUrl.class);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeParcelable(icon, flags);
        dest.writeParcelable(medium, flags);
        dest.writeParcelable(large, flags);
    }

    @Override
    public int describeContents() { return 0; }

    public static final Creator<PictureFile> CREATOR = new Creator<>() {
        @Override
        public PictureFile createFromParcel(Parcel in) { return new PictureFile(in); }
        @Override
        public PictureFile[] newArray(int size) { return new PictureFile[size]; }
    };
}
