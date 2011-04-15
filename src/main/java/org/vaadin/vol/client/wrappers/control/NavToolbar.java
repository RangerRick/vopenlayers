package org.vaadin.vol.client.wrappers.control;


public class NavToolbar extends Control {
	protected NavToolbar() {};
	
	public static native NavToolbar create() 
	/*-{
		return new $wnd.OpenLayers.Control.NavToolbar();
	}-*/;

}
