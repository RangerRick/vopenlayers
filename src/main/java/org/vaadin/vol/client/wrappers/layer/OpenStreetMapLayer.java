package org.vaadin.vol.client.wrappers.layer;


public class OpenStreetMapLayer extends Layer {
	
	protected OpenStreetMapLayer(){};
	
	public native final static OpenStreetMapLayer create(String displayName, String projection)
	/*-{

		var options = {sphericalMercator: true};
		if(projection) {
			options.projection = projection;
		}
		
		if(!displayName) displayName = "OpenStreetMap";
		
		return new $wnd.OpenLayers.Layer.OSM.Mapnik(displayName, options);
		
	}-*/;

}
