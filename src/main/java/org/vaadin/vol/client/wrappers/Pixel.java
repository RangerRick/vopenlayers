package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;

public class Pixel extends JavaScriptObject {

    protected Pixel() {
    };

    public native static Pixel create(double x, double y)
    /*-{
    	return new $wnd.OpenLayers.Pixel(x, y);
    }-*/;

}
