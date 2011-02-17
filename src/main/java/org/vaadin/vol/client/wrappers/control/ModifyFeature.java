package org.vaadin.vol.client.wrappers.control;

import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

public class ModifyFeature extends Control {
	
	protected ModifyFeature(){}
	
	public native static ModifyFeature create(VectorLayer targetLayer)
	/*-{
		return new $wnd.OpenLayers.Control.ModifyFeature(targetLayer);
	}-*/;

	public final native Vector getModifiedFeature() 
	/*-{
		return this.feature;
	}-*/;


}
