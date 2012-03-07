package org.vaadin.vol.demo;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.PolyLine;
import org.vaadin.vol.VectorLayer;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;

/**
 * Example where one can drag points or squares with context menu.
 */
public class ActionHandlers extends AbstractVOLTest implements Handler {

    private VectorLayer vectorLayer;
    private OpenLayersMap openLayersMap;

    private void addBaseLayer(OpenLayersMap openLayersMap) {
        openLayersMap.addLayer(new OpenStreetMapLayer());

        vectorLayer = new VectorLayer();
        openLayersMap.addLayer(vectorLayer);

        openLayersMap.addActionHandler(this);
        openLayersMap.setImmediate(true); // to get extent eagerly, used to draw
                                          // relatively sized squares
        this.openLayersMap = openLayersMap;

    }

    @Override
    public String getDescription() {
        return "Example where one can drag points or squares with context menu";
    }

    private static final Action POINT = new Action("Add Point");
    private static final Action RECT = new Action("Add Rectangle");
    private static final Action[] ACTIONS = new Action[] { POINT, RECT };

    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    public void handleAction(Action action, Object sender, Object target) {
        Point point = (Point) target;
        if (action == POINT) {
            PointVector pointVector = new PointVector();
            pointVector.setPoints(point);
            vectorLayer.addVector(pointVector);
        } else { // RECT
            Bounds extend = openLayersMap.getExtend();
            double left = extend.getLeft();
            double top = extend.getTop();
            double right = extend.getRight();
            double bottom = extend.getBottom();

            double dx = (right - left) / 10;
            double dy = (top - bottom) / 10;

            left = point.getLon() - dx;
            right = point.getLon() + dx;
            bottom = point.getLat() - dy;
            top = point.getLat() + dy;
            PolyLine polyLine = new PolyLine();
            polyLine.setPoints(new Point(left, top), new Point(right, top),
                    new Point(right, bottom), new Point(left, bottom),
                    new Point(left, top));
            vectorLayer.addVector(polyLine);
        }

    }

    @Override
    OpenLayersMap getMap() {
        OpenLayersMap openLayersMap = new OpenLayersMap();
        addBaseLayer(openLayersMap);
        return openLayersMap;
    }

}
