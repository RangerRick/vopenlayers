package org.vaadin.vol.client.wrappers;


public class VectorLayer extends Layer {

	protected VectorLayer() {
	};

	
	/**
	 * TODO name and styles
	 * @return
	 */
	public native final static VectorLayer create(String displayName)
	/*-{

        var layer_style = $wnd.OpenLayers.Util.extend({}, $wnd.OpenLayers.Feature.Vector.style['default']);
        layer_style.fillOpacity = 0.2;
        layer_style.graphicOpacity = 1;
		
		return new $wnd.OpenLayers.Layer.Vector(displayName, {styles: layer_style});
		
	}-*/;

	public native final void removeFeature(Vector vector) 
	/*-{
		this.removeFeatures(vector);
	}-*/;

	public native final void addFeature(Vector vector) 
	/*-{
		this.addFeatures(vector);
	}-*/;
	
}
