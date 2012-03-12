package org.vaadin.vol.demo;

import org.vaadin.vol.Area;
import org.vaadin.vol.Attributes;
import org.vaadin.vol.Bounds;
import org.vaadin.vol.GoogleSatelliteMapLayer;
import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.RenderIntent;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.Symbolizer;
import org.vaadin.vol.VectorLayer;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class StyleMapAddUniqueValueRules extends AbstractVOLTest {
    private HorizontalLayout controls;

    @Override
    public String getDescription() {
        return "Example how to use complex attribute based styling.";
    }

    @Override
    protected void setup() {
        super.setup();
        ((VerticalLayout) getContent()).addComponentAsFirst(controls);
    }

    @Override
    Component getMap() {
        final OpenLayersMap map = new OpenLayersMap();
        map.setImmediate(true); // update extent and zoom to server as they
                                // change

        /*
         * Open street maps layer as a base layer. Note importance of the order,
         * OSM layer now sets the projection to Spherical Mercator. If added eg.
         * after markers or vectors, they might render with bad values.
         */
        OpenStreetMapLayer osm = new OpenStreetMapLayer();

        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        GoogleSatelliteMapLayer googleSatellite = new GoogleSatelliteMapLayer();

        final VectorLayer vectorLayer = new VectorLayer();

        /*
         * Draw triangle over vaadin hq.
         */

        Point[] points = new Point[3];
        points[0] = new Point(22.29, 60.45);
        points[1] = new Point(22.30, 60.46);
        points[2] = new Point(22.31, 60.45);

        Area area = new Area();
        area.setPoints(points);

        Style vaadinColors = new Style();
        vaadinColors.setStrokeColor("#1cffff");
        vaadinColors.setFillColor("#0000ff");
        vaadinColors.setFillOpacity(0.4);
        vaadinColors.setStrokeWidth(3);
        area.setCustomStyle(vaadinColors);

        Style defaultstyle = new Style();
        /* Set stroke color to green, otherwise like default style */
        defaultstyle.extendCoreStyle("default");
        defaultstyle.setStrokeColor("#00b963");
        defaultstyle.setFillColor("#00ff00");

        // Make borders of selected graphs bigger
        Style selectStyle = new Style();
        selectStyle.setStrokeWidth(5);

        StyleMap stylemap = new StyleMap(defaultstyle, selectStyle, null);
        // make selectStyle inherit attributes not explicitly set
        stylemap.setExtendDefault(true);
        vectorLayer.setStyleMap(stylemap);

        // setRestrictedExtent(map, points);
        zoomToExtent(map, points);

        vectorLayer.addVector(area);

        // Also create another vector that uses styleamps styles
        Area area2 = new Area();
        Point[] points2 = new Point[points.length];
        for (int i = 0; i < points2.length; i++) {
            points2[i] = new Point(points[i].getLon() + 0.02,
                    points[i].getLat());
        }
        area2.setPoints(points2);
        vectorLayer.addVector(area2);

        // Add styled PointVectors to area corners, styling with styleNames
        Style style = new Style();
        style.setFill(true);
        // style.setFillColor("#0000ff"); //
        style.setFillOpacity(0.8);
        style.setStroke(false);
        // style.setPointRadius(30);
        // style.setPointRadiusByAttribute("pointRadius"); //
        stylemap.setStyle(new RenderIntent("red"), style);
        Style markerStyle = new Style();
        // markerStyle
        // .setExternalGraphic(getURL()
        // +
        // "../VAADIN/widgetsets/org.vaadin.vol.demo.VolExampleAppWidgetset/img/marker.png");
        markerStyle.setGraphicZIndex(11);
        markerStyle.setGraphicSize(16, 21);
        // markerStyle
        // .setBackgroundGraphic(getURL()
        // +
        // "../VAADIN/widgetsets/org.vaadin.vol.demo.VolExampleAppWidgetset/img/marker_shadow.png");
        markerStyle.setBackgroundYOffset(-7);
        markerStyle.setBackgroundXOffset(0);
        markerStyle.setBackgroundGraphicZIndex(10);
        markerStyle.setFillOpacity(1);
        markerStyle.setStrokeOpacity(1);
        markerStyle.setPointRadius(10);
        stylemap.setStyle(new RenderIntent("marker"), markerStyle);

        Symbolizer symbolizer_lookup = new Symbolizer();
        Symbolizer symb = new Symbolizer();
        symb.setProperty("pointRadius", 20);
        symb.setProperty("fillColor", "#ff0000");
        symbolizer_lookup.setProperty("size0", symb);
        // symbolizer_lookup.setProperty("0", symb);

        symb = new Symbolizer();
        symb.setProperty("pointRadius", 40);
        symb.setProperty("fillColor", "#ffff00");
        symbolizer_lookup.setProperty("size1", symb);
        // symbolizer_lookup.setProperty("1", symb);

        symb = new Symbolizer();
        symb.setProperty("pointRadius", 10);
        symb.setProperty("fillColor", "#ff00ff");
        symbolizer_lookup.setProperty("size2", symb);
        // symbolizer_lookup.setProperty("2", symb);

        stylemap.addUniqueValueRules(new RenderIntent("red"), "size",
                symbolizer_lookup, null);
        stylemap.addUniqueValueRules(new RenderIntent("marker"), "size",
                symbolizer_lookup, null);

        for (int i = 0; i < points.length; i++) {
            PointVector pointVector = new PointVector(points[i].getLon(),
                    points[i].getLat());
            Attributes attr = new Attributes();

            attr.setProperty("size", "size" + String.valueOf(i));
            // attr.setProperty("size", i);
            pointVector.setAttributes(attr);

            if (i == 0) {
                pointVector.setStyleName("marker");
            } else {
                pointVector.setStyleName("red");
            }

            vectorLayer.addVector(pointVector);
        }

        map.setCenter(22.30, 60.452);
        map.setZoom(15);

        // map.setSizeFull();
        map.setWidth("800px");
        map.setHeight("600px");

        // add layers

        // base layers
        map.addLayer(osm);
        map.addLayer(googleStreets);
        map.addLayer(googleSatellite);

        map.addLayer(vectorLayer);

        controls = new HorizontalLayout();

        return map;
    }

    /**
     * An example how to zoom the map so that it covers given points.
     * 
     * @param map
     * @param points
     */
    private void zoomToExtent(OpenLayersMap map, Point[] points) {
        Bounds bounds = new Bounds(points);
        map.zoomToExtent(bounds);
    }

}
