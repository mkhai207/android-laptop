package com.example.android_doan.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadMultipleFileResponse {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<FileData> data;

    public static class FileData{
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
        public String getFileLink(){
            return fileLink;
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<FileData> getData() {
        return data;
    }
}
