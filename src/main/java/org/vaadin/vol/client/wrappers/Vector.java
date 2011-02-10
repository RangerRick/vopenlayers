package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;


public abstract class Vector extends AbstractOpenLayersWrapper {

	protected Vector(){};
	
	/**
	 * TODO typing especially for style
	 * 
	 * @param geom
	 * @param attributes
	 * @param style
	 * @return
	 */
	public static native final Vector create(Geometry geom, JavaScriptObject attributes, JavaScriptObject style)
	/*-{
		return new $wnd.OpenLayers.Feature.Vector(geom, attributes, style);
	}-*/;
	
	public native final Geometry getGeometry() 
	/*-{
		return this.geometry;
	}-*/;

	
	
}
