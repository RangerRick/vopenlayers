package org.vaadin.vol.client.wrappers;

import org.vaadin.vol.client.wrappers.layer.VectorLayer;

public class DrawFeature extends Control {
	protected DrawFeature(){}
	
	public native static DrawFeature create(VectorLayer targetLayer, Handler drawHandler)
	/*-{
		return new $wnd.OpenLayers.Control.DrawFeature(targetLayer, drawHandler);
	}-*/;

	public native final void activate() 
	/*-{
		this.activate();
	}-*/;

	public native final void deActivate() 
	/*-{
		this.deactivate();
	}-*/;

}
