package com.example.android_doan.utils;

public class Resource {
    public enum Status { LOADING, SUCCESS, ERROR }
    public enum Action { GET_CART, UPDATE_QUANTITY, REMOVE_CART }

    private final Status status;
    private final String message;
    private final Action action;

    public Resource(Status status, String message, Action action) {
        this.status = status;
        this.message = message;
        this.action = action;
    }

    public static Resource loading(Action action) {
        return new Resource(Status.LOADING, null, action);
    }

    public static Resource success(String message, Action action) {
        return new Resource(Status.SUCCESS, message, action);
    }

    public static Resource error(String message, Action action) {
        return new Resource(Status.ERROR, message, action);
    }

    public Status getStatus() { return status; }
    public String getMessage() { return message; }
    public Action getAction() { return action; }
}