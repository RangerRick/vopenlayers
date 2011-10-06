package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;

/**
 * Coordinate system is KKJ / Finland Uniform Coordinate System
 * 
 * FIXME "EPSG:2393" points over 900913 (OSM) don't appear to fit perfectly, an
 * issue with proj4js configuration?
 */
public class CustomProjectionWithOpenStreetMap extends CustomProjectionWithWms {

    protected void addBaseLayer(OpenLayersMap openLayersMap) {
        openLayersMap.addLayer(new OpenStreetMapLayer());

    }

}
