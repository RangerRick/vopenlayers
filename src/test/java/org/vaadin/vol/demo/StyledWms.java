package org.vaadin.vol.demo;

import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.WebMapServiceLayerStyled;

import com.vaadin.ui.Component;

public class StyledWms extends AbstractVOLTest {

    @Override
    Component getMap() {
        final OpenLayersMap map = new OpenLayersMap();

        map.setJsMapOptions("{projection: "
                + "new OpenLayers.Projection(\"EPSG:900913\"),"
                + "units: \"m\","
                + "numZoomLevels: 22,"
                + "maxResolution: 156543.0339, "
                + "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        googleStreets.setProjection("EPSG:900913");
        map.addLayer(googleStreets);

        OpenStreetMapLayer osm = new OpenStreetMapLayer();
        osm.setProjection("EPSG:900913");
        map.addLayer(osm);

        WebMapServiceLayerStyled wms = new WebMapServiceLayerStyled();
        wms.setUri("http://giswebservices.massgis.state.ma.us/geoserver/wms");
        // wms.setUri("http://127.0.0.1:8090/geoserver/wms");
        // wms.setLayers("topp:states");
        wms.setLayers("states");
        wms.setTransparent(true);
        wms.setFormat("image/gif");
        wms.setBaseLayer(false);
        wms.setDisplayName("states");
        // Be careful about namespaces in SLD like topp:states or massgis:states
        // otherwise it will render the map using defaultstyle. Basically a
        // mistake in the namespace will prevent overriding the style with the
        // one provided.
        // String sld =
        // "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>topp:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke/></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        String sld = "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>massgis:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke><CssParameter name=\"stroke\">#FF0000</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        wms.setSld(sld);
        map.addLayer(wms);

        map.setSizeFull();

        return map;
    }


}
