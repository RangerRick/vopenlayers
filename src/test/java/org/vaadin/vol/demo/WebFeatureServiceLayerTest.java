package org.vaadin.vol.demo;

import java.util.Map;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.Point;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.WebFeatureServiceLayer;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

/**
 * http://openlayers.org/dev/examples/wfs-states.js
 */
public class WebFeatureServiceLayerTest extends AbstractVOLTest {

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setUri("http://tilecache.osgeo.org/wms-c/Basic.py");
        webMapServiceLayer.setBaseLayer(true);
        webMapServiceLayer.setDisplayName("Base map");
        openLayersMap.addLayer(webMapServiceLayer);

        WebFeatureServiceLayer webFeatureServiceLayer = new WebFeatureServiceLayer() {
            @Override
            protected void featureSelected(String fid, Map<String, Object> attr) {
                super.featureSelected(fid, attr);
                Object state = attr.get("STATE_NAME");
                Object persons = attr.get("PERSONS");
                showNotification("State: " + state + " (population:" + persons + ")");
            }
        };
        // proxied to http://demo.opengeo.org/geoserver/wfs
        webFeatureServiceLayer.setUri(getApplication().getURL()
                + "../WFSPROXY/");
        webFeatureServiceLayer.setFeatureType("states");
        webFeatureServiceLayer.setFeatureNS("http://www.openplans.org/topp");
        
        
        /*
         * Style like a normal web feature server.
         */
        
        Style style = new Style();
        style.extendCoreStyle("default");
        style.setFillColor("green");
        style.setFillOpacity(0.5);
        StyleMap styleMap = new StyleMap(style);
        styleMap.setExtendDefault(true);
        webFeatureServiceLayer.setStyleMap(styleMap );

        openLayersMap.addLayer(webFeatureServiceLayer);

        Bounds bounds = new Bounds(new Point(-140.4, 25.1), new Point(-44.4,
                50.5));
        openLayersMap.zoomToExtent(bounds);

        openLayersMap.setSizeFull();

        return openLayersMap;
    }

}
