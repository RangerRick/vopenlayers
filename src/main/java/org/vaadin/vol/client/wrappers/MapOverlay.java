package org.vaadin.vol.client.wrappers;

import org.vaadin.vol.client.wrappers.control.Control;
import org.vaadin.vol.client.wrappers.layer.Layer;
import org.vaadin.vol.client.wrappers.popup.Popup;

import com.google.gwt.core.client.JavaScriptObject;

public class MapOverlay extends AbstractOpenLayersWrapper {

	protected MapOverlay() {
	}

	/**
	 * TODO parametrice options
	 * 
	 * @param id
	 * @return
	 */
	public static native MapOverlay get(String id)
	/*-{
	            var options = {
	            // for only map tiler base layer, use these
	//	                projection: new $wnd.OpenLayers.Projection("EPSG:900913"),
	//	                displayProjection: new $wnd.OpenLayers.Projection("EPSG:4326"),
	//	                units: "m",
	//	                maxResolution: 156543.0339,
	//	                maxExtent: new $wnd.OpenLayers.Bounds(-20037508, -20037508, 20037508, 20037508.34)
		            };
		
		 //
		 // Rude hack to add "firebug lite" to GWT iframe (actually to all iframes). 
		 // Without this some OL functions may fail without modern browser or firebug.
		 //
		var iframes = $doc.getElementsByTagName("iframe");
		for(var i = 0; i < iframes.length; i++) {
			var frame = iframes[i].contentWindow;
			try {
				if(!frame.console) {
					var e = function(){};
					frame.console = {log:e,error:e,dir:e,debug:e,info:e,warn:e,error:e,userError:e,assert:e,dirXml:e}
				}
			} catch(e){};
		}

		return new $wnd.OpenLayers.Map(id, options);
	}-*/;

	public final native void addControl(Control control)
	/*-{
		this.addControl(control);
	}-*/;

	public final native void addLayer(Layer layer)
	/*-{
		this.addLayer(layer);
	}-*/;

	public final native void setCenter(LonLat lonLat, int zoom)
	/*-{
		this.setCenter(lonLat,zoom);
	}-*/;

	public final native Projection getProjection()
	/*-{
		return this.getProjectionObject();
	}-*/;

	public final native void removeLayer(Layer remove)
	/*-{
		this.removeLayer(remove);
	}-*/;

	public final native void addPopup(Popup popup)
	/*-{
		this.addPopup(popup);
	}-*/;

	public final native void removePopup(Popup popup)
	/*-{
		this.removePopup(popup);
	}-*/;

	public final native Layer getLayer(String id)
	/*-{
		return this.getLayer(id);
	}-*/;

	public final native void removeContol(Control control)
	/*-{
		this.removeControl(control);
	}-*/;

	public final native Bounds getMaxExtent()
	/*-{
		return this.getMaxExtent();
	}-*/;

	public final native int getZoom()
	/*-{
		return this.getZoom();
	}-*/;

	public final native void zoomTo(int zoom)
	/*-{
		this.zoomTo(zoom);
	}-*/;

	public final native Bounds getExtent() 
	/*-{
		return this.getExtent();
	}-*/;

}
