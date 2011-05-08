package org.vaadin.vol.demo;

import java.io.IOException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.vaadin.vol.Area;
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
import org.vaadin.vol.Popup;
import org.vaadin.vol.Popup.CloseEvent;
import org.vaadin.vol.Popup.CloseListener;
import org.vaadin.vol.Popup.PopupStyle;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.DrawingMode;
import org.vaadin.vol.VectorLayer.VectorDrawnEvent;
import org.vaadin.vol.VectorLayer.VectorDrawnListener;
import org.vaadin.vol.VectorLayer.VectorModifiedEvent;
import org.vaadin.vol.WebMapServiceLayer;
import org.vaadin.vol.WebMapServiceLayerStyled;
import org.xml.sax.SAXException;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VolApplication extends Application {
	private VerticalLayout layout = new VerticalLayout();
	private HorizontalLayout controls = new HorizontalLayout();

	@Override
	public void init() {
		final Window mainWindow = new Window("Vol example Application", layout);
		setMainWindow(mainWindow);

		OpenLayersMap map = createTestMap(mainWindow);
		// OpenLayersMap map = getMapIssue1();
		// OpenLayersMap map = getMapIssue2();

		// OpenLayersMap map = getMapWithUsHighWaysOnTopOfWebMercator();
		// OpenLayersMap map = getMapWithCustomStyle();

		layout.setSizeFull();
		layout.addComponent(controls);
		layout.addComponent(map);
		layout.setExpandRatio(map, 1);

	}

	private OpenLayersMap createTestMap(final Window mainWindow) {
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

		/*
		 * Draw triangle over vaadin hq.
		 */

		Point[] points = new Point[3];
		points[0] = new Point(22.29, 60.45);
		points[1] = new Point(22.30, 60.46);
		points[2] = new Point(22.31, 60.45);

		Area area = new Area();
		area.setPoints(points);

		// setRestrictedExtent(map, points);
		zoomToExtent(map, points);

		vectorLayer.addVector(area);

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
				popup.addListener(new CloseListener() {
					public void onClose(CloseEvent event) {
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
					mainWindow
							.showNotification("Sorry, feature is on TODO list. Try area.");
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
		map.addLayer(mapTilerLayer);
		map.addLayer(vectorLayer);
		map.addLayer(markerLayer);

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

	private OpenLayersMap getMapIssue1() {
		OpenLayersMap map = new OpenLayersMap();
		// map.setImmediate(true);

		/*
		 * Open street maps layer as a base layer. Note importance of the order,
		 * OSM layer now sets the projection to Spherical Mercator. If added eg.
		 * after markers or vectors, they might render with bad values.
		 */
		OpenStreetMapLayer osm = new OpenStreetMapLayer();

		GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();

		/**
		 * Creating a WMS layer to add to OpenLayersMap
		 */

		/**
		 * Creating a MarketLayer to add to OpenLayersMap
		 */
		MarkerLayer markerLayer = new MarkerLayer();
		Marker marker = new Marker(22.30083, 60.452541);
		marker.setIcon("http://dev.vaadin.com/chrome/site/vaadin-trac.png", 60,
				20);
		markerLayer.addComponent(marker);

		/**
		 * Creating a VectorLayer to add to OpenLayersMap
		 */
		VectorLayer vectorLayer = new VectorLayer();
		vectorLayer.setDisplayName("Vector layer");
		vectorLayer.setDrawindMode(VectorLayer.DrawingMode.NONE);

		OpenStreetMapLayer osmLayer = new OpenStreetMapLayer();

		map.setCenter(22.30, 60.452);
		map.setZoom(1000);

		// base layers
		// map.addLayer(googleStreets);
		map.addLayer(osm);

		// map.addComponent(wmsLayer);
		// map.addLayer(wmsLayer);
		// map.addComponent(markerLayer);
		map.addLayer(markerLayer);
		map.addComponent(vectorLayer);
		map.addComponent(osmLayer);
		map.setSizeFull();

		return map;
	}

	private static OpenLayersMap getMapIssue2() {

		final OpenLayersMap map = new OpenLayersMap();

		// Defining a WMS layer as in OL examples
		WebMapServiceLayer wms = new WebMapServiceLayer();
		wms.setUri("http://vmap0.tiles.osgeo.org/wms/vmap0");
		wms.setLayers("basic");
		wms.setServiceType("wms");
		wms.setDisplayName("OpenLayers WMS");
		wms.setBaseLayer(true);
		map.addLayer(wms);

		// GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
		// googleStreets.setProjection("EPSG:102113");
		// map.addLayer(googleStreets);

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

		wms = new WebMapServiceLayer();
		wms.setUri("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap");
		wms.setLayers("bathymetry,land_fn,park,drain_fn,drainage,"
				+ "prov_bound,fedlimit,rail,road,popplace");
		wms.setFormat("image/png");
		wms.setDisplayName("Canadian data");
		wms.setBaseLayer(false);
		map.addLayer(wms);

		map.setSizeFull();

		return map;
	}

	/**
	 * Google projection, spherial mercator, web mercator, 900913, 1002113. All
	 * the same practically.
	 * 
	 * <p>
	 * Use js map settings and override base layer projection to 102113
	 * (~90091~...). Then works with arcgis server that don't know abot google
	 * mercator.
	 * 
	 * @return
	 */
	public OpenLayersMap getMapWithUsHighWaysOnTopOfWebMercator() {
		final OpenLayersMap map = new OpenLayersMap();

		map.setJsMapOptions("{projection: "
				+ "new OpenLayers.Projection(\"EPSG:102113\"),"
				+ "units: \"m\","
				+ "numZoomLevels: 17,"
				+ "maxResolution: 156543.0339, "
				+ "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

		GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
		googleStreets.setProjection("EPSG:102113");
		map.addLayer(googleStreets);

		OpenStreetMapLayer osm = new OpenStreetMapLayer();
		osm.setProjection("EPSG:102113");
		map.addLayer(osm);

		WebMapServiceLayer wms = new WebMapServiceLayer();
		wms.setUri("http://sampleserver1.arcgisonline.com/arcgis/services/Specialty/ESRI_StateCityHighway_USA/MapServer/WMSServer");
		wms.setLayers("2");
		wms.setTransparent(true);
		wms.setFormat("image/gif");
		wms.setDisplayName("wms");
		wms.setBaseLayer(false);
		map.addLayer(wms);

		map.setSizeFull();

		return map;

	}

	public OpenLayersMap getMapWithCustomStyle() {
		final OpenLayersMap map = new OpenLayersMap();

		map.setJsMapOptions("{projection: "
				+ "new OpenLayers.Projection(\"EPSG:900913\"),"
				+ "units: \"m\","
				+ "numZoomLevels: 22,"
				+ "maxResolution: 156543.0339, "
				+ "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

		GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
		googleStreets.setProjection("EPSG:900913");
		map.addLayer(googleStreets);

		OpenStreetMapLayer osm = new OpenStreetMapLayer();
		osm.setProjection("EPSG:900913");
		map.addLayer(osm);

		WebMapServiceLayerStyled wms = new WebMapServiceLayerStyled();
		wms.setUri("http://giswebservices.massgis.state.ma.us/geoserver/wms");
		// wms.setUri("http://127.0.0.1:8090/geoserver/wms");
		// wms.setLayers("topp:states");
		wms.setLayers("states");
		wms.setTransparent(true);
		wms.setFormat("image/gif");
		wms.setBaseLayer(false);
		wms.setDisplayName("states");
		// Be careful about namespaces in SLD like topp:states or massgis:states
		// otherwise it will render the map using defaultstyle. Basically a
		// mistake in the namespace will prevent overriding the style with the
		// one provided.
		// String sld =
		// "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>topp:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke/></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
		String sld = "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>massgis:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke><CssParameter name=\"stroke\">#FF0000</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
		wms.setSld(sld);
		map.addLayer(wms);

		map.setSizeFull();

		return map;

	}
}
