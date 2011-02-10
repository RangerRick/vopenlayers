package org.vaadin.vol.client.wrappers;


import com.google.gwt.core.client.JavaScriptObject;

public class Icon extends JavaScriptObject {
	protected Icon() {
	}

	public static native Icon create(String url, Size size) 
	/*-{
		return new $wnd.OpenLayers.Icon(url, size);
	}-*/;

}
