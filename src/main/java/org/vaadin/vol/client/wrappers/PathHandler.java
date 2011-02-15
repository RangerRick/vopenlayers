package org.vaadin.vol.client.wrappers;

public class PathHandler extends Handler {
	protected PathHandler() {}
	
	public final native static PathHandler get()
	/*-{
		return $wnd.OpenLayers.Handler.Path;
	 }-*/;

}
