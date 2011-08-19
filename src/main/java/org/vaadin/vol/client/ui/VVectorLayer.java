package org.vaadin.vol.client.ui;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.JsObject;
import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.control.Control;
import org.vaadin.vol.client.wrappers.control.DrawFeature;
import org.vaadin.vol.client.wrappers.control.ModifyFeature;
import org.vaadin.vol.client.wrappers.geometry.Geometry;
import org.vaadin.vol.client.wrappers.geometry.LineString;
import org.vaadin.vol.client.wrappers.geometry.Point;
import org.vaadin.vol.client.wrappers.handler.PathHandler;
import org.vaadin.vol.client.wrappers.handler.PointHandler;
import org.vaadin.vol.client.wrappers.handler.PolygonHandler;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VVectorLayer extends FlowPanel implements VLayer, Container {

    private VectorLayer vectors;
    private String drawingMode = "NONE";
    private Control df;
    private GwtOlHandler _fAddedListener;
    private boolean updating;
    private ApplicationConnection client;
    private String displayName;
    private GwtOlHandler _fModifiedListener;

    public VectorLayer getLayer() {
        if (vectors == null) {
            vectors = VectorLayer.create(displayName);
            vectors.registerHandler("featureadded", getFeatureAddedListener());
            vectors.registerHandler("featuremodified",
                    getFeatureModifiedListener());
            vectors.registerHandler("afterfeaturemodified", new GwtOlHandler() {
                public void onEvent(JsArray arguments) {
                    client.sendPendingVariableChanges();
                }
            });
        }
        return vectors;
    }

    private GwtOlHandler getFeatureModifiedListener() {
        if (_fModifiedListener == null) {
            _fModifiedListener = new GwtOlHandler() {

                public void onEvent(JsArray arguments) {
                    if (!updating && drawingMode != "NONE") {
                        // use vaadin js object helper to get the actual feature
                        // from ol event
                        // TODO improve typing of OL events
                        JsObject event = arguments.get(0).cast();
                        Vector feature = event.getFieldByName("feature").cast();
                        Geometry geometry = feature.getGeometry();

                        boolean isLineString = true; // TODO
                        if (isLineString) {
                            LineString ls = geometry.cast();
                            JsArray<Point> allVertices = ls.getAllVertices();
                            client.updateVariable(paintableId,
                                    "newVerticesProj", getMap().getProjection()
                                            .toString(), false);
                            String[] points = new String[allVertices.length()];
                            for (int i = 0; i < allVertices.length(); i++) {
                                Point point = allVertices.get(i);
                                point = point.nativeClone();
                                point.transform(getMap().getProjection(),
                                        Projection.get("EPSG:4326"));
                                points[i] = point.toString();
                            }
                            // VConsole.log("modified");
                            // communicate points to server and mark the
                            // new geometry to be removed on next update.
                            client.updateVariable(paintableId, "vertices",
                                    points, false);

                            Vector modifiedFeature = ((ModifyFeature) df.cast())
                                    .getModifiedFeature();
                            Iterator<Widget> iterator = iterator();
                            while (iterator.hasNext()) {
                                VAbstractVector next = (VAbstractVector) iterator
                                        .next();
                                Vector vector = next.getVector();
                                if (vector == modifiedFeature) {
                                    client.updateVariable(paintableId,
                                            "modifiedVector", next, false);
                                    break;
                                }
                            }

                            // client.sendPendingVariableChanges();
                            // lastNewDrawing = feature;
                        }
                    }
                }
            };
        }
        return _fModifiedListener;
    }

    private String paintableId;
    private Vector lastNewDrawing;
    private boolean added = false;

    private GwtOlHandler getFeatureAddedListener() {
        if (_fAddedListener == null) {
            _fAddedListener = new GwtOlHandler() {

                public void onEvent(JsArray arguments) {
                    if (!updating && drawingMode != "NONE") {
                        // use vaadin js object helper to get the actual feature
                        // from ol event
                        // TODO improve typing of OL events
                        JsObject event = arguments.get(0).cast();
                        Vector feature = event.getFieldByName("feature").cast();
                        Geometry geometry = feature.getGeometry();

                        if (drawingMode == "AREA" || drawingMode == "LINE") {
                            LineString ls = geometry.cast();
                            JsArray<Point> allVertices = ls.getAllVertices();
                            client.updateVariable(paintableId,
                                    "newVerticesProj", getMap().getProjection()
                                            .toString(), false);
                            String[] points = new String[allVertices.length()];
                            for (int i = 0; i < allVertices.length(); i++) {
                                Point point = allVertices.get(i);
                                point.transform(getMap().getProjection(),
                                        Projection.get("EPSG:4326"));
                                points[i] = point.toString();
                            }
                            VConsole.log("drawing done");
                            client.updateVariable(paintableId, "vertices",
                                    points, false);
                        } else if (drawingMode == "POINT") {
                            // point
                            Point point = geometry.cast();
                            point.transform(getMap().getProjection(),
                                    Projection.get("EPSG:4326"));
                            double x = point.getX();
                            double y = point.getY();
                            client.updateVariable(paintableId, "x", x, false);
                            client.updateVariable(paintableId, "y", y, false);
                        }
                        // communicate points to server and mark the
                        // new geometry to be removed on next update.
                        client.sendPendingVariableChanges();
                        lastNewDrawing = feature;
                    }
                }
            };
        }
        return _fAddedListener;
    }

    public void updateFromUIDL(UIDL layer, ApplicationConnection client) {
        if (client.updateComponent(this, layer, false)) {
            return;
        }
        this.client = client;
        paintableId = layer.getId();
        updating = true;
        displayName = layer.getStringAttribute("name");
        if (!added) {
            getMap().addLayer(getLayer());
            added = true;
        }

        // Last new drawing only visible to next update. If used by server side
        // handler, we probably have it as a child component
        if (lastNewDrawing != null) {
            getLayer().removeFeature(lastNewDrawing);
            lastNewDrawing = null;
        }

        HashSet<Widget> orphaned = new HashSet<Widget>();
        for (Iterator<Widget> iterator = iterator(); iterator.hasNext();) {
            orphaned.add(iterator.next());
        }

        int childCount = layer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            UIDL childUIDL = layer.getChildUIDL(i);
            VAbstractVector vector = (VAbstractVector) client
                    .getPaintable(childUIDL);
            boolean isNew = !hasChildComponent(vector);
            if (isNew) {
                add(vector);
            }
            vector.updateFromUIDL(childUIDL, client);
            orphaned.remove(vector);
        }
        for (Widget widget : orphaned) {
            widget.removeFromParent();
        }
        setDrawingMode(layer.getStringAttribute("dmode"));
        updating = false;
    }

    private void setDrawingMode(String newDrawingMode) {
        newDrawingMode = newDrawingMode.intern();
        if (drawingMode != newDrawingMode) {
            if (drawingMode != "NONE") {
                // remove old drawing feature
                df.deActivate();
                getMap().removeControl(df);
            }
            drawingMode = newDrawingMode;
            df = null;

            if (drawingMode == "AREA") {
                df = DrawFeature.create(getLayer(), PolygonHandler.get());
            } else if (drawingMode == "LINE") {
                df = DrawFeature.create(getLayer(), PathHandler.get());
            } else if (drawingMode == "MODIFY") {
                df = ModifyFeature.create(getLayer());
            } else if (drawingMode == "POINT") {
                df = DrawFeature.create(getLayer(), PointHandler.get());
            }
            if (df != null) {
                getMap().addControl(df);
                df.activate();
            }

        }
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        getMap().removeLayer(getLayer());
    }

    private Map getMap() {
        return ((VOpenLayersMap) getParent().getParent()).getMap();
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        // TODO Auto-generated method stub

    }

    public boolean hasChildComponent(Widget component) {
        return getWidgetIndex(component) != -1;
    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // TODO Auto-generated method stub

    }

    public boolean requestLayout(Set<Paintable> children) {
        // TODO Auto-generated method stub
        return false;
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        // TODO Auto-generated method stub
        return null;
    }

}
