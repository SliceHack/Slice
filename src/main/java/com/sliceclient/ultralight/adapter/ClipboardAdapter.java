package com.sliceclient.ultralight.adapter;

import com.labymedia.ultralight.plugin.clipboard.UltralightClipboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class ClipboardAdapter implements UltralightClipboard {

    @Override
    public void clear() {
        writePlainText("");
    }

    @Override
    public String readPlainText() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if(transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) transferable.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception ignored){}
        return "";
    }

    @Override
    public void writePlainText(String text) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
        } catch (Exception ignored){}
    }
}
