package org.vaadin.vol.demo;

import org.vaadin.vol.Marker;
import org.vaadin.vol.MarkerLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Component;

public class MarkerAddingAndRemoving extends AbstractVOLTest implements Handler {

    private static final Action ADD = new Action("ADD MARKER");
    private static final Action TOGGLE = new Action("TOGGLE LAYER");
    private static final Action[] ACTIONS = new Action[] { ADD, TOGGLE };
    private MarkerLayer markerLayer;
    private OpenLayersMap map;

    @Override
    public String getDescription() {
        return "In this example markers can be added with context menu. When marker is clicked, it gets removed. Also one can toggle marker layer via context menu.";
    }

    @Override
    Component getMap() {
        map = new OpenLayersMap();
        /*
         * Open street maps layer as a base layer. Note importance of the order,
         * OSM layer now sets the projection to Spherical Mercator. If added eg.
         * after markers or vectors, they might render with bad values.
         */

        map.setCenter(22.30, 60.452);
        map.setZoom(1000);

        // base layers
        map.addLayer(new OpenStreetMapLayer());

        map.addActionHandler(this);

        markerLayer = new MarkerLayer();

        map.addLayer(markerLayer);

        addMarker(22.30, 60.452);

        map.setSizeFull();

        return map;
    }

    @Override
    public void attach() {
        super.attach();
        showNotification(getDescription(), Notification.TYPE_WARNING_MESSAGE);
    }

    private void addMarker(double lon, double lat) {
        final Marker marker = new Marker(lon, lat);
        markerLayer.addMarker(marker);
        ClickListener listener = new ClickListener() {
            public void click(ClickEvent event) {
                markerLayer.removeMarker(marker);
            }
        };
        marker.addClickListener(listener);
    }

    public Action[] getActions(Object target, Object sender) {
        return ACTIONS;
    }

    public void handleAction(Action action, Object sender, Object target) {
        if (action == ADD) {
            Point point = (Point) target;
            addMarker(point.getLon(), point.getLat());
        } else if (action == TOGGLE) {
            if (markerLayer.getParent() == null) {
                // add layer
                map.addLayer(markerLayer);
            } else {
                // remove layer
                map.removeLayer(markerLayer);
            }
        }

    }

}
