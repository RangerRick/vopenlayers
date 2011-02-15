package org.vaadin.vol.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.vaadin.vol.client.wrappers.DrawFeature;
import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.JsObject;
import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.PathHandler;
import org.vaadin.vol.client.wrappers.PolygonHandler;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.geometry.Geometry;
import org.vaadin.vol.client.wrappers.geometry.LineString;
import org.vaadin.vol.client.wrappers.geometry.Point;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.VConsole;

public class VVectorLayer extends FlowPanel implements VLayer, Container {

	private VectorLayer vectors;
	private String drawingMode = "NONE";
	private DrawFeature df;
	private GwtOlHandler _fAddedListener;
	private boolean updating;
	private ApplicationConnection client;
	private String displayName;

	public VectorLayer getLayer() {
		if (vectors == null) {
			vectors = VectorLayer.create(displayName);
			vectors.registerHandler("featureadded", getFeatureAddedListener());
		}
		return vectors;
	}

	private String paintableId;
	private Vector lastNewDrawing;
	private boolean added = false;

	private GwtOlHandler getFeatureAddedListener() {
		if (_fAddedListener == null) {
			_fAddedListener = new GwtOlHandler() {

				public void onEvent(JsArray arguments) {
					if (!updating && drawingMode != "NONE") {
						// use vaadin js object helper to get the actual feature
						// from ol event
						// TODO improve typing of OL events
						JsObject event = arguments.get(0).cast();
						Vector feature = event.getFieldByName("feature").cast();
						Geometry geometry = feature.getGeometry();

						if (drawingMode == "AREA" || drawingMode == "LINE") {
							LineString ls = geometry.cast();
							JsArray<Point> allVertices = ls.getAllVertices();
							client.updateVariable(paintableId,
									"newVerticesProj", getMap().getProjection()
											.toString(), false);
							String[] points = new String[allVertices.length()];
							for (int i = 0; i < allVertices.length(); i++) {
								Point point = allVertices.get(i);
								point.transform(getMap().getProjection(),
										Projection.get("EPSG:4326"));
								points[i] = point.toString();
							}
							VConsole.log("drawing done");
							// communicate points to server and mark the
							// new geometry to be removed on next update.
							client.updateVariable(paintableId, "vertices",
									points, false);
							client.sendPendingVariableChanges();
							lastNewDrawing = feature;
						}
					}
				}
			};
		}
		return _fAddedListener;
	}

	public void updateFromUIDL(UIDL layer, ApplicationConnection client) {
		if (client.updateComponent(this, layer, false)) {
			return;
		}
		this.client = client;
		this.paintableId = layer.getId();
		updating = true;
		displayName = layer.getStringAttribute("name");
		if(!added ) {
			getMap().addLayer(getLayer());
			added = true;
		}

		// Last new drawing only visible to next update. If used by server side
		// handler, we probably have it as a child component
		if(lastNewDrawing != null) {
			getLayer().removeFeature(lastNewDrawing);
			lastNewDrawing = null;
		}
		

		HashSet<Widget> orphaned = new HashSet<Widget>();
		for(Iterator<Widget> iterator = iterator();iterator.hasNext();) {
			orphaned.add(iterator.next());
		}

		int childCount = layer.getChildCount();
		for (int i = 0; i < childCount; i++) {
			UIDL childUIDL = layer.getChildUIDL(i);
			VAbstractVector vector = (VAbstractVector) client
					.getPaintable(childUIDL);
			boolean isNew = !hasChildComponent(vector);
			if (isNew) {
				add(vector);
			}
			vector.updateFromUIDL(childUIDL, client);
			orphaned.remove(vector);
		}
		for (Widget widget : orphaned) {
			widget.removeFromParent();
		}
		setDrawingMode(layer.getStringAttribute("dmode"));
		updating = false;
	}

	private void setDrawingMode(String newDrawingMode) {
		newDrawingMode = newDrawingMode.intern();
		if (drawingMode != newDrawingMode) {
			if (drawingMode != "NONE") {
				// remove old drawing feature
				df.deActivate();
				getMap().removeControl(df);
			}
			drawingMode = newDrawingMode;
			df = null;

			if (drawingMode == "AREA") {
				df = DrawFeature.create(getLayer(), PolygonHandler.get());
			} else if (drawingMode == "LINE") {
				df = DrawFeature.create(getLayer(), PathHandler.get());
			} else if (drawingMode == "POINT") {
				// TODO
			}
			if (df != null) {
				getMap().addControl(df);
				df.activate();
			}

		}
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		getMap().removeLayer(getLayer());
	}

	private Map getMap() {
		return ((VOpenLayersMap) getParent().getParent()).getMap();
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
