package org.vaadin.vol.client.ui;

import java.util.Set;

import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.MarkerLayer;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMarkerLayer extends FlowPanel implements VLayer, Container {

	private MarkerLayer markers;
	private String name;
	private boolean layerAdded;

	public MarkerLayer getLayer() {
		if(markers == null) {
			markers = MarkerLayer.create(name);
		}
		return markers;
	}

	public void updateFromUIDL(UIDL layer, ApplicationConnection client) {
		if(client.updateComponent(this, layer, false)) {
			return;
		}
		name = layer.getStringAttribute("name");
		if(!layerAdded) {
			getMap().addLayer(getLayer());
			layerAdded = true;
		}
		int childCount = layer.getChildCount();
		for (int i = 0; i < childCount; i++) {
			UIDL childUIDL = layer.getChildUIDL(i);
			VMarker marker = (VMarker) client.getPaintable(childUIDL);
			boolean isNew = !hasChildComponent(marker);
			if(isNew) {
				add(marker);
			}
			marker.updateFromUIDL(childUIDL, client);
		}
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		getMap().removeLayer(getLayer());
	}
	
	
	private Map getMap() {
		return ((VOpenLayersMap)getParent().getParent()).getMap();
	}

	public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
		// TODO Auto-generated method stub
		
	}

	public boolean hasChildComponent(Widget component) {
		return getWidgetIndex(component) != -1;
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

}
