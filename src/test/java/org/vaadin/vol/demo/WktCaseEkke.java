package org.vaadin.vol.demo;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.WellKnownTextLayer;

import com.vaadin.ui.Component;

/**
 * WellKnownText test.
 */
public class WktCaseEkke extends AbstractVOLTest {

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        openLayersMap.addLayer(new OpenStreetMapLayer());

        WellKnownTextLayer wktLayer = new WellKnownTextLayer();
        String wktText = "LINESTRING(8.887336 50.055967,8.888376 50.057001,8.888543 50.057247,8.888193 50.057068)";
        wktLayer.setWellKnownText(wktText);
        openLayersMap.addLayer(wktLayer);
        Bounds bounds = new Bounds();
        bounds.extend(new Point(8.887336,50.055967), new  Point(8.888376,50.057001), new Point(8.888543, 50.057247), new Point(8.888193, 50.057068));
        openLayersMap.zoomToExtent(bounds);
        openLayersMap.setSizeFull();
        return openLayersMap;
    }

}
