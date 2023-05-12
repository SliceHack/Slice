package com.sliceclient.ultralight;

import lombok.Getter;

import java.io.File;

public class Page {

    @Getter
    private final String url;

    public Page(File file) {
        this.url = "file:///" + file.getAbsolutePath();
    }

    public Page(String url) {
        this.url = url;
    }
}
