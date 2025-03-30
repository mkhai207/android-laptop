package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BrandModel implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;
    private boolean isChecked;

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isChecked() {
        return isChecked;
    }
}
