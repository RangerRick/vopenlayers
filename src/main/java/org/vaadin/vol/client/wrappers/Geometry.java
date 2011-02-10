package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;


public abstract class Geometry extends JavaScriptObject {

	protected Geometry(){};
	
	
	public native final Bounds getBounds() 
	/*-{
		return this.getBounds();
	}-*/;
	

	
	
}
