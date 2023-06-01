package com.sliceclient.ultralight.js;

import com.labymedia.ultralight.UltralightView;
import com.labymedia.ultralight.databind.Databind;
import com.labymedia.ultralight.databind.DatabindConfiguration;
import com.labymedia.ultralight.javascript.JavascriptContext;
import com.labymedia.ultralight.javascript.JavascriptContextLock;
import com.labymedia.ultralight.javascript.JavascriptObject;
import com.labymedia.ultralight.javascript.JavascriptValue;
import com.sliceclient.ultralight.js.binding.SliceJsClientBinding;
import com.sliceclient.ultralight.view.View;
import com.sun.istack.internal.NotNull;
import com.sun.org.slf4j.internal.LoggerFactory;
import lombok.Getter;
import com.sun.org.slf4j.internal.Logger;

public class SliceJsContext {

    private static final Logger logger = LoggerFactory.getLogger(SliceJsContext.class);

    @Getter
    private final View view;

    @Getter
    private final Databind databind;

    public SliceJsContext(View view) {
        this.view = view;
        this.databind = new Databind(
                DatabindConfiguration
                        .builder()
                        .contextProviderFactory(javascriptValue -> consumer -> {
                                    try (JavascriptContextLock lock = view.getView().lockJavascriptContext()) {
                                        consumer.accept(lock);
                                    } catch (Exception e) {
                                        logger.error("An exception occurred which prevented a JavaScript action from being executed by Ultralight JS Engine.", e);
                                    }
                                }
                        )
                        .build()
        );

    }

    public void loadContext(View view, JavascriptContext context) {
        JavascriptContext globalContext = context.getGlobalContext();
        JavascriptObject globalObject = globalContext.getGlobalObject();

        setProperty(globalObject, context, "view", view);
        setProperty(globalObject, context, "client", new SliceJsClientBinding());
    }

    public void setProperty(JavascriptObject obj, JavascriptContext context, String name, Object value) {
        obj.setProperty(name, databind.getConversionUtils().toJavascript(context, value), 0);
    }
}
