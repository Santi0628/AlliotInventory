package com.alliot.inventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.alliot.inventory.util.ParcelCompat;
import com.google.gson.annotations.SerializedName;

public class Item implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("internal_sku")
    private String internalSku;

    @SerializedName("external_sku")
    private String externalSku;

    @SerializedName("barcode")
    private String barcode;

    @SerializedName("barcode1")
    private String barcode1;

    @SerializedName("active")
    private boolean active;

    @SerializedName("consumable")
    private boolean consumable;

    @SerializedName("repairable")
    private boolean repairable;

    @SerializedName("restricted")
    private boolean restricted;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("default_picture")
    private DefaultPicture defaultPicture;

    @SerializedName("inventory")
    private Inventory inventory;

    @SerializedName("device_inventory")
    private Inventory deviceInventory;

    @SerializedName("model")
    private String model;

    @SerializedName("brand")
    private String brand;

    public Item() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInternalSku() { return internalSku; }
    public void setInternalSku(String internalSku) { this.internalSku = internalSku; }

    public String getExternalSku() { return externalSku; }
    public void setExternalSku(String externalSku) { this.externalSku = externalSku; }

    public String getBarcode() {
        if (barcode != null) return barcode;
        return barcode1;
    }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isConsumable() { return consumable; }
    public void setConsumable(boolean consumable) { this.consumable = consumable; }

    public boolean isRepairable() { return repairable; }
    public void setRepairable(boolean repairable) { this.repairable = repairable; }

    public boolean isRestricted() { return restricted; }
    public void setRestricted(boolean restricted) { this.restricted = restricted; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public DefaultPicture getDefaultPicture() { return defaultPicture; }
    public void setDefaultPicture(DefaultPicture defaultPicture) { this.defaultPicture = defaultPicture; }

    public Inventory getInventory() {
        if (inventory != null) return inventory;
        return deviceInventory;
    }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getMediumImageUrl() {
        if (defaultPicture != null && defaultPicture.getFile() != null) {
            return defaultPicture.getFile().getMediumUrl();
        }
        return null;
    }

    public String getLargeImageUrl() {
        if (defaultPicture != null && defaultPicture.getFile() != null) {
            return defaultPicture.getFile().getLargeUrl();
        }
        return null;
    }

    public double getAvailableQuantity() {
        Inventory inv = getInventory();
        return inv != null ? inv.getAvailableQuantity() : 0;
    }

    protected Item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        internalSku = in.readString();
        externalSku = in.readString();
        barcode = in.readString();
        barcode1 = in.readString();
        active = in.readByte() != 0;
        consumable = in.readByte() != 0;
        repairable = in.readByte() != 0;
        restricted = in.readByte() != 0;
        quantity = in.readInt();
        defaultPicture = ParcelCompat.readParcelable(in, DefaultPicture.class);
        inventory = ParcelCompat.readParcelable(in, Inventory.class);
        deviceInventory = ParcelCompat.readParcelable(in, Inventory.class);
        model = in.readString();
        brand = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(internalSku);
        dest.writeString(externalSku);
        dest.writeString(barcode);
        dest.writeString(barcode1);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeByte((byte) (consumable ? 1 : 0));
        dest.writeByte((byte) (repairable ? 1 : 0));
        dest.writeByte((byte) (restricted ? 1 : 0));
        dest.writeInt(quantity);
        dest.writeParcelable(defaultPicture, flags);
        dest.writeParcelable(inventory, flags);
        dest.writeParcelable(deviceInventory, flags);
        dest.writeString(model);
        dest.writeString(brand);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
