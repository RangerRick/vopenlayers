package org.vaadin.vol.demo;

import org.vaadin.vol.AbstractAutoPopulatedVectorLayer.FeatureSelectedEvent;
import org.vaadin.vol.AbstractAutoPopulatedVectorLayer.FeatureSelectedListener;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.WebFeatureServiceLayer;

import com.vaadin.ui.Component;

/**
 * http://openlayers.org/dev/examples/wfs-states.js
 */
public class WebFeatureServiceLayerTest2 extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Just another WFS example.";
    }

    private WebFeatureServiceLayer createWfsLayer(String displayName,
            String proxyUrl, String featureType) {
        WebFeatureServiceLayer wfsLayer = new WebFeatureServiceLayer();
        wfsLayer.setDisplayName(displayName);
        wfsLayer.setUri(proxyUrl);
        wfsLayer.setFeatureType(featureType);
        wfsLayer.setFeatureNS("http://www.openplans.org/topp");
        wfsLayer.setProjection("EPSG:4326");
        return wfsLayer;
    }

    private void setStyle(WebFeatureServiceLayer wfs, double opacity,
            String fillColor, String strokeColor, double pointRadius,
            double strokeWidth) {
        Style style = new Style();
        style.extendCoreStyle("default");
        style.setFillColor(fillColor);
        style.setStrokeColor(strokeColor);
        style.setStrokeWidth(strokeWidth);
        style.setPointRadius(pointRadius);
        style.setFillOpacity(opacity);
        StyleMap styleMap = new StyleMap(style);
        styleMap.setExtendDefault(true);
        wfs.setStyleMap(styleMap);

    }

    @Override
    Component getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        OpenStreetMapLayer osmLayer = new OpenStreetMapLayer();
        osmLayer.setUrl("http://b.tile.openstreetmap.org/${z}/${x}/${y}.png");
        osmLayer.setDisplayName("OSM");

        String proxyUrl = getApplication().getURL()
                + "../WFSPROXY/demo.opengeo.org/geoserver/wfs";

        WebFeatureServiceLayer wfsCities = createWfsLayer("Cities", proxyUrl,
                "tasmania_cities");
        setStyle(wfsCities, 1, "yellow", "red", 4, 2);

        WebFeatureServiceLayer wfsRoads = createWfsLayer("Roads", proxyUrl,
                "tasmania_roads");
        setStyle(wfsRoads, 1, "gray", "gray", 0, 4);
        wfsRoads.addListener(new FeatureSelectedListener() {
            public void featureSelected(FeatureSelectedEvent event) {
                Object typeName = event.getAttributes().get("TYPE");
                showNotification("Road type: " + typeName);
            }
        });

        WebFeatureServiceLayer wfsBoundaries = createWfsLayer("Boundaries",
                proxyUrl, "tasmania_state_boundaries");
        wfsBoundaries.setEnabled(false);
        WebFeatureServiceLayer wfsWater = createWfsLayer("Water", proxyUrl,
                "tasmania_water_bodies");
        setStyle(wfsWater, 0.5, "blue", "blue", 1, 2);

        openLayersMap.addLayer(osmLayer);
        openLayersMap.addLayer(wfsCities);
        openLayersMap.addLayer(wfsRoads);
        openLayersMap.addLayer(wfsWater);
        openLayersMap.addLayer(wfsBoundaries);
        openLayersMap.setSizeFull();

        openLayersMap.setCenter(146.9417, -42.0429);
        openLayersMap.setZoom(7);

        return openLayersMap;
    }

}
