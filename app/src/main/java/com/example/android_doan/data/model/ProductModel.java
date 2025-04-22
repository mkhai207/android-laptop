package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ProductModel implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("model")
    private String model;

    @SerializedName("cpu")
    private String cpu;

    @SerializedName("ram")
    private int ram;

    @SerializedName("memory")
    private String memory;

    @SerializedName("memoryType")
    private String memoryType;

    @SerializedName("gpu")
    private String gpu;

    @SerializedName("screen")
    private String screen;

    @SerializedName("price")
    private double price;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("status")
    private String status; // Có thể là null

    @SerializedName("weight")
    private double weight;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("color")
    private String color;

    @SerializedName("port")
    private String port;

    @SerializedName("os")
    private String os; // Có thể là null

    @SerializedName("sold")
    private int sold;

    @SerializedName("tag")
    private String tag;

    @SerializedName("slider")
    private List<String> slider;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("category")
    private CategoryModel category;

    @SerializedName("brand")
    private BrandModel brand;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getCpu() {
        return cpu;
    }

    public int getRam() {
        return ram;
    }

    public String getMemory() {
        return memory;
    }

    public String getMemoryType() {
        return memoryType;
    }

    public String getGpu() {
        return gpu;
    }

    public String getScreen() {
        return screen;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public double getWeight() {
        return weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getColor() {
        return color;
    }

    public String getPort() {
        return port;
    }

    public String getOs() {
        return os;
    }

    public int getSold() {
        return sold;
    }

    public String getTag() {
        return tag;
    }

    public List<String> getSlider() {
        return slider;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public BrandModel getBrand() {
        return brand;
    }
}
