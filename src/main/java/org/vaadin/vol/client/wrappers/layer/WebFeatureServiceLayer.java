package org.vaadin.vol.client.wrappers.layer;

public class WebFeatureServiceLayer extends VectorLayer {

	protected WebFeatureServiceLayer() {
	};

	public native final static WebFeatureServiceLayer create(String display,
			String url, String featureType, String ns, String projection)
	/*-{
		
            return new $wnd.OpenLayers.Layer.Vector(display, {
                'strategies': [new $wnd.OpenLayers.Strategy.BBOX()],
                'protocol': new $wnd.OpenLayers.Protocol.WFS({
                    'url': url,
                    'featureType': featureType,
                    'featureNS': ns
                })
            });
	}-*/;

    public static native final void setProxy(String stringAttribute) 
    /*-{
        $wnd.OpenLayers.ProxyHost = stringAttribute;
    }-*/;

}
