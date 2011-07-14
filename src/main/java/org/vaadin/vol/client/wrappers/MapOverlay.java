package org.vaadin.vol.client.wrappers;

import org.vaadin.vol.client.wrappers.control.Control;
import org.vaadin.vol.client.wrappers.layer.Layer;
import org.vaadin.vol.client.wrappers.popup.Popup;

import com.google.gwt.core.client.JsArray;

public class MapOverlay extends AbstractOpenLayersWrapper {

    protected MapOverlay() {
    }

    /**
     * TODO parametrice options
     * 
     * @param id
     * @param initialControls
     * @return
     */
    public static native MapOverlay get(String id, String optionsJs,
            JsArray<Control> initialControls)
    /*-{
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
    	var OpenLayers = $wnd.OpenLayers;

    	var options;
    	if(optionsJs) {
       	    eval("options = " +optionsJs+ ";");
    	} else {
    	    options = {}
    	}
    	if(!options.controls) {
            options["controls"] = initialControls;
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

    public final native void zoomToExtent(Bounds bounds)
    /*-{
    	this.zoomToExtent(bounds);
    }-*/;

    public final native void setRestrictedExtent(Bounds bounds)
    /*-{
    	this.setOptions({restrictedExtent:bounds});
    }-*/;

    public final native Layer getBaseLayer()
    /*-{
    	return this.baseLayer;
    }-*/;

    public final native JsArray<Control> getControls()
    /*-{
    	return this.controls;
    }-*/;

}
