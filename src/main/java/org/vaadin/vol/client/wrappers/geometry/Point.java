package org.vaadin.vol.client.wrappers.geometry;

import org.vaadin.vol.client.wrappers.Projection;



public class Point extends Geometry {

	protected Point(){};

	public native static Point create(float x, float y) 
	/*-{
		return new $wnd.OpenLayers.Geometry.Point(x,y);
	}-*/;

	public final native void transform(Projection fromProjection, Projection toProjection) 
	/*-{
		this.transform(fromProjection, toProjection);
	}-*/;

	public final native Point nativeClone() 
	/*-{
		return this.clone();
	}-*/;

	public final native double getX() 
	/*-{
		return this.x;
	}-*/;

	public final native double getY() 
	/*-{
		return this.y;
	}-*/;
	
	public final double getLongitude() {
		return getX();
	}
	
	public final double getLatitude() {
		return getY();
	}

}
