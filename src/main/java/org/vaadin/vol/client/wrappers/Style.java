package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;

public class Style extends JsObject {

    protected Style() {
    };

    public static native Style create()
    /*-{
         return  $wnd.OpenLayers.Util.extend({}, $wnd.OpenLayers.Feature.Vector.style['default']);
    }-*/;

    public static native Style create(String string)
    /*-{
        return new $wnd.OpenLayers.Style(string);
    }-*/;

    public static native Style create(JavaScriptObject symbolizer)
    /*-{
        if(symbolizer['__VOL_INHERIT']) {
            var parent = $wnd.OpenLayers.Feature.Vector.style[symbolizer['__VOL_INHERIT']];
            delete symbolizer['__VOL_INHERIT'];
            $wnd.OpenLayers.Util.applyDefaults(symbolizer, parent);
        }
        return new $wnd.OpenLayers.Style(symbolizer);
    }-*/;

}
