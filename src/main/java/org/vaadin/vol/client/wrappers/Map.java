package org.vaadin.vol.client.wrappers;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget that contains open layers map. Proxys all relevant OpenLayers map
 * methods to the contained map.
 */
public class Map extends Widget {

	private static int sequense = 0;

	private MapOverlay jsoverlay;

	private final DivElement mapElement;

	public Map() {
		setElement(Document.get().createDivElement());
		mapElement = Document.get().createDivElement();
		Style style = mapElement.getStyle();
		style.setWidth(100, Unit.PCT);
		style.setHeight(100, Unit.PCT);
		setWidth("100%");
		setHeight("100%");
		getElement().appendChild(mapElement);
		String id = "VOLMAP_" + sequense++;
		mapElement.setId(id);
	}

	private MapOverlay getMap() {
		if (jsoverlay == null) {
			jsoverlay = MapOverlay.get(mapElement.getId());
		}
		return jsoverlay;
	}

	public void addControl(Control control) {
		getMap().addControl(control);
	}

	public void addLayer(Layer layer) {
		getMap().addLayer(layer);
	}
	
	public void removeLayer(Layer remove) {
		getMap().removeLayer(remove);
	}


	public void setCenter(LonLat lonLat, int zoom) {
		getMap().setCenter(lonLat, zoom);
		// TODO Auto-generated method stub

	}

	public Projection getProjection() {
		return getMap().getProjection();
	}

	public void addPopup(Popup popup) {
		getMap().addPopup(popup);
	}

	public void removePopup(Popup popup) {
		getMap().removePopup(popup);
	}

	public Layer getLayer(String id) {
		return getMap().getLayer(id);
	}

	public void removeControl(Control control) {
		getMap().removeContol(control);
	}

	public Bounds getMaxExtent() {
		return getMap().getMaxExtent();
	}

}
