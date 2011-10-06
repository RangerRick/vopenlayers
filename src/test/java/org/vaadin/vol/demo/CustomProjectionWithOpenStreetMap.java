package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;

/**
 * Coordinate system is KKJ / Finland Uniform Coordinate System
 * 
 * FIXME "EPSG:2393" points over 9009l3 (OSM) don't appear to fit perfectly
 */
public class CustomProjectionWithOpenStreetMap extends CustomProjectionWithWms {
    
    protected void addBaseLayer(OpenLayersMap openLayersMap) {
        openLayersMap.addLayer(new OpenStreetMapLayer());
    }

}
