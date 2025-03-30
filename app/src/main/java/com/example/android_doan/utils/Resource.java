package com.example.android_doan.utils;

public class Resource {
    public enum Status { LOADING, SUCCESS, ERROR }
    private final Status status;
    private final String message;

    public Resource(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static Resource loading() {
        return new Resource(Status.LOADING, null);
    }

    public static Resource success(String message) {
        return new Resource(Status.SUCCESS, message);
    }

    public static Resource error(String message) {
        return new Resource(Status.ERROR, message);
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
}