package com.flixbus.api.domain;

public class CartExtraProductsFreeExtra {

    private String title;
    private String description;
    private String product_type;
    private String extra_reference_uid;
    private String price_per_item;

    private int count;
    private int max_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getExtra_reference_uid() {
        return extra_reference_uid;
    }

    public void setExtra_reference_uid(String extra_reference_uid) {
        this.extra_reference_uid = extra_reference_uid;
    }

    public String getPrice_per_item() {
        return price_per_item;
    }

    public void setPrice_per_item(String price_per_item) {
        this.price_per_item = price_per_item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getMax_count() {
        return max_count;
    }

    public void setMax_count(int max_count) {
        this.max_count = max_count;
    }
}
