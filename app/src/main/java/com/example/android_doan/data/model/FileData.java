package com.example.android_doan.data.model;

import com.google.gson.annotations.SerializedName;

public class FileData {
    @SerializedName("fileName")
    private String fileName;

    @SerializedName("uploadedTime")
    private String uploadedTime;

    private String fileLink;

    public String getFileName() {
        return fileName;
    }

    public String getUploadedTime() {
        return uploadedTime;
    }

    public String getFileLink() {
        return fileLink;
    }
}