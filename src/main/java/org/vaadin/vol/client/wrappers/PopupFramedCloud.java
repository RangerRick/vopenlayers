package org.vaadin.vol.client.wrappers;

import com.google.gwt.core.client.JavaScriptObject;


public class PopupFramedCloud extends Popup {

	protected PopupFramedCloud(){};
	
	public native static PopupFramedCloud create(LonLat lonlat, Size size, String contenHtml, JavaScriptObject anchor, boolean closable, GwtOlHandler onclosehandler)
	/*-{

		if(onclosehandler) {
			var f = function(evt) {
				onclosehandler.@org.vaadin.vol.client.wrappers.GwtOlHandler::onEvent(Lcom/google/gwt/core/client/JsArray;)(arguments);
			};
		}
		return new $wnd.OpenLayers.Popup.FramedCloud(null, lonlat, size, contenHtml, anchor.size ? anchor : anchor.icon, closable, f);
	}-*/;

}
