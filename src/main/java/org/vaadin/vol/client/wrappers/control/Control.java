package org.vaadin.vol.client.wrappers.control;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class Control extends JavaScriptObject {

	protected Control () {}
	
	public native final void activate() 
	/*-{
		this.activate();
	}-*/;

	public native final void deActivate() 
	/*-{
		this.deactivate();
	}-*/;

}
