package com.example.android_doan.data.model.response;

import com.google.gson.annotations.SerializedName;

public class UploadFileResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private FileData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public FileData getData() {
        return data;
    }

    public static class FileData{
        @SerializedName("fileName")
        private String fileName;

        @SerializedName("uploadedTime")
        private String uploadedTime;

        public String getFileName() {
            return fileName;
        }

        public String getUploadedTime() {
            return uploadedTime;
        }
    }
}
