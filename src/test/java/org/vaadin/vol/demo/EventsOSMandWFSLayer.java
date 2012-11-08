package org.vaadin.vol.demo;


import org.vaadin.vol.AbstractLayerBase.LoadEndEvent;
import org.vaadin.vol.AbstractLayerBase.LoadEndListener;
import org.vaadin.vol.AbstractLayerBase.LoadStartEvent;
import org.vaadin.vol.AbstractLayerBase.LoadStartListener;
import org.vaadin.vol.AbstractLayerBase.VisibilityChangedEvent;
import org.vaadin.vol.AbstractLayerBase.VisibilityChangedListener;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.WebFeatureServiceLayer;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Loads different feature types from a wfs use beforefeature select event
 * to show messages.
 * TODO - only the last added Listener catch the mouse clicks, seems to be a bug
 * in mouse click handling
 */
public class EventsOSMandWFSLayer extends AbstractVOLTest {
	private LoadStartListener loadStartListener=new LoadStartListenerImpl();
	private LoadEndListener loadEndListener=new LoadEndListenerImpl();
	private VisibilityChangedListener visChangedListener=new VisChangedListenerImpl();
	
	int start=0,end=0;
	
	private HorizontalLayout controls;
	private com.vaadin.ui.TextArea editor;
	
    @Override
    protected void setup() {
        super.setup();
        ((VerticalLayout) getContent()).addComponentAsFirst(controls);
    }
	
	
    @Override
    public String getDescription() {    	
        return "Demonstrates the use of basic layer events";
    }

    private WebFeatureServiceLayer createWfsLayer(String displayName,
            String proxyUrl, String featureType) {
        WebFeatureServiceLayer wfsLayer = new WebFeatureServiceLayer();
        wfsLayer.setDisplayName(displayName);
        wfsLayer.setUri(proxyUrl);
        wfsLayer.setFeatureType(featureType);
        wfsLayer.setFeatureNS("http://www.openplans.org/topp");
        wfsLayer.setProjection("EPSG:4326");
        wfsLayer.setSelectionCtrlId("1");
        wfsLayer.addListener(loadStartListener);
        wfsLayer.addListener(loadEndListener);
        wfsLayer.addListener(visChangedListener);
        return wfsLayer;
    }

    private void setStyle(WebFeatureServiceLayer wfs, double opacity,
            String fillColor, String strokeColor, double pointRadius,
            double strokeWidth) {
        Style style = new Style();
        style.extendCoreStyle("default");
        style.setFillColor(fillColor);
        style.setStrokeColor(strokeColor);
        style.setStrokeWidth(strokeWidth);
        style.setPointRadius(pointRadius);
        style.setFillOpacity(opacity);
        StyleMap styleMap = new StyleMap(style);
        styleMap.setExtendDefault(true);
        wfs.setStyleMap(styleMap);

    }

    @Override
    Component getMap() {    	
        OpenLayersMap openLayersMap = new OpenLayersMap();
        OpenStreetMapLayer osmLayer = new OpenStreetMapLayer();

        osmLayer.addListener(loadStartListener);
        osmLayer.addListener(loadEndListener);
        
        osmLayer.setUrl("http://b.tile.openstreetmap.org/${z}/${x}/${y}.png");
        osmLayer.setDisplayName("OSM");

        String proxyUrl = getApplication().getURL()
                + "../WFSPROXY/demo.opengeo.org/geoserver/wfs";

        WebFeatureServiceLayer wfsCities = createWfsLayer("Cities", proxyUrl,
                "tasmania_cities");
        setStyle(wfsCities, 1, "yellow", "red", 4, 2);

        WebFeatureServiceLayer wfsRoads = createWfsLayer("Roads", proxyUrl,
                "tasmania_roads");
        setStyle(wfsRoads, 1, "gray", "gray", 0, 4);
        // don't use beforeselected and selected listener at the same time to show massages
        WebFeatureServiceLayer wfsBoundaries = createWfsLayer("Boundaries",
                proxyUrl, "tasmania_state_boundaries");
        wfsBoundaries.setVisibility(false);
        WebFeatureServiceLayer wfsWater = createWfsLayer("Water", proxyUrl,
                "tasmania_water_bodies");
        setStyle(wfsWater, 0.5, "blue", "blue", 1, 2);
        openLayersMap.addLayer(osmLayer);
        openLayersMap.addLayer(wfsCities);
        openLayersMap.addLayer(wfsRoads);
        openLayersMap.addLayer(wfsWater);
        openLayersMap.addLayer(wfsBoundaries);               
        openLayersMap.setSizeFull();

        openLayersMap.setCenter(146.9417, -42.0429);
        openLayersMap.setZoom(7);
        
        controls = new HorizontalLayout();

        editor = new com.vaadin.ui.TextArea();
        editor.setRows(20);
        editor.setColumns(20);
        editor.setImmediate(true);
        addComponent(editor);        
        controls.addComponent(editor);
                
        return openLayersMap;
    }
    
    private String getLogTxt(String msg,String value) {
		String newTxt="start="+start+", end="+end;
    	return newTxt;
    }

    class LoadStartListenerImpl implements LoadStartListener {
    	public void loadStart(LoadStartEvent event) {
    		start++;
    		editor.setValue(getLogTxt("loadStart",(String)editor.getValue()));
    	};
    }
    class LoadEndListenerImpl implements LoadEndListener {
    	public void loadEnd(LoadEndEvent event) {
    		end++;
    		editor.setValue(getLogTxt("loadEnd",(String)editor.getValue()));
    	};
    }
    class VisChangedListenerImpl implements VisibilityChangedListener {
    	public void visibilityChanged(VisibilityChangedEvent event) {
    		editor.setValue(getLogTxt("visChanged vis="+event.isVisible(),(String)editor.getValue()));
    	}
    }
}
