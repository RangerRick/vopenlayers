package org.vaadin.vol.demo;

import org.vaadin.vol.Control;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

/**
 * Coordinate system is KKJ / Finland Uniform Coordinate System as is base map.
 *
 * TODO Is there a free public WMS service we could use for this test?
 *
 */
public class CustomProjectionWithWms extends AbstractVOLTest {

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        // KKJ / Finland Uniform Coordinate System
        openLayersMap.setApiProjection("EPSG:2393");
        openLayersMap.getControls().clear();
        openLayersMap.getControls().add(Control.Navigation);
        openLayersMap.getControls().add(Control.PanZoomBar);
        openLayersMap.getControls().add(Control.MousePosition);
        
        addLayer(openLayersMap);
        
        // Center on Vaadin HQ
        openLayersMap.setCenter(3241719.686,6713842.519);
        openLayersMap.setZoom(8);
        
        // Add vector on Vaadin HQ
        VectorLayer vectorLayer = new VectorLayer();
        // vector layer inherits projection from map
        // this should default to using base laer coordinates
        vectorLayer.addComponent(new PointVector(3241719.686,6713842.519));
        
        // next building (hospital) in different projection
        PointVector pointVector = new PointVector(22.2959818,60.45406552);
        // override vectors projection
        pointVector.setProjection("EPSG:4326");
        vectorLayer.addComponent(pointVector);
        
        openLayersMap.addLayer(vectorLayer);
        return openLayersMap;
    }

    protected void addLayer(OpenLayersMap openLayersMap) {
        openLayersMap.setJsMapOptions("{ maxExtent: new OpenLayers.Bounds(0,0,3750000,7850000), units: 'm', resolutions: [3172.931125, 794.034895, 264.639239, 132.291931, 52.9167724, 26.458386, 13.229193, 6.614597, 4.234905, 2.116577, 1],projection: \"EPSG:2393\"}");
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setBaseLayer(true);
        webMapServiceLayer.setUri("http://www.myserver.fi/myservice");
        webMapServiceLayer.setProjection("EPSG:2393");
        webMapServiceLayer.setLayers("mylayer");
        webMapServiceLayer.setFormat("image/png");
        openLayersMap.addLayer(webMapServiceLayer);
    }


}
