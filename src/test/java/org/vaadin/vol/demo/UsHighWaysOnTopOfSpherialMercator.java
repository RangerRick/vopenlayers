package org.vaadin.vol.demo;

import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

/**
 * Google projection, spherial mercator, web mercator, 900913, 1002113. All the
 * same practically.
 * 
 * <p>
 * Use js map settings and override base layer projection to 102113
 * (~90091~...). Then works with arcgis server that don't know abot google
 * mercator.
 * 
 */
public class UsHighWaysOnTopOfSpherialMercator extends AbstractVOLTest {

    @Override
    Component getMap() {
        final OpenLayersMap map = new OpenLayersMap();

        map.setJsMapOptions("{projection: "
                + "new OpenLayers.Projection(\"EPSG:102113\"),"
                + "units: \"m\","
                + "numZoomLevels: 17,"
                + "maxResolution: 156543.0339, "
                + "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        googleStreets.setProjection("EPSG:102113");
        map.addLayer(googleStreets);

        OpenStreetMapLayer osm = new OpenStreetMapLayer();
        osm.setProjection("EPSG:102113");
        map.addLayer(osm);

        WebMapServiceLayer wms = new WebMapServiceLayer();
        wms.setUri("http://sampleserver1.arcgisonline.com/arcgis/services/Specialty/ESRI_StateCityHighway_USA/MapServer/WMSServer");
        wms.setLayers("2");
        wms.setTransparent(true);
        wms.setFormat("image/gif");
        wms.setDisplayName("wms");
        wms.setBaseLayer(false);
        map.addLayer(wms);

        map.setSizeFull();

        return map;
    }

}
