package org.vaadin.vol.demo;

import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.Marker;
import org.vaadin.vol.MarkerLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.VectorLayer;

import com.vaadin.ui.Component;

public class MapIssue1 extends AbstractVOLTest {

    @Override
    Component getMap() {
        OpenLayersMap map = new OpenLayersMap();
        // map.setImmediate(true);

        /*
         * Open street maps layer as a base layer. Note importance of the order,
         * OSM layer now sets the projection to Spherical Mercator. If added eg.
         * after markers or vectors, they might render with bad values.
         */
        OpenStreetMapLayer osm = new OpenStreetMapLayer();

        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();

        /**
         * Creating a WMS layer to add to OpenLayersMap
         */

        /**
         * Creating a MarketLayer to add to OpenLayersMap
         */
        MarkerLayer markerLayer = new MarkerLayer();
        Marker marker = new Marker(22.30083, 60.452541);
        marker.setIcon("http://dev.vaadin.com/chrome/site/vaadin-trac.png", 60,
                20);
        markerLayer.addComponent(marker);

        /**
         * Creating a VectorLayer to add to OpenLayersMap
         */
        VectorLayer vectorLayer = new VectorLayer();
        vectorLayer.setDisplayName("Vector layer");
        vectorLayer.setDrawindMode(VectorLayer.DrawingMode.NONE);

        OpenStreetMapLayer osmLayer = new OpenStreetMapLayer();

        map.setCenter(22.30, 60.452);
        map.setZoom(1000);

        // base layers
        // map.addLayer(googleStreets);
        map.addLayer(osm);

        // map.addComponent(wmsLayer);
        // map.addLayer(wmsLayer);
        // map.addComponent(markerLayer);
        map.addLayer(markerLayer);
        map.addComponent(vectorLayer);
        map.addComponent(osmLayer);
        map.setSizeFull();

        return map;
    }


}
