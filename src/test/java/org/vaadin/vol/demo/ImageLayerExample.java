package org.vaadin.vol.demo;

import java.util.Arrays;

import org.vaadin.vol.ImageLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.DrawingMode;
import org.vaadin.vol.VectorLayer.VectorDrawnEvent;
import org.vaadin.vol.VectorLayer.VectorDrawnListener;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

public class ImageLayerExample extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Demonstrates usage of ImageLayer.";
    }

    @Override
    Component getMap() {
        OpenLayersMap map = new OpenLayersMap();

        String url = "http://earthtrends.wri.org/images/maps/4_m_citylights_lg.gif";

        int width = 580;
        int height = 288;
        map.setWidth(width, UNITS_PIXELS);
        map.setHeight(height, UNITS_PIXELS);

        ImageLayer imgLayer = new ImageLayer(url, width, height);
        // imgLayer.setBaseLayer(true);
        // Defaults to imgLayer.setBounds(-180d, -90d, 180d, 90d);
        imgLayer.setBounds(-180d, -88.759d, 180d, 88.759d);
        imgLayer.setDisplayName("Image layer: city lights");
        map.addLayer(imgLayer);

        // Add wms layer as reference
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
        webMapServiceLayer.setLayers("basic");
        webMapServiceLayer.setDisplayName("WMS");
        map.addLayer(webMapServiceLayer);

        final VectorLayer vectorLayer = new VectorLayer();
        vectorLayer.setDrawingMode(DrawingMode.LINE);
        vectorLayer.addListener(new VectorDrawnListener() {
            public void vectorDrawn(VectorDrawnEvent event) {
                Vector vector = event.getVector();
                vectorLayer.addComponent(vector);
                getWindow().showNotification(
                        "Route drawn with points: "
                                + Arrays.toString(vector.getPoints()));
            }
        });
        map.addLayer(vectorLayer);
        map.setZoom(0);

        return map;
    }

}
