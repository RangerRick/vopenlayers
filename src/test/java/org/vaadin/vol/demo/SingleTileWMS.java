package org.vaadin.vol.demo;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.Point;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

/**
 * http://openlayers.org/dev/examples/wfs-states.js
 */
public class SingleTileWMS extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Use the single tile feature for WMS";
    }

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setSingleTile(true);
        webMapServiceLayer.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
        webMapServiceLayer.setBaseLayer(true);
        webMapServiceLayer.setDisplayName("Single tile");
        openLayersMap.addLayer(webMapServiceLayer);

        Bounds bounds = new Bounds(new Point(-140.4, 25.1), new Point(-44.4,
                50.5));
        openLayersMap.zoomToExtent(bounds);
        openLayersMap.setCenter(6.5, 40.5);
        openLayersMap.setZoom(4);

        return openLayersMap;
    }

}
