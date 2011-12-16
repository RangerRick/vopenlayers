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
    public String getDescription() {
        return "How to use WMS layer with non standard projection. Demo does not work (no public WMS server suitable known for this purpose).";
    }

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        // KKJ / Finland Uniform Coordinate System
        openLayersMap.setApiProjection("EPSG:2393");
        openLayersMap.getControls().clear();
        openLayersMap.getControls().add(Control.Navigation);
        openLayersMap.getControls().add(Control.PanZoomBar);
        openLayersMap.getControls().add(Control.MousePosition);

        addBaseLayer(openLayersMap);

        // Center on Vaadin HQ
        openLayersMap.setCenter(3241692.676, 6713854.524);
        openLayersMap.setZoom(8);

        // Add vector on Vaadin HQ
        VectorLayer vectorLayer = new VectorLayer();
        // vector layer inherits projection from map
        // this should default to using base layer coordinates 2393
        vectorLayer.addComponent(new PointVector(3241692.676, 6713854.524));

        /*
         * This works fine from 900913 and 4326 align perfectly with 900913 base
         * layer, but the third marker should be on same spot (Defined with
         * 2393, projection tested with kansalaisen.karttapaikka.fi). If a base
         * layer is in 2393, 4326 and 2393 align ok, 900913 badly. So apparently
         * direct transforms 900913 <> 2393 are so broken.
         */
        PointVector pointVector = new PointVector(2482517.039867359,
                8501187.741456728);
        pointVector.setProjection("EPSG:900913");
        vectorLayer.addComponent(pointVector);
        pointVector = new PointVector(22.30083, 60.452541);
        pointVector.setProjection("EPSG:4326");
        vectorLayer.addComponent(pointVector);

        // next building (hospital) in different projection
        pointVector = new PointVector(22.2959818, 60.45406552);
        // override vectors projection
        pointVector.setProjection("EPSG:4326");
        vectorLayer.addComponent(pointVector);

        openLayersMap.addLayer(vectorLayer);
        return openLayersMap;
    }

    protected void addBaseLayer(OpenLayersMap openLayersMap) {
        openLayersMap
                .setJsMapOptions("{ maxExtent: new OpenLayers.Bounds(0,0,3750000,7850000), units: 'm', resolutions: [3172.931125, 794.034895, 264.639239, 132.291931, 52.9167724, 26.458386, 13.229193, 6.614597, 4.234905, 2.116577, 1],projection: \"EPSG:2393\"}");
        WebMapServiceLayer webMapServiceLayer = new WebMapServiceLayer();
        webMapServiceLayer.setBaseLayer(true);
        webMapServiceLayer.setUri("http://www.example.fi/wms");
        webMapServiceLayer.setProjection("EPSG:2393");
        webMapServiceLayer.setLayers("wms");
        webMapServiceLayer.setFormat("image/png");
        openLayersMap.addLayer(webMapServiceLayer);
    }

}
