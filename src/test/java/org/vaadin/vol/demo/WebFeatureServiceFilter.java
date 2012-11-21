package org.vaadin.vol.demo;



import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.WebFeatureServiceLayer;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class WebFeatureServiceFilter extends AbstractVOLTest {

    @Override
    public String getDescription() {    	
        return "Dynamic filter for WFS layer";
    }

	private HorizontalLayout controls;
	private com.vaadin.ui.ComboBox filterCombo;
    
    
    @Override
    protected void setup() {
        super.setup();
        ((VerticalLayout) getContent()).addComponentAsFirst(controls);
    }
    
    private WebFeatureServiceLayer createWfsLayer(String displayName,
            String proxyUrl, String featureType) {
        WebFeatureServiceLayer wfsLayer = new WebFeatureServiceLayer();
        wfsLayer.setDisplayName(displayName);
        wfsLayer.setUri(proxyUrl);
        wfsLayer.setFeatureType(featureType);
        wfsLayer.setFeatureNS("http://www.openplans.org/topp");
        wfsLayer.setProjection("EPSG:4326");
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
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setUri("http://demo.opengeo.org/geoserver/wms");
        webMapServiceLayer.setLayers("topp:naturalearth");
        webMapServiceLayer.setDisplayName("Base map");
        openLayersMap.addLayer(webMapServiceLayer);

        String proxyUrl = getApplication().getURL()
                + "../WFSPROXY/demo.opengeo.org/geoserver/wfs";
        
        final WebFeatureServiceLayer wfsRoads = createWfsLayer("Roads", proxyUrl,
//                "tasmania_water_bodies"/*"tasmania_roads"*/);
                "tasmania_roads");        		
        setStyle(wfsRoads, 1, "orange", "orange", 0, 4);

        wfsRoads.setFilter("==","TYPE","road");
        
        openLayersMap.addLayer(wfsRoads);
        
        openLayersMap.setSizeFull();

        openLayersMap.setCenter(146.9417, -42.0429);
        openLayersMap.setZoom(7);
        
        controls = new HorizontalLayout();

        filterCombo = new com.vaadin.ui.ComboBox();
        filterCombo.setFilteringMode(Filtering.FILTERINGMODE_OFF);
        filterCombo.setImmediate(true);
        filterCombo.addItem("all");
        filterCombo.addItem("road");
        filterCombo.addItem("highway");
        filterCombo.select("road");
        filterCombo.addListener(new Property.ValueChangeListener() {			
			public void valueChange(ValueChangeEvent event) {				
				String s=(String)event.getProperty().getValue();
				if (s.equals("highway") || s.equals("road"))
					wfsRoads.setFilterAndRefresh("==","TYPE",s);
				else if (s.equals("all"))
					wfsRoads.setFilterAndRefresh("==","TYPE","");
			}
		});
        
        addComponent(filterCombo);        
        controls.addComponent(filterCombo);
        
        return openLayersMap;
    }
}
