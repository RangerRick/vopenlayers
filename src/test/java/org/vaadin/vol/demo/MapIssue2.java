package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.WebMapServiceLayer;

import com.vaadin.ui.Component;

public class MapIssue2 extends AbstractVOLTest {

    @Override
    Component getMap() {
        final OpenLayersMap map = new OpenLayersMap();

        // Defining a WMS layer as in OL examples
        WebMapServiceLayer wms = new WebMapServiceLayer();
        wms.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
        wms.setLayers("basic");
        wms.setServiceType("wms");
        wms.setDisplayName("OpenLayers WMS");
        wms.setBaseLayer(true);
        map.addLayer(wms);

        // GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        // googleStreets.setProjection("EPSG:102113");
        // map.addLayer(googleStreets);

        // OL example data from canada
        // var dm_wms = new OpenLayers.Layer.WMS(
        // "Canadian Data",
        // "http://www2.dmsolutions.ca/cgi-bin/mswms_gmap",
        // {
        // layers: "bathymetry,land_fn,park,drain_fn,drainage," +
        // "prov_bound,fedlimit,rail,road,popplace",
        // transparent: "true",
        // format: "image/png"
        // },
        // {isBaseLayer: false, visibility: false}
        // );

        wms = new WebMapServiceLayer();
        wms.setUri("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap");
        wms.setLayers("bathymetry,land_fn,park,drain_fn,drainage,"
                + "prov_bound,fedlimit,rail,road,popplace");
        wms.setFormat("image/png");
        wms.setDisplayName("Canadian data");
        wms.setBaseLayer(false);
        map.addLayer(wms);

        map.setSizeFull();

        return map;
    }


}
