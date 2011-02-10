package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;

public class JsObject extends JavaScriptObject {
	protected JsObject(){}
	
	public final native JavaScriptObject getFieldByName(String name)
	/*-{
		return this[name];
	}-*/;
	

}
