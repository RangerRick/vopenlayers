package org.vaadin.vol.client.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.vaadin.vol.client.wrappers.LonLat;
import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.control.LayerSwitcher;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

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
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

		// This call should be made first.
		// It handles sizes, captions, tooltips, etc. automatically.
		if (client.updateComponent(this, uidl, true)) {
			// If client.updateComponent returns true there has been no changes
			// and we
			// do not need to update anything.
			return;
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

			// String name = layer.getStringAttribute("name");
			// assert name != null;
			//
			// if (name.equals("google")) {
			// // TODO make this work with Gmaps V3
			// GoogleOptions params = new GoogleOptions();
			// params.setType(GMapType.G_SATELLITE_MAP);
			// params.setSphericalMercator(false);
			// params.setIsBaseLayer(layer.getBooleanAttribute("isBaseLayer"));
			// Google google = new Google("Google Satelitte", params);
			//
			// getMap().addLayers(new Layer[] { google });
			// } else if (name.equals("osm")) {
			//
			// OSM osm = OSM.Mapnik(null);
			// OSM osmarender = OSM.Osmarender("Osmarender");
			// osm.setIsBaseLayer(layer.getBooleanAttribute("isBaseLayer"));
			// osmarender.setIsBaseLayer(layer.getBooleanAttribute("isBaseLayer"));
			// getMap().addLayer(osm);
			// getMap().addLayer(osmarender);
			//
			// } else if (name.equals("markers")) {
			// markerLayer = new MarkerLayer("Markers");
			// markerLayer.updateFromUIDL(layer);
			// getMap().addLayer(markerLayer);
			// } else if (name.equals("wms")) {
			// // Defining a WMSLayer and adding it to a Map
			// WMSParams wmsParams = new WMSParams();
			// wmsParams.setFormat("image/jpeg");
			// wmsParams.setLayers(layer.getStringAttribute("layer"));
			//
			// if (layer.hasAttribute("featureid")) {
			// if (!layer.getStringAttribute("featureid").equals("")) {
			// // wmsParams.setFilters(layer.getStringAttribute("featureid"));
			// }
			// }
			// WMSOptions wmsOptions = new WMSOptions();
			// wmsOptions.setIsBaseLayer(layer
			// .getBooleanAttribute("isBaseLayer"));
			// if (layer.hasAttribute("opacity")) {
			// wmsOptions.setLayerOpacity(layer
			// .getDoubleAttribute("opacity"));
			// }
			// if (layer.hasAttribute("projection")) {
			// wmsOptions.setProjection(layer
			// .getStringAttribute("projection"));
			// }
			//
			// WMS wms = new WMS(layer.getStringAttribute("display"),
			// layer.getStringAttribute("uri"), wmsParams, wmsOptions);
			//
			// // TODO save by name/id or similar, targeted updates, best
			// // would be to make layers vaadin widgets somehow ->
			// // framework
			// // would point updates to right place
			// getMap().addLayer(wms);
			//
			// }
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
		int zoom = uidl.getIntAttribute("zoom");
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
