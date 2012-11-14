package org.vaadin.vol.client.wrappers.control;

import org.vaadin.vol.client.wrappers.handler.Handler;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

import com.google.gwt.core.client.JavaScriptObject;

public class DrawFeature extends Control {
    protected DrawFeature() {
    }

    public static DrawFeature create(VectorLayer targetLayer,
            Handler drawHandler) {
        return create(targetLayer, drawHandler, null);
    }

    public native static DrawFeature create(VectorLayer targetLayer,
            Handler drawHandler, JavaScriptObject options)
    /*-{
    	return new $wnd.OpenLayers.Control.DrawFeature(targetLayer, drawHandler, options);
    }-*/;

}
