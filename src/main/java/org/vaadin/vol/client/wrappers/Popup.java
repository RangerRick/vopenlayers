package org.vaadin.vol.client.wrappers;


public class Popup extends AbstractOpenLayersWrapper {

	protected Popup(){};
	
	public native static Popup create(LonLat lonlat, Size size, String contenHtml, boolean closable, GwtOlHandler onclosehandler)
	/*-{

		if(onclosehandler) {
			var f = function(evt) {
				// TODO add event ??
				onclosehandler.@org.vaadin.vol.client.wrappers.GwtOlHandler::onEvent(Lcom/google/gwt/core/client/JsArray;)(arguments);
			};
		}
		return new $wnd.OpenLayers.Popup(null, lonlat, size, contenHtml, closable, f);
	}-*/;

	public final native void hide() 
	/*-{
		this.hide();
	}-*/;

}
