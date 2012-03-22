package org.vaadin.vol.demo;

import java.util.Iterator;

import org.vaadin.vol.Bounds;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.PolyLine;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.SelectionMode;
import org.vaadin.vol.VectorLayer.VectorSelectedEvent;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;

/**
 * Example where one can drag points or squares with context menu.
 */
public class RemovingSelectedVector extends AbstractVOLTest implements Handler, ClickListener {

    private VectorLayer vectorLayer;
    private OpenLayersMap openLayersMap;
    protected Vector selected;
    private Button removeSelected = new Button("Remove selected", this);
    private Button removeAll = new Button("Remove all but selected", this);

    private void addBaseLayer(OpenLayersMap openLayersMap) {
        openLayersMap.addLayer(new OpenStreetMapLayer());

        vectorLayer = new VectorLayer();
        openLayersMap.addLayer(vectorLayer);
        vectorLayer.setSelectionMode(SelectionMode.SIMPLE);
        vectorLayer.addListener(new VectorLayer.VectorSelectedListener() {
            public void vectorSelected(VectorSelectedEvent event) {
                selected = event.getVector();
            }
        });

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
        
        getContent().addComponent(removeSelected);
        getContent().addComponent(removeAll);
        OpenLayersMap openLayersMap = new OpenLayersMap();
        addBaseLayer(openLayersMap);
        return openLayersMap;
    }

    public void buttonClick(ClickEvent event) {
        if(event.getButton() == removeAll) {
            Iterator<Component> componentIterator = vectorLayer.getComponentIterator();
            while(componentIterator.hasNext()) {
                Component next = componentIterator.next();
                if(next != selected) {
                    vectorLayer.removeComponent(next);
                }
            }
        } else if (event.getButton() == removeSelected) {
            vectorLayer.removeComponent(selected);
        }
        
    }

}
