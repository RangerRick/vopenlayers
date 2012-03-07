package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapCycleLayer;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.OpenStreetMapOsmarenderLayer;

import com.vaadin.ui.Component;

public class OpenStreetMapTypes extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Demonstrates various OSM types.";
    }

    @Override
    Component getMap() {
        OpenLayersMap map = new OpenLayersMap();
        /*
         * Open street maps layer as a base layer. Note importance of the order,
         * OSM layer now sets the projection to Spherical Mercator. If added eg.
         * after markers or vectors, they might render with bad values.
         */

        map.setCenter(22.30, 60.452);
        map.setZoom(1000);

        // base layers
        map.addLayer(new OpenStreetMapLayer());
        map.addLayer(new OpenStreetMapCycleLayer());
        map.addLayer(new OpenStreetMapOsmarenderLayer());

        map.setSizeFull();

        return map;
    }

}
