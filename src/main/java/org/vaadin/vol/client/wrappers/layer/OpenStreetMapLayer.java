package org.vaadin.vol.client.wrappers.layer;


public class OpenStreetMapLayer extends Layer {
	
	protected OpenStreetMapLayer(){};
	
	public native final static OpenStreetMapLayer create(String displayName, String projection)
	/*-{

		var options = {transitionEffect: 'resize'};
		if(projection) {
			options.projection = projection;
		}
		
		if(!displayName) displayName = "OpenStreetMap";
		var OpenLayers = $wnd.OpenLayers;
		return new OpenLayers.Layer.OSM(displayName,null,options);
		
	}-*/;

}
