package org.vaadin.vol.client.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.management.ImmutableDescriptor;

import org.vaadin.vol.client.wrappers.Bounds;
import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.LonLat;
import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.control.LayerSwitcher;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;

/**
 * Client side widget which communicates with the server. Messages from the
 * server are shown as HTML and mouse clicks are sent to the server.
 */
public class VOpenLayersMap extends FlowPanel implements Container {

	/** Set the CSS class name to allow styling. */
	public static final String CLASSNAME = "v-openlayersmap";

	private static final Projection DEFAULT_PROJECTION = Projection
			.get("EPSG:4326");

	/** The client side widget identifier */
	protected String paintableId;

	/** Reference to the server connection object. */
	protected ApplicationConnection client;

	// private MarkerLayer markerLayer;

	HashMap<String, Widget> components = new HashMap<String, Widget>();

	private Map map = new Map();

	FlowPanel fakePaintables = new FlowPanel();

	private HashSet<String> orphanedcomponents;

	private GwtOlHandler extentChangeListener;

	private boolean immediate;

	/**
	 * The constructor should first call super() to initialize the component and
	 * then handle any initialization relevant to Vaadin.
	 */
	public VOpenLayersMap() {
		setWidth("500px");
		setHeight("500px");
		add(map);
		add(fakePaintables);
		fakePaintables.setVisible(false);

		// This method call of the Paintable interface sets the component
		// style name in DOM tree
		setStyleName(CLASSNAME);
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, final ApplicationConnection client) {

		// This call should be made first.
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true)) {
			// If client.updateComponent returns true there has been no changes
			// and we
			// do not need to update anything.
			return;
		}
		
		immediate = uidl.hasAttribute("immediate");
		
		if(extentChangeListener == null) {
			extentChangeListener = new GwtOlHandler() {
				public void onEvent(JsArray arguments) {
					int zoom = map.getZoom();
					client.updateVariable(paintableId, "zoom", zoom, false);
					Bounds extent = map.getExtent();
					if(extent == null) {
						VConsole.log(" extent null");
						return;
					}
					Projection projection = map.getProjection();
					extent.transform(projection, DEFAULT_PROJECTION);
					client.updateVariable(paintableId, "left", extent.getLeft(), false);
					client.updateVariable(paintableId, "right", extent.getRight(), false);
					client.updateVariable(paintableId, "top", extent.getTop(), false);
					client.updateVariable(paintableId, "bottom", extent.getBottom(), immediate);
				}
			};
			getMap().registerEventHandler("moveend", extentChangeListener);
			getMap().registerEventHandler("zoomed", extentChangeListener);
			
			/*
			 * Update extent on first paint.
			 */
//			extentChangeListener.onEvent(null);
		}

		map.addControl(LayerSwitcher.create());

		// Save reference to server connection object to be able to send
		// user interaction later
		this.client = client;

		// Save the client side identifier (paintable id) for the widget
		paintableId = uidl.getId();

		orphanedcomponents = new HashSet<String>(components.keySet());

		Iterator<Object> childIterator = uidl.getChildIterator();
		while (childIterator.hasNext()) {
			UIDL layerUidl = (UIDL) childIterator.next();
			orphanedcomponents.remove(layerUidl.getId());
			Paintable paintable = client.getPaintable(layerUidl);
			if (!components.containsKey(layerUidl.getId())) {
				components.put(layerUidl.getId(), (Widget) paintable);
				fakePaintables.add((Widget) paintable);
			}
			paintable.updateFromUIDL(layerUidl, client);

		}

		for (String id : orphanedcomponents) {
			Widget remove = components.remove(id);
			fakePaintables.remove(remove);
		}

		updateZoomAndCenter(uidl);

	}

	private void updateZoomAndCenter(UIDL uidl) {
		// // TODO set zoom only if marked dirty on server, also separately from
		// // center point
		
		int zoom = map.getZoom();
		if(uidl.hasAttribute("zoom")) {
			zoom = uidl.getIntAttribute("zoom");
			if(!uidl.hasAttribute("clat")) {
				// just set zoom, no center position
				map.setZoom(zoom);
			}
		}
		
		if (uidl.hasAttribute("clat")) {
			double lat = uidl.getDoubleAttribute("clat");
			double lon = uidl.getDoubleAttribute("clon");
			LonLat lonLat = LonLat.create(lon, lat);
			// expect center point to be in WSG84
			Projection projection = map.getProjection();
			lonLat.transform(DEFAULT_PROJECTION, projection);
			map.setCenter(lonLat, zoom);
		}
	}

	public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
		// TODO Auto-generated method stub

	}

	public boolean hasChildComponent(Widget component) {
		return fakePaintables.getWidgetIndex(component) != -1;
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

	public Map getMap() {
		return map;
	}
}
