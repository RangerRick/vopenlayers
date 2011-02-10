package org.vaadin.vol.client.wrappers;


public class OpenStreetMapLayer extends Layer {
	
	protected OpenStreetMapLayer(){};
	
	public native final static OpenStreetMapLayer create()
	/*-{
		return new $wnd.OpenLayers.Layer.OSM.Mapnik();
	}-*/;

}
