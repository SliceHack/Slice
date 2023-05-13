package com.sliceclient.ultralight.listener;

import com.labymedia.ultralight.plugin.loading.UltralightLoadListener;

public abstract class SliceLoadListener implements UltralightLoadListener {
    @Override public void onBeginLoading(long frameId, boolean isMainFrame, String url) {}
    @Override public void onFinishLoading(long frameId, boolean isMainFrame, String url) {}
    @Override public void onFailLoading(long frameId, boolean isMainFrame, String url, String description, String errorDomain, int errorCode) {}
    @Override public void onUpdateHistory() {}
    @Override public void onWindowObjectReady(long frameId, boolean isMainFrame, String url) {}
    @Override public void onDOMReady(long frameId, boolean isMainFrame, String url) {}
}
