package org.vaadin.vol.client.wrappers.layer;


public class GoogleStreetLayer extends Layer {
	
	protected GoogleStreetLayer(){};
	
	public native final static GoogleStreetLayer create()
	/*-{
		return new $wnd.OpenLayers.Layer.Google("Google Streets",
    				{numZoomLevels: 17, sphericalMercator: true});
	}-*/;

}
