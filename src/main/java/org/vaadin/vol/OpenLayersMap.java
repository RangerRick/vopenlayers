package org.vaadin.vol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import com.vaadin.event.Action;
import com.vaadin.terminal.KeyMapper;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * Server side component for the VOpenLayersMap widget.
 */
@SuppressWarnings("serial")
@com.vaadin.ui.ClientWidget(org.vaadin.vol.client.ui.VOpenLayersMap.class)
public class OpenLayersMap extends AbstractComponentContainer implements
        Action.Container {

    private final Set<Action.Handler> actionHandlers = new LinkedHashSet<Action.Handler>();
    private final KeyMapper actionMapper = new KeyMapper();
    private List<Component> layers = new LinkedList<Component>();
    private double centerLon = 0;
    private double centerLat = 0;
    private int zoom = 3;
    private boolean partialRepaint;

    private HashSet<Control> controls = new HashSet<Control>(Arrays.asList(
            Control.ArgParser, Control.Navigation, Control.TouchNavigation,
            Control.Attribution));

    public OpenLayersMap() {
        this(false);
    }

    public OpenLayersMap(boolean skipControls) {
        setWidth("500px");
        setHeight("350px");
        if (!skipControls) {
            addControl(Control.PanZoom);
            addControl(Control.LayerSwitcher);
        }
    }

    public void addControl(Control control) {
        controls.add(control);
        setDirty("controls");
    }

    public void removeControl(Control control) {
        controls.remove(control);
        setDirty("controls");
    }

    /**
     * @return set of current controls used by this map. Note that returns
     *         reference to internal data structure. If you modify the set
     *         directly, call requestRepaint for the map to force repaint.
     */
    public Set<Control> getControls() {
        return controls;
    }

    /**
     * A typed alias for {@link #addComponent(Component)}.
     *
     * @param layer
     */
    public void addLayer(Layer layer) {
        addComponent(layer);
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        layers.remove(c);
        setDirty("components");
    }

    /**
     * Adds component into the OpenLayers Map. Note that the map only supports
     * certain types of Components.
     * <p>
     * Developers are encouraged to use better typed methods instead:
     *
     * @see #addLayer(Layer)
     * @see #addPopup(Popup)
     *
     * @see com.vaadin.ui.AbstractComponentContainer#addComponent(com.vaadin.ui.Component)
     */
    @Override
    public void addComponent(Component c) {
        setDirty("components");
        super.addComponent(c);
        layers.remove(c);
        layers.add(c);
    }

    public void setCenter(double lon, double lat) {
        centerLat = lat;
        centerLon = lon;
        setDirty("clat");
    }

    /**
     * Set the center of map to the center of a bounds
     *
     */
    public void setCenter(Bounds bounds) {
        centerLat = (bounds.getBottom() + bounds.getTop()) / 2.0;
        centerLon = (bounds.getRight() + bounds.getLeft()) / 2.0;
        setDirty("clat");
    }

    public void setZoom(int zoomLevel) {
        zoom = zoomLevel;
        setDirty("zoom");
    }

    public int getZoom() {
        return zoom;
    }

    private HashSet<String> dirtyFields = new HashSet<String>();
    private boolean fullRepaint = true;
    private double top;
    private double right;
    private double bottom;
    private double left;

    private String jsMapOptions;
    private Bounds zoomToExtent;
    private Bounds restrictedExtend;
    private String projection;

    private void setDirty(String fieldName) {
        if (!fullRepaint) {
            dirtyFields.add(fieldName);
            partialPaint();
        }
    }

    private boolean isDirty(String fieldName) {
        /*
         * If full repaint if request repaint called directly or painted without
         * repaint.
         */
        if (fullRepaint || dirtyFields.isEmpty()) {
            return true;
        } else {
            return dirtyFields.contains(fieldName);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        if (isDirty("projection") && projection != null) {
            target.addAttribute("projection", projection);
        }
        if (isDirty("jsMapOptions") && jsMapOptions != null) {
            target.addAttribute("jsMapOptions", jsMapOptions);
        }

        if (isDirty("restrictedExtend") && restrictedExtend != null) {
            restrictedExtend.paint("re", target);
        }

        if (isDirty("zoomToExtent") && zoomToExtent != null) {
            zoomToExtent.paint("ze", target);
            zoomToExtent = null;
        } else {
            if (isDirty("clat")) {
                target.addAttribute("clon", centerLon);
                target.addAttribute("clat", centerLat);
            }
            if (isDirty("zoom")) {
                target.addAttribute("zoom", zoom);
            }
        }
        if (isDirty("components")) {
            target.addAttribute("componentsPainted", true);
            for (Component component : layers) {
                component.paint(target);
            }
        }
        if (isDirty("controls")) {
            target.addAttribute("controls", controls.toArray());
        }

        paintActions(target, findAndPaintBodyActions(target));

        clearPartialPaintFlags();
        fullRepaint = false;
    }

    /**
     * Receive and handle events and other variable changes from the client.
     *
     * {@inheritDoc}
     */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        if (variables.containsKey("top")) {
            updateExtent(variables);
        }

        // Actions
        if (variables.containsKey("action")) {
            String string = (String) variables.get("action");
            final StringTokenizer st = new StringTokenizer(string, ",");
            if (st.countTokens() == 2) {
                final String coords = st.nextToken();
                String[] split = coords.split(":");
                Point point = new Point(Double.parseDouble(split[0]),
                        Double.parseDouble(split[1]));
                final Action action = (Action) actionMapper.get(st.nextToken());

                if (action != null && actionHandlers != null) {
                    for (Action.Handler ah : actionHandlers) {
                        ah.handleAction(action, this, point);
                    }
                }
            }
        }
    }

    protected void updateExtent(Map<String, Object> variables) {
        int zoom = (Integer) variables.get("zoom");
        this.zoom = zoom;
        top = (Double) variables.get("top");
        right = (Double) variables.get("right");
        bottom = (Double) variables.get("bottom");
        left = (Double) variables.get("left");
    }

    /**
     * Note, this does not work until the map is rendered.
     *
     * @return
     */
    public Bounds getExtend() {
        Bounds bounds = new Bounds();
        bounds.setTop(top);
        bounds.setLeft(left);
        bounds.setRight(right);
        bounds.setBottom(bottom);
        return bounds;
    }

    public void replaceComponent(Component oldComponent, Component newComponent) {
        throw new UnsupportedOperationException();
    }

    public Iterator<Component> getComponentIterator() {
        return new LinkedList<Component>(layers).iterator();
    }

    public void addPopup(Popup popup) {
        addComponent(popup);
    }

    @Override
    public void requestRepaint() {
        if (!partialRepaint) {
            clearPartialPaintFlags();
            fullRepaint = true;
        }
        super.requestRepaint();
    }

    private void partialPaint() {
        partialRepaint = true;
        try {
            requestRepaint();
        } finally {
            partialRepaint = false;
        }
    }

    private void clearPartialPaintFlags() {
        dirtyFields.clear();
    }

    /**
     * Sets the js snippet that will be evaluated as maps custom options. See
     * OpenLayers JS api for more details.
     * <p>
     * Note, that the string will be executed as javascript on the client side.
     * VALIDATE content in case you accept input from the client.
     * <p>
     * Also note that init options only take effect if they are set before the
     * map gets rendered.
     *
     * @param jsMapOptions
     */
    public void setJsMapOptions(String jsMapOptions) {
        this.jsMapOptions = jsMapOptions;
    }

    public String getJsMapOptions() {
        return jsMapOptions;
    }

    /**
     * Zooms the map to display given bounds.
     *
     * <p>
     * Note that this method overrides possibly set center and zoom levels.
     *
     * @param bounds
     */
    public void zoomToExtent(Bounds bounds) {
        zoomToExtent = bounds;
        // also save center point for refreshes
        centerLon = (zoomToExtent.getMinLon() + zoomToExtent.getMaxLon()) / 2;
        centerLat = (zoomToExtent.getMinLat() + zoomToExtent.getMaxLat()) / 2;
        setDirty("zoomToExtent");
    }

    /**
     * Sets the area within the panning and zooming is restricted. With this
     * method developer can "limit" the area that is shown for the end user.
     * <p>
     * Note, that due the fact that open layers supports just zoom levels, the
     * displayed area might be slightly larger if the size of the component
     * don't match with the size of restricted area on minimum zoom level. If
     * area outside restricted extent may not be displayed at all, one must
     * ensure about this by either using a base layer that only contains the
     * desired area or by "masking" out the undesired area with e.g. a vector
     * layer.
     *
     * @param bounds
     */
    public void setRestrictedExtent(Bounds bounds) {
        restrictedExtend = bounds;
        setDirty("restrictedExtend");
    }

    /**
     * Sets the projection which is used by the user of this map. E.g. values
     * passed to API like {@link #setCenter(double, double)} should be in the
     * same projection.
     * <p>
     * Note that resetting projection on already rendered map may cause
     * unexpected results.
     *
     * @param projection
     */
    public void setApiProjection(String projection) {
        this.projection = projection;
        setDirty("projection");
    }

    /**
     * Gets the projection which is used by the user of this map. E.g. values
     * passed to API like {@link #setCenter(double, double)} should be in the
     * same projection.
     *
     * @return the projection used, defaults to EPSG:4326
     */
    public String getApiProjection() {
        return projection;
    }

    /**
     * Calculates an array of resolutions for use in OpenLayers map creation
     */
    protected double[] calculateResolutions(Bounds bounds, int tileSize,
      int zoomLevels) {

        final double extentWidth = bounds.getRight() - bounds.getLeft();
        final double extentHeight = bounds.getTop() - bounds.getBottom();

        double resX = extentWidth / tileSize;
        double resY = extentHeight / tileSize;

        if (resX <= resY) {
            // use one tile wide by N tiles high
            int tilesHigh = (int) Math.round(resY / resX);
            // previous resY was assuming 1 tile high, recompute with the actual number of tiles
            // high
            resY = resY / tilesHigh;
        } else {
            // use one tile high by N tiles wide
            int tilesWide = (int) Math.round(resX / resY);
            // previous resX was assuming 1 tile wide, recompute with the actual number of tiles
            // wide
            resX = resX / tilesWide;
        }

        // the maximum of resX and resY is the one that adjusts better
        final double res = Math.max(resX, resY);

        double[] resolutions = new double[zoomLevels];
        resolutions[0] = res;

        for (int i = 1; i < zoomLevels; i++) {
            resolutions[i] = resolutions[i - 1] / 2;
        }

        return resolutions;
    }

    private void paintActions(PaintTarget target, final Set<Action> actionSet)
            throws PaintException {
        if (!actionSet.isEmpty()) {
            target.addVariable(this, "action", "");
            target.startTag("actions");
            for (Action a : actionSet) {
                target.startTag("action");
                if (a.getCaption() != null) {
                    target.addAttribute("caption", a.getCaption());
                }
                if (a.getIcon() != null) {
                    target.addAttribute("icon", a.getIcon());
                }
                target.addAttribute("key", actionMapper.key(a));
                target.endTag("action");
            }
            target.endTag("actions");
        }
    }

    private Set<Action> findAndPaintBodyActions(PaintTarget target) {
        Set<Action> actionSet = new LinkedHashSet<Action>();
        if (actionHandlers != null) {
            final ArrayList<String> keys = new ArrayList<String>();
            for (Action.Handler ah : actionHandlers) {
                // Getting actions for the null item, which in this case means
                // the body item
                final Action[] actions = ah.getActions(this, this);
                if (actions != null) {
                    for (Action action : actions) {
                        actionSet.add(action);
                        keys.add(actionMapper.key(action));
                    }
                }
            }
            target.addAttribute("alb", keys.toArray());
        }
        return actionSet;
    }

    /**
     * Registers a new action handler for this container
     *
     * @see com.vaadin.event.Action.Container#addActionHandler(Action.Handler)
     */
    public void addActionHandler(Action.Handler actionHandler) {
        if (actionHandler != null) {
            if (!actionHandlers.contains(actionHandler)) {
                actionHandlers.add(actionHandler);
                requestRepaint();
            }
        }
    }

    /**
     * Removes a previously registered action handler for the contents of this
     * container.
     *
     * @see com.vaadin.event.Action.Container#removeActionHandler(Action.Handler)
     */
    public void removeActionHandler(Action.Handler actionHandler) {
        if (actionHandlers != null && actionHandlers.contains(actionHandler)) {
            actionHandlers.remove(actionHandler);
            requestRepaint();
        }
    }
}
