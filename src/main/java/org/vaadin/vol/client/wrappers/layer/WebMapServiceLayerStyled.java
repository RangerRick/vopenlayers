package org.vaadin.vol.client.wrappers.layer;

public class WebMapServiceLayerStyled extends WebMapServiceLayer
{

    protected WebMapServiceLayerStyled() {
	};
    
    public native final static WebMapServiceLayerStyled create(String display, 
		String url, String layers, String format, boolean isBaseLayer, boolean transparent, String sld)
    /*-{ 
      	var params = {};
	if(layers) params.layers = layers;
	if(format) params.format = format;
	params.transparent = transparent;
	if(sld) params.sld_body = sld;
      	var options = {};
	options.isBaseLayer = isBaseLayer;
     	return new $wnd.OpenLayers.Layer.WMS.Post(display, url, params, options);
    }-*/;
}
