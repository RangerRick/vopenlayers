package org.vaadin.vol.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.vaadin.vol.Area;
import org.vaadin.vol.Attributes;
import org.vaadin.vol.Bounds;
import org.vaadin.vol.Control;
import org.vaadin.vol.GoogleSatelliteMapLayer;
import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.MapTilerLayer;
import org.vaadin.vol.Marker;
import org.vaadin.vol.MarkerLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.Popup;
import org.vaadin.vol.Popup.PopupStyle;
import org.vaadin.vol.RenderIntent;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.DrawingMode;
import org.vaadin.vol.VectorLayer.SelectionMode;
import org.vaadin.vol.VectorLayer.VectorDrawnEvent;
import org.vaadin.vol.VectorLayer.VectorDrawnListener;
import org.vaadin.vol.VectorLayer.VectorModifiedEvent;
import org.vaadin.vol.VectorLayer.VectorSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorSelectedListener;
import org.xml.sax.SAXException;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class Demo extends AbstractVOLTest {
    private HorizontalLayout controls;

    @Override
    public String getDescription() {
        return "Simple example demonstrating some base layers and basic vector usage.";
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

        // Defining a WMS layer as in OL examples
        // WebMapServiceLayer wms = new WebMapServiceLayer();
        // wms.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
        // wms.setLayers("basic");
        // wms.setServiceType("wms");
        // wms.setDisplayName("OpenLayers WMS");
        // wms.setBaseLayer(true);
        // map.addLayer(wms);

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

        // wms = new WebMapServiceLayer();
        // wms.setUri("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap");
        // wms.setLayers("bathymetry,land_fn,park,drain_fn,drainage,"
        // + "prov_bound,fedlimit,rail,road,popplace");
        // wms.setFormat("image/png");
        // wms.setDisplayName("Canadian data");
        // wms.setBaseLayer(false);
        // map.addLayer(wms);

        MapTilerLayer mapTilerLayer = null;
        try {
            // mapTilerLayer = new
            // MapTilerLayer("http://matti.virtuallypreinstalled.com/tiles/pirttikankare/pirttikankare/");
            mapTilerLayer = new MapTilerLayer(
                    "http://dl.dropbox.com/u/4041822/pirttikankare/");
            mapTilerLayer.setDisplayName("Pirttikankare");
            mapTilerLayer.setBaseLayer(false);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final VectorLayer vectorLayer = new VectorLayer();

        vectorLayer.setSelectionMode(SelectionMode.SIMPLE);

        vectorLayer.addListener(new VectorSelectedListener() {
            public void vectorSelected(VectorSelectedEvent event) {
                Vector vector = event.getVector();
                Window window = vectorLayer.getWindow();
                vectorLayer.getWindow().showNotification(
                        "Selected vector with points "
                                + Arrays.deepToString(vector.getPoints()));
            }
        });

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
        vaadinColors.setFillColor("#adfffc");
        vaadinColors.setFillOpacity(0.4);
        vaadinColors.setStrokeWidth(3);
        area.setCustomStyle(vaadinColors);

        Style defaultstyle = new Style();
        /* Set stroke color to green, otherwise like default style */
        defaultstyle.extendCoreStyle("default");
        defaultstyle.setStrokeColor("#00b963");

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
        style.setFillColor("#ff000e");
        style.setFillOpacity(0.8);
        style.setStroke(false);
        // style.setPointRadius(30);
        style.setPointRadiusByAttribute("pointRadius");
        stylemap.setStyle(new RenderIntent("red"), style);
        Style markerStyle = new Style();
        markerStyle
                .setExternalGraphic(getURL()
                        + "../VAADIN/widgetsets/org.vaadin.vol.demo.VolExampleAppWidgetset/img/marker.png");
        markerStyle.setGraphicZIndex(11);
        markerStyle.setGraphicSize(16, 21);
        markerStyle
                .setBackgroundGraphic(getURL()
                        + "../VAADIN/widgetsets/org.vaadin.vol.demo.VolExampleAppWidgetset/img/marker_shadow.png");
        markerStyle.setBackgroundYOffset(-7);
        markerStyle.setBackgroundXOffset(0);
        markerStyle.setBackgroundGraphicZIndex(10);
        markerStyle.setFillOpacity(1);
        markerStyle.setStrokeOpacity(1);
        markerStyle.setPointRadius(10);
        stylemap.setStyle(new RenderIntent("marker"), markerStyle);

        for (int i = 0; i < points.length; i++) {
            PointVector pointVector = new PointVector(points[i].getLon(),
                    points[i].getLat());
            if (i == 0) {
                pointVector.setStyleName("marker");
            } else {
                pointVector.setStyleName("red");
                Attributes attr = new Attributes();
                attr.setProperty("pointRadius", (i + 1) * 10);
                pointVector.setAttributes(attr);

            }
            vectorLayer.addVector(pointVector);
        }

        // Definig a Marker Layer
        MarkerLayer markerLayer = new MarkerLayer();

        // Defining a new Marker

        final Marker marker = new Marker(22.30083, 60.452541);
        // URL of marker Icon
        marker.setIcon("http://dev.vaadin.com/chrome/site/vaadin-trac.png", 60,
                20);

        // Add some server side integration when clicking a marker
        marker.addClickListener(new ClickListener() {
            public void click(ClickEvent event) {
                final Popup popup = new Popup(marker.getLon(), marker.getLat(),
                        "Vaadin HQ is <em>here</em>!");
                popup.setAnchor(marker);
                popup.setPopupStyle(PopupStyle.FRAMED_CLOUD);
                popup.addListener(new Popup.CloseListener() {

                    public void onClose(org.vaadin.vol.Popup.CloseEvent event) {
                        System.err.println("Closed");
                        map.removeComponent(popup);
                    }
                });
                map.addPopup(popup);
            }
        });

        // Add the marker to the marker Layer
        markerLayer.addMarker(marker);
        map.setCenter(22.30, 60.452);
        map.setZoom(15);

        map.setSizeFull();
        // map.setWidth("600px");
        // map.setHeight("400px");

        OptionGroup drawingMode = new OptionGroup();
        for (DrawingMode l : VectorLayer.DrawingMode.values()) {
            drawingMode.addItem(l);
        }
        drawingMode.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                DrawingMode mode = (DrawingMode) event.getProperty().getValue();
                if (mode == DrawingMode.MODIFY || mode == DrawingMode.AREA
                        || mode == DrawingMode.LINE
                        || mode == DrawingMode.POINT
                        || mode == DrawingMode.NONE) {
                    vectorLayer.setDrawindMode(mode);
                } else {
                    showNotification("Sorry, feature is on TODO list. Try area.");
                }
            }
        });
        drawingMode.setValue(DrawingMode.NONE);
        drawingMode.setImmediate(true);

        vectorLayer.addListener(new VectorDrawnListener() {
            public void vectorDrawn(VectorDrawnEvent event) {
                Vector vector = event.getVector();
                vectorLayer.addVector(vector);
                vectorLayer.getWindow().showNotification(
                        "Vector drawn:" + vector);
            }
        });

        vectorLayer.addListener(new VectorLayer.VectorModifiedListener() {
            public void vectorModified(VectorModifiedEvent event) {
                vectorLayer.getWindow().showNotification(
                        "Vector modified:" + event.getVector());
            }
        });

        // add layers

        // base layers
        map.addLayer(osm);
        map.addLayer(googleStreets);
        map.addLayer(googleSatellite);

        // map.addComponent(wms);
        // map.addLayer(mapTilerLayer);
        map.addLayer(vectorLayer);
        // map.addLayer(markerLayer);

        controls = new HorizontalLayout();
        controls.addComponent(drawingMode);
        Button moveToTMSExample = new Button("Move to TMS example");
        moveToTMSExample.addListener(new Button.ClickListener() {
            public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
                map.setCenter(22.805, 60.447);
                map.setZoom(15);
            }
        });

        Panel panel = new Panel(new CssLayout());
        OptionGroup mapcontrols = new OptionGroup();
        mapcontrols.setMultiSelect(true);
        mapcontrols.setImmediate(true);
        Control[] values = Control.values();
        for (int i = 0; i < values.length; i++) {
            mapcontrols.addItem(values[i]);
        }
        mapcontrols.setValue(map.getControls());
        mapcontrols.addListener(new ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Control[] controls3 = map.getControls().toArray(
                        new Control[map.getControls().size()]);
                for (int i = 0; i < controls3.length; i++) {
                    Control control = controls3[i];
                    map.removeControl(control);
                }
                Collection<Control> value = (Collection<Control>) event
                        .getProperty().getValue();
                for (Control control : value) {
                    map.addControl(control);
                }
            }
        });
        panel.setHeight("100px");
        panel.setWidth("200px");
        panel.addComponent(mapcontrols);

        controls.addComponent(panel);

        controls.addComponent(moveToTMSExample);

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

    /**
     * An example how to restrict the displayed map so that it covers minimal
     * rectangular area that contains given points.
     * 
     * @param map
     * @param points
     */
    private void setRestrictedExtent(OpenLayersMap map, Point[] points) {
        Bounds bounds = new Bounds(points);
        map.setRestrictedExtent(bounds);
    }

    @Override
    public boolean isSuitebleOnlineDemo() {
        return true;
    }

}
