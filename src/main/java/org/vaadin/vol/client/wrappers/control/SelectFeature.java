package org.vaadin.vol.client.wrappers.control;

import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.layer.Layer;

public class SelectFeature extends Control {
    protected SelectFeature() {
    }

    public native static SelectFeature create(Layer targetLayer,
            boolean multiple, boolean boxselection, boolean clickout,
            boolean toggle, boolean hover)
    /*-{
    	var o = {
           toggleKey: "ctrlKey",
           multipleKey: "shiftKey",
    	}
        o = multiple;
        o.box = boxselection;
        o.clickout = clickout;
        o.toggle = toggle;
        o.hover = hover;
    	return new $wnd.OpenLayers.Control.SelectFeature(targetLayer, o);
    }-*/;

    public static SelectFeature create(Layer targetLayer) {
        return create(targetLayer, false, false, false, false, false);
    }

    public final native void unselectAll()
    /*-{
        this.unselectAll();
    }-*/;

    public final native void select(Vector vector)
    /*-{
        this.select(vector);
    }-*/;

    public final native void highlight(Vector vector)
    /*-{
        this.highlight(vector);
    }-*/;
}
