package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.LonLat;
import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.Marker;
import org.vaadin.vol.client.wrappers.Popup;
import org.vaadin.vol.client.wrappers.PopupAnchored;
import org.vaadin.vol.client.wrappers.PopupAnchoredBubble;
import org.vaadin.vol.client.wrappers.PopupFramed;
import org.vaadin.vol.client.wrappers.PopupFramedCloud;
import org.vaadin.vol.client.wrappers.Projection;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPopup extends Widget implements Paintable {

	private enum PopupStyle {
		DEFAULT, ANCHORED, ANCHORED_BUBBLE, FRAMED, FRAMED_CLOUD
	}

	private Popup popup;
	private GwtOlHandler closeEventHandler = new GwtOlHandler() {
		public void onEvent(JsArray arguments) {
			if (client.hasEventListeners(VPopup.this, "close")) {
				client.updateVariable(client.getPid(VPopup.this), "close", "",
						true);
			}
			popup.hide();
		}
	};
	private ApplicationConnection client;

	public VPopup() {
		setElement(Document.get().createDivElement());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.terminal.gwt.client.Paintable#updateFromUIDL(com.vaadin.terminal
	 * .gwt.client.UIDL, com.vaadin.terminal.gwt.client.ApplicationConnection)
	 */
	public void updateFromUIDL(UIDL childUIDL,
			final ApplicationConnection client) {
		if (client.updateComponent(this, childUIDL, false)) {
			return;
		}
		this.client = client;

		/*
		 * With Popups, we always remove the old one and add a new one.
		 */

		if (popup != null) {
			getMap().removePopup(popup);
		}
		double lon = childUIDL.getDoubleAttribute("lon");
		double lat = childUIDL.getDoubleAttribute("lat");
		final Projection projection = Projection.get(childUIDL
				.getStringAttribute("pr"));
		LonLat point = LonLat.create(lon, lat);
		Projection projection2 = getMap().getProjection();
		point.transform(projection, projection2);

		final String content = childUIDL.getStringAttribute("content");

		PopupStyle style = PopupStyle.valueOf(childUIDL
				.getStringAttribute("style"));

		// TODO remove marker dependency
		Marker anchor = null;
		if (childUIDL.hasAttribute("anchor")) {
			Paintable paintableAttribute = childUIDL.getPaintableAttribute("anchor", client);
			if (paintableAttribute != null) {
				VMarker vanchor = (VMarker) paintableAttribute;
				anchor = vanchor.getMarker();
			}
		}

		switch (style) {
		case ANCHORED:
			popup = PopupAnchored.create(point, null, content, anchor.cast(),
					true, closeEventHandler);
			break;
		case ANCHORED_BUBBLE:
			popup = PopupAnchoredBubble.create(point, null, content, anchor.cast(),
					true, closeEventHandler);
			break;
		case FRAMED:
			popup = PopupFramed.create(point, null, content, anchor.cast(),
					true, closeEventHandler);
			break;
		case FRAMED_CLOUD:
			popup = PopupFramedCloud.create(point, null, content, anchor.cast(),
					true, closeEventHandler);
			break;

		case DEFAULT:
		default:
			popup = Popup.create(point, null, content, true, closeEventHandler);
			break;
		}

		getMap().addPopup(popup);
	}

	private Map getMap() {
		return ((VOpenLayersMap) getParent().getParent()).getMap();
	}

	public Popup getPopup() {
		return popup;
	}

}
