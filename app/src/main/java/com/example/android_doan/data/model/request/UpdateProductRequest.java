package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;

import java.util.List;

public class UpdateProductRequest {
    private String id; // Sử dụng UUID nếu ID là chuỗi UUID
    private String name;
    private String model;
    private String cpu;
    private String ram; // Giữ nguyên String nếu server mong đợi kiểu này
    private String memory;
    private String memoryType;
    private String gpu;
    private String screen;
    private String price; // Giữ nguyên String để đúng format
    private String description;
    private String thumbnail;
    private int status; // Kiểu int thay vì boolean
    private String weight;
    private String quantity;
    private String tag;
    private String os;
    private String color;
    private String port;
    private List<String> slider;
    private CategoryModel category;
    private BrandModel brand;

    public UpdateProductRequest(String id, String name, String model, String cpu, String ram, String memory, String memoryType, String gpu, String screen, String price, String description, String thumbnail, int status, String weight, String quantity, String tag, String os, String color, String port, List<String> slider, CategoryModel category, BrandModel brand) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.cpu = cpu;
        this.ram = ram;
        this.memory = memory;
        this.memoryType = memoryType;
        this.gpu = gpu;
        this.screen = screen;
        this.price = price;
        this.description = description;
        this.thumbnail = thumbnail;
        this.status = status;
        this.weight = weight;
        this.quantity = quantity;
        this.tag = tag;
        this.os = os;
        this.color = color;
        this.port = port;
        this.slider = slider;
        this.category = category;
        this.brand = brand;
    }
}
