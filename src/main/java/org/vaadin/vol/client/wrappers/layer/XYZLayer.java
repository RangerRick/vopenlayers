package org.vaadin.vol.client.wrappers.layer;


public class XYZLayer extends Layer {

    protected XYZLayer() {
    };

    public native final static XYZLayer create(String display,
            String uri, boolean sphericalMercator)
    /*-{
        var params = {};
        params.sphericalMercator = sphericalMercator;
        return new $wnd.OpenLayers.Layer.XYZ(display, uri, params);
    }-*/;

}
