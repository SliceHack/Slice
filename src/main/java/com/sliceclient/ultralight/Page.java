package com.sliceclient.ultralight;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter @Setter
public class Page {

    private String url;

    public Page(File file) {
        url = "file:///" + file.getAbsolutePath();
    }

    public Page(String url) {
        this.url = url;
    }

    public static Page of(String url) {
        return new Page(url);
    }
}
