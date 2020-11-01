package com.example.river_island;

public class Product {

    private String product_name;
    private String cost;
    private String isTrending;
    private String imgURL;

    public Product(String product_name, String isTrending, String cost, String imgURL)
    {
        this.product_name = product_name;
        this.isTrending = isTrending;
        this.cost = cost;
        this.imgURL = imgURL;
    }


    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {this.imgURL = imgURL; }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getIsTrending() {
        return isTrending;
    }

    public void setIsTrending(String isTrending) {
        this.isTrending = isTrending;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

}
