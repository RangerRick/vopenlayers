package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

public class WmsCqlFilter extends AbstractVOLTest {
    
    @Override
    public String getDescription() {
        return "Usage example of CQL queries for a WMS layer. Nonfuncitonal: no compatible public wms service.";
    }

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
        webMapServiceLayer.setBaseLayer(true);
        webMapServiceLayer.setDisplayName("Base map");
        openLayersMap.addLayer(webMapServiceLayer);

        WebMapServiceLayer cqlTest = new WebMapServiceLayer();
        cqlTest.setUri("http://www.example.com:8080/geoserver/wms?service=wms");
        cqlTest.setLayers("detail_occurrences");
        cqlTest.setTransparent(true);
        cqlTest.setFormat("image/png");
        cqlTest.setDisplayName("CQL test");
        cqlTest.setBaseLayer(false);
        cqlTest.setCqlFilter(""); // CQL here
        openLayersMap.addLayer(cqlTest);

        openLayersMap.setSizeFull();

        return openLayersMap;
    }


}
