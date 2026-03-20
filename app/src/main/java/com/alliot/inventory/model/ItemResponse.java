package com.alliot.inventory.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private List<Item> data;

    @SerializedName("page")
    private int page;

    @SerializedName("per")
    private int per;

    @SerializedName("total")
    private int total;

    @SerializedName("$paginated")
    private boolean paginated;

    public ItemResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isPaginated() {
        return paginated;
    }

    public void setPaginated(boolean paginated) {
        this.paginated = paginated;
    }

    public boolean hasMorePages() {
        return page < total;          // total = número de páginas totales
    }

    public int getEstimatedTotalItems() {
        return total * per;           // estimado = páginas × ítems/página
    }
}
