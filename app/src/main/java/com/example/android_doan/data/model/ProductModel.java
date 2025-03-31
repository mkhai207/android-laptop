package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

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

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getCpu() { return cpu; }
    public void setCpu(String cpu) { this.cpu = cpu; }

    public int getRam() { return ram; }
    public void setRam(int ram) { this.ram = ram; }

    public String getMemory() { return memory; }
    public void setMemory(String memory) { this.memory = memory; }

    public String getMemoryType() { return memoryType; }
    public void setMemoryType(String memoryType) { this.memoryType = memoryType; }

    public String getGpu() { return gpu; }
    public void setGpu(String gpu) { this.gpu = gpu; }

    public String getScreen() { return screen; }
    public void setScreen(String screen) { this.screen = screen; }

    public double getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }

    public int getSold() { return sold; }
    public void setSold(int sold) { this.sold = sold; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public List<String> getSlider() { return slider; }
    public void setSlider(List<String> slider) { this.slider = slider; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public CategoryModel getCategory() { return category; }
    public void setCategory(CategoryModel category) { this.category = category; }

    public BrandModel getBrand() { return brand; }
    public void setBrand(BrandModel brand) { this.brand = brand; }
}
