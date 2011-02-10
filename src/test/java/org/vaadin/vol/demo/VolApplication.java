package org.vaadin.vol.demo;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.vaadin.vol.Area;
import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.Marker;
import org.vaadin.vol.MarkerLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.Point;
import org.vaadin.vol.Popup;
import org.vaadin.vol.Popup.CloseEvent;
import org.vaadin.vol.Popup.CloseListener;
import org.vaadin.vol.Popup.PopupStyle;
import org.vaadin.vol.MapTilerLayer;
import org.vaadin.vol.Vector;
import org.vaadin.vol.VectorLayer;
import org.vaadin.vol.VectorLayer.DrawingMode;
import org.vaadin.vol.VectorLayer.VectorDrawnEvent;
import org.vaadin.vol.VectorLayer.VectorDrawnListener;
import org.xml.sax.SAXException;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VolApplication extends Application {
	private VerticalLayout layout = new VerticalLayout();
	private HorizontalLayout controls = new HorizontalLayout();

	@Override
	public void init() {
		final Window mainWindow = new Window("Vol example Application", layout);
		setMainWindow(mainWindow);
		final OpenLayersMap map = new OpenLayersMap();

		/*
		 * Open street maps layer as a base layer. Note importance of the order,
		 * OSM layer now sets the projection to Spherical Mercator. If added eg.
		 * after markers or vectors, they might render with bad values.
		 */
		OpenStreetMapLayer osm = new OpenStreetMapLayer();

		GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();

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
//			mapTilerLayer = new MapTilerLayer("http://matti.virtuallypreinstalled.com/tiles/pirttikankare/pirttikankare/");
			mapTilerLayer = new MapTilerLayer("http://dl.dropbox.com/u/4041822/pirttikankare/");
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
		mapTilerLayer.setDisplayName("Pirttikankare");
		mapTilerLayer.setBaseLayer(false);
		

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
				Popup popup = new Popup(marker.getLon(), marker.getLat(),
						"Vaadin HQ is <em>here</em>!");
				popup.setAnchor(marker);
				popup.setPopupStyle(PopupStyle.FRAMED_CLOUD);
				popup.addListener(new CloseListener() {
					public void onClose(CloseEvent event) {
						System.err.println("Closed");
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
//		map.setWidth("600px");
//		map.setHeight("400px");

		layout.setSizeFull();
		layout.addComponent(controls);
		layout.addComponent(map);
		layout.setExpandRatio(map, 1);

		OptionGroup drawingMode = new OptionGroup();
		for (DrawingMode l : VectorLayer.DrawingMode.values()) {
			drawingMode.addItem(l);
		}
		drawingMode.addListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				DrawingMode mode = (DrawingMode) event.getProperty()
				.getValue();
				if(mode == DrawingMode.AREA || mode == DrawingMode.NONE) {
					vectorLayer.setDrawindMode(mode );
				} else {
					mainWindow.showNotification("Sorry, feature is on TODO list. Try area.");
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
		
		// add layers
		
		// base layers
		map.addLayer(googleStreets);
		map.addLayer(osm);

		
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
		controls.addComponent(moveToTMSExample );

	}

}
