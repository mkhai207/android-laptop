package com.example.android_doan.data.model;

import android.net.Uri;

public class SliderImage {
    private String url;
    private Uri uri;

    public SliderImage(String url) {
        this.url = url;
        this.uri = null;
    }

    public SliderImage(Uri uri) {
        this.uri = uri;
        this.url = null;
    }

    public String getUrl() {
        return url;
    }

    public Uri getUri() {
        return uri;
    }

    public boolean isLocalImage() {
        return uri != null;
    }
}
