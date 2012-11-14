package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.XYZLayer;

import com.vaadin.ui.Component;

public class XyzLayerExample extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Demonstrates usage of TMS Layer.";
    }

    @Override
    Component getMap() {
        OpenLayersMap map = new OpenLayersMap();
//        map.setJsMapOptions("{maxResolution:1.40625/2}");

        map.setJsMapOptions("{projection: "
        + "new OpenLayers.Projection(\"EPSG:900913\"),"
        + "units: \"m\","
        + "numZoomLevels: 18,"
        + "maxResolution: 156543.0339, "
        + "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

        map.setCenter(22.30, 60.452);
        map.setZoom(10);

//        MapTilerLayer mapTilerLayer = new MapTilerLayer();
//        mapTilerLayer.setUri("http://tilecache.osgeo.org/wms-c/Basic.py/");
//        mapTilerLayer.setLayers("basic"); // default
//        mapTilerLayer.setType("png"); // default
//        map.addComponent(mapTilerLayer);
        
        
        XYZLayer xyzLayer = new XYZLayer();
        xyzLayer.setUri("http://tiles.kartat.kapsi.fi/peruskartta/${z}/${x}/${y}.png");
        xyzLayer.setSphericalMercator(true);
        xyzLayer.setDisplayName("Peruskartta");
        map.addComponent(xyzLayer);
        
        xyzLayer = new XYZLayer();
        xyzLayer.setUri("http://tiles.kartat.kapsi.fi/ortokuva/${z}/${x}/${y}.png");
        xyzLayer.setSphericalMercator(true);
        xyzLayer.setDisplayName("Ortokuva");
        map.addComponent(xyzLayer);
        
        map.addLayer(new OpenStreetMapLayer());

        map.setSizeFull();

        return map;
    }

}
