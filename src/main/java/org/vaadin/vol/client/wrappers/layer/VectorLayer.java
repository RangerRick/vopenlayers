package org.vaadin.vol.client.wrappers.layer;

import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.StyleMap;
import org.vaadin.vol.client.wrappers.Vector;

import com.google.gwt.core.client.JavaScriptObject;

public class VectorLayer extends Layer {

    protected VectorLayer() {
    };

    /**
     * TODO name and styles
     * 
     * @return
     */
    public native final static VectorLayer create(String displayName)
    /*-{
        var layer_style = $wnd.OpenLayers.Util.extend({}, $wnd.OpenLayers.Feature.Vector.style['default']);
        layer_style.fillOpacity = 0.2;
        layer_style.graphicOpacity = 1;
    	
    	return new $wnd.OpenLayers.Layer.Vector(displayName, {styles: layer_style});
    	
    }-*/;

    public native final static VectorLayer create(String displayName,
            JavaScriptObject stylemap)
    /*-{
    	_myvector_layer = new $wnd.OpenLayers.Layer.Vector(displayName);
    	if(stylemap) 
    		_myvector_layer.styleMap = stylemap;
    	return _myvector_layer;
    	
    }-*/;

    public native final void removeFeature(Vector vector)
    /*-{
    	this.removeFeatures(vector);
    }-*/;
    
    public native final void eraseFeature(Vector vector)
    /*-{
    	this.eraseFeatures(vector);
    }-*/;

    public native final void addFeature(Vector vector)
    /*-{
    	this.addFeatures(vector);
    }-*/;

    public native final void drawFeature(Vector vector)
    /*-{
    	this.drawFeature(vector);
    }-*/;
    
    public native final void redraw()
    /*-{
        this.redraw();
    }-*/;

    public native final void setStyleMap(StyleMap style)
    /*-{
    	 this.styleMap = style;
    }-*/;

    public native final StyleMap getStyleMap(StyleMap style)
    /*-{
    	 return this.styleMap;
    }-*/;

    public native final void removeAllFeatures() 
    /*-{
        this.removeAllFeatures();
    }-*/;
    
    /**
     * it's maybe useful for blocking beforefeatureselected events
     * @param eventName
     * @param handler
     */
	public native final void registerReturnFalseHandler(String eventName, GwtOlHandler handler) 
	/*-{
		var f = function() {
			$entry(handler.@org.vaadin.vol.client.wrappers.GwtOlHandler::onEvent(Lcom/google/gwt/core/client/JsArray;)(arguments));
			return false;
		};
		this.events.addEventType(eventName);
		this.events.register(eventName,this,f);
		
	}-*/;
}
