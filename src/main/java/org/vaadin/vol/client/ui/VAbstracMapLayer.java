package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.layer.Layer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VAbstracMapLayer<T extends Layer> extends Widget
		implements VLayer {

	public VAbstracMapLayer() {
		setElement(Document.get().createDivElement());
	}

	private T layer;
	protected boolean layerAttached = false;
	private String displayName;
	private String projection;

	public T getLayer() {
		if (layer == null) {
			layer = createLayer();
		}
		return layer;
	}

	abstract T createLayer();

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (client.updateComponent(this, uidl, false)) {
			return;
		}
		
		displayName = uidl.hasAttribute("name") ? uidl.getStringAttribute("name") : null;
		projection = uidl.hasAttribute("projection") ? uidl.getStringAttribute("projection") : null;
		// we'll do this lazy, not in attach, so implementations can
		// customize parameters for layer constructors. Possible changes must be
		// dealt inimplementation.
		if (!layerAttached) {
			attachLayerToMap();
			layerAttached = true;
		}
	}

	protected void attachLayerToMap() {
		getMap().addLayer(getLayer());
	}

	protected Map getMap() {
		return ((VOpenLayersMap) getParent().getParent()).getMap();
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		getMap().removeLayer(getLayer());
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}
	
	protected String getProjection() {
		return projection;
	}
	
	protected String getDisplayName() {
		return displayName;
	}

}
