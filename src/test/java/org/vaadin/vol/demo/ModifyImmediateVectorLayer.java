package org.vaadin.vol.demo;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.PolyLine;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.DrawingMode;
import org.vaadin.vol.VectorLayer.SelectionMode;
import org.vaadin.vol.VectorLayer.VectorModifiedEvent;
import org.vaadin.vol.VectorLayer.VectorSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorUnSelectedEvent;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.MethodProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class ModifyImmediateVectorLayer extends AbstractVOLTest implements
        VectorLayer.VectorModifiedListener {

    @Override
    public String getDescription() {
        return "Tests complex situations with vector editing (both server and client side), selection and vector removal all together.";
    }

    private HorizontalLayout controls;
    VectorLayer vectorLayer = new VectorLayer();

    private List<Vector> vectors = new ArrayList<Vector>();
    private NativeSelect nativeSelect;

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

        OpenStreetMapLayer osm = new OpenStreetMapLayer();

        vectorLayer.setImmediate(true);
        vectorLayer.setDrawindMode(DrawingMode.MODIFY);

        /*
         * Set thicker stroke width so editing the polyline is easier.
         */
        Style style = new Style();
        style.setStrokeWidth(3);
        StyleMap styleMap = new StyleMap(style);
        styleMap.setExtendDefault(true);
        vectorLayer.setStyleMap(styleMap);

        Point[] pa = new Point[4];
        pa[0] = new Point(12.4109, 41.8092);
        PointVector pvA1 = new PointVector(pa[0].getLon(), pa[0].getLat());
        pvA1.setDebugId("1");
        vectors.add(pvA1);
        pvA1.select();
        pa[1] = new Point(12.4459, 41.9829);
        PointVector pvA2 = new PointVector(pa[1].getLon(), pa[1].getLat());
        pvA2.setDebugId("2");
        vectors.add(pvA2);
        pa[2] = new Point(12.6150, 41.8968);
        PointVector pvA3 = new PointVector(pa[2].getLon(), pa[2].getLat());
        pvA3.setDebugId("3");
        vectors.add(pvA3);

        pa[3] = new Point(12.4947, 41.9000);
        PointVector pvB1 = new PointVector(pa[3].getLon(), pa[3].getLat());
        pvB1.setDebugId("4");
        vectors.add(pvB1);
        PolyLine polyLine = new PolyLine();
        polyLine.setPoints(pa);
        polyLine.setDebugId("polyline");
        vectors.add(polyLine);

        vectorLayer.addVector(polyLine);

        vectorLayer.addVector(pvA1);
        vectorLayer.addVector(pvA2);
        vectorLayer.addVector(pvA3);
        vectorLayer.addVector(pvB1);

        zoomToExtent(map, pa);

        map.setSizeFull();

        vectorLayer.addListener(new VectorLayer.VectorSelectedListener() {
            public void vectorSelected(VectorSelectedEvent event) {
                if (nativeSelect.getValue() != event.getVector()) {
                    nativeSelect.setValue(event.getVector());
                }
            }
        });

        vectorLayer.addListener(new VectorLayer.VectorUnSelectedListener() {
            public void vectorUnSelected(VectorUnSelectedEvent event) {
                Vector vector = event.getVector();
                nativeSelect.setValue(null);
            }

        });

        vectorLayer.addListener(new ComponentDetachListener() {
            public void componentDetachedFromContainer(
                    ComponentDetachEvent event) {
                vectorLayer.getWindow().showNotification(
                        "Vector removed (Component detach event).");
            }
        });

        vectorLayer.addListener((VectorLayer.VectorModifiedListener) this);

        // add layers
        map.addLayer(osm);
        map.addLayer(vectorLayer);

        controls = new HorizontalLayout();

        CheckBox checkBox = new CheckBox("Immediate vector layer");
        checkBox.setPropertyDataSource(new MethodProperty<VectorLayer>(
                vectorLayer, "immediate"));
        checkBox.setImmediate(true);
        controls.addComponent(checkBox);

        controls.addComponent(new Button("Toggle editable-plainselectable",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        if (vectorLayer.getDrawindMode() == DrawingMode.MODIFY) {
                            vectorLayer.setDrawindMode(DrawingMode.NONE);
                            vectorLayer.setSelectionMode(SelectionMode.SIMPLE);
                            vectorLayer.getWindow().showNotification(
                                    "Selections only");
                        } else {
                            vectorLayer.setSelectionMode(SelectionMode.NONE);
                            vectorLayer.setDrawindMode(DrawingMode.MODIFY);
                            vectorLayer.getWindow().showNotification(
                                    "Modifications allowed");
                        }
                    }
                }));

        controls.addComponent(new Button("Sync"));

        controls.addComponent(new Button("Shift sel component",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        Vector vector = vectorLayer.getSelectedVector();
                        if (vector != null) {
                            Bounds extend = map.getExtend();
                            double toRight = (extend.getRight() - extend
                                    .getLeft()) / 10.0;
                            Point[] points = vector.getPoints();
                            for (Point point : points) {
                                point.setLon(point.getLon() + toRight);
                            }
                            vector.requestRepaint();
                        }
                    }
                }));

        controls.addComponent(new Button("Remove sel component",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        Vector vector = vectorLayer.getSelectedVector();
                        if (vector != null) {
                            nativeSelect.removeItem(vector);
                            vectorLayer.removeComponent(vector);
                        }
                    }
                }));

        nativeSelect = new NativeSelect();
        nativeSelect.setNullSelectionAllowed(true);
        for (Vector point : vectors) {
            nativeSelect.addItem(point);
            nativeSelect.setItemCaption(point, "Vector :" + point.getDebugId());
        }
        nativeSelect.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object value = event.getProperty().getValue();
                if (value instanceof Vector) {
                    vectorLayer.setSelectedVector((Vector) value);
                } else {
                    vectorLayer.setSelectedVector(null);
                }
            }
        });
        nativeSelect.setImmediate(true);
        controls.addComponent(nativeSelect);

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

    public void vectorModified(VectorModifiedEvent event) {
        vectorLayer.getWindow().showNotification(
                "Vector modified::" + event.getVector().getDebugId());
    }

}
