package org.vaadin.vol.client.wrappers.layer;

public class OpenStreetMapLayer extends Layer {

    protected OpenStreetMapLayer() {
    };

    /*
     * TODO refactor redundant code
     */

    public native final static OpenStreetMapLayer create(String displayName,
            String projection)
    /*-{
    	var options = {transitionEffect: 'resize'};
    	if(projection) {
    		options.projection = projection;
    	}
    	
    	if(!displayName) displayName = "OpenStreetMap";
    	var OpenLayers = $wnd.OpenLayers;
    	return new OpenLayers.Layer.OSM(displayName,null,options);
    	
    }-*/;

    public native final static OpenStreetMapLayer createCycleMap(
            String displayName, String projection)
    /*-{
        var options = {transitionEffect: 'resize'};
        if(projection) {
                options.projection = projection;
        }
        
        if(!displayName) displayName = "OpenStreetMap";
        var OpenLayers = $wnd.OpenLayers;
        return new OpenLayers.Layer.OSM.CycleMap(displayName,null,options);
    }-*/;

    public native final static OpenStreetMapLayer createOsmarenderMap(
            String displayName, String projection)
    /*-{
        var options = {transitionEffect: 'resize'};
        if(projection) {
                options.projection = projection;
        }
        
        if(!displayName) displayName = "OpenStreetMap";
        var OpenLayers = $wnd.OpenLayers;
        return new OpenLayers.Layer.OSM.Osmarender(displayName,null,options);
    }-*/;

}
