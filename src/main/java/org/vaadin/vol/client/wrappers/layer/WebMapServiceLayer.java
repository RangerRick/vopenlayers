package org.vaadin.vol.client.wrappers.layer;

public class WebMapServiceLayer extends Layer {

	protected WebMapServiceLayer() {
	};

	public native final static WebMapServiceLayer create(String display,
			String url, String layers, String format, boolean isBaseLayer, boolean transparent)
	/*-{
		var params = {};
		if(layers) params.layers = layers;
		if(format) params.format = format;
		params.transparent = transparent;
		var options = {};
		options.isBaseLayer = isBaseLayer;
		return new $wnd.OpenLayers.Layer.WMS(display, url, params, options);
	}-*/;

}
