package com.example.android_doan.data.model.request;

import com.example.android_doan.data.model.BrandModel;
import com.example.android_doan.data.model.CategoryModel;

import java.util.List;

public class CreateProductRequest {
    private String name;
    private String model;
    private String cpu;
    private int ram;
    private String memory;
    private String memoryType;
    private String gpu;
    private String screen;
    private double price;
    private String description;
    private String thumbnail;
    private boolean status;
    private double weight;
    private int quantity;
    private String color;
    private String port;
    private String os;
    private String tag;
    private List<String> slider;
    private CategoryModel category;
    private BrandModel brand;

    public CreateProductRequest(String name, String model, String cpu, int ram, String memory, String memoryType, String gpu, String screen, double price, String description, String thumbnail, boolean status, double weight, int quantity, String color, String port, String os, String tag, List<String> slider, CategoryModel category, BrandModel brand) {
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
        this.color = color;
        this.port = port;
        this.os = os;
        this.tag = tag;
        this.slider = slider;
        this.category = category;
        this.brand = brand;
    }
}
