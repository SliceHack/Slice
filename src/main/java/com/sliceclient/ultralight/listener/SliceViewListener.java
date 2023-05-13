package com.sliceclient.ultralight.listener;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.input.UltralightCursor;
import com.labymedia.ultralight.math.IntRect;
import com.labymedia.ultralight.plugin.view.MessageLevel;
import com.labymedia.ultralight.plugin.view.MessageSource;
import com.labymedia.ultralight.plugin.view.UltralightViewListener;

public abstract class SliceViewListener implements UltralightViewListener {
    @Override public void onChangeTitle(String title) {}
    @Override public void onChangeURL(String url) {}
    @Override public void onChangeTooltip(String tooltip) {}
    @Override public void onChangeCursor(UltralightCursor cursor) {}
    @Override public void onAddConsoleMessage(MessageSource source, MessageLevel level, String message, long lineNumber, long columnNumber, String sourceId) {}
    @Override public UltralightView onCreateChildView(String openerUrl, String targetUrl, boolean isPopup, IntRect popupRect) {return null;}
}
