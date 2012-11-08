package org.vaadin.vol.client.wrappers.layer;

import org.vaadin.vol.client.wrappers.AbstractOpenLayersWrapper;
import org.vaadin.vol.client.wrappers.Bounds;
import org.vaadin.vol.client.wrappers.MapOverlay;
import org.vaadin.vol.client.wrappers.Projection;


/**
 * Consider if this super class is needed at all. Make this a marker
 */
public abstract class Layer extends AbstractOpenLayersWrapper {
	protected Layer () {}

	public final native void setBaseLayer(boolean isBaseLayer) 
	/*-{
		this.setIsBaseLayer(isBaseLayer);
	}-*/;

	public final native Projection getProjection() 
	/*-{
	 	return this.projection;
	 }-*/;

	public final native MapOverlay getMap() 
	/*-{
	 	return this.map;
	 }-*/;

	public final native String getId() 
	/*-{
	 	return this.id;
	}-*/;

	public final native Bounds getMaxExtent() 
	/*-{
		return this.getMaxExtent();
	}-*/;
	
	public final native void setMaxExtent(Bounds bounds) 
	/*-{
		this.maxExtent = bounds;
	}-*/;

	public final native boolean isVisible () 
	/*-{
	 	return this.visibility;
	}-*/;
	
}
