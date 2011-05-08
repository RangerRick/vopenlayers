package org.vaadin.vol.client.wrappers.control;

import org.vaadin.vol.client.wrappers.Map;

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

	/**
	 * Helper to create a control by its name.
	 * 
	 * @param name
	 * @param map
	 * @return
	 */
	public static Control getControlByName(String name, Map map) {
//		if("OverviewMap".equals(name)) {
//			return OverviewMap.create(map.getBaseLayer());
//		}
		return getControlByName(name);
	}

	private native static final Control getControlByName(String name) 
	/*-{
		return new $wnd.OpenLayers.Control[name]();
	}-*/;

	public native final String getId() 
	/*-{
		return this.id;
	}-*/;

}
