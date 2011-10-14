package org.vaadin.vol.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.StyleMap;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.control.SelectFeature;
import org.vaadin.vol.client.wrappers.format.WKT;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

import com.google.gwt.core.client.JsArray;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ValueMap;

public abstract class VAbstractAutopopulatedVectorLayer<T> extends
		VAbstracMapLayer<VectorLayer> {

	private Boolean isBaseLayer;
	private Double opacity;
	private boolean transparent;
	private String display;
	private ApplicationConnection client;
	protected VectorLayer layer;
	private SelectFeature control;
	private StyleMap styleMap;
	private GwtOlHandler selectedhandler;
	private String paintableId;

	public VAbstractAutopopulatedVectorLayer() {
		super();
	}

	protected void updateGenericVectorLayersAttributes(UIDL uidl) {
		if (!uidl.hasAttribute("cached")) {
			this.paintableId = uidl.getId();
			display = uidl.getStringAttribute("display");
			setStyleMap(VVectorLayer.getStyleMap(uidl));
		}
	}

	protected void updateSelectionControl(final ApplicationConnection client) {
		if (client.hasEventListeners(this, "vsel")) {
			if (control == null) {
				if (selectedhandler == null) {

					// create select/unselect feature and communicate fid to
					// server
					selectedhandler = new GwtOlHandler() {
						public void onEvent(JsArray arguments) {
							ValueMap javaScriptObject = arguments.get(0).cast();
							Vector vector = javaScriptObject.getValueMap(
									"feature").cast();
							String fid = vector.getFeatureId();
							ValueMap attr = vector.getAttributes();
							client.updateVariable(paintableId, "fid", fid,
									false);
							Map<String, Object> hashMap = new HashMap<String, Object>();
							for (String key : attr.getKeySet()) {
								hashMap.put(key, attr.getString(key));
							}
							client.updateVariable(paintableId, "attr", hashMap,
									false);
							Projection targetProjection = getMap()
									.getProjection();
							String projection = getProjection();
							if (projection == null) {
								projection = "EPSG:4326";
							}
							Projection sourceProjection = Projection
									.get(projection);
							WKT wktFormatter = WKT.create(sourceProjection,
									targetProjection);
							String wkt = wktFormatter.write(vector);
							client.updateVariable(paintableId, "wkt", wkt,
									false);
							client.sendPendingVariableChanges();
						}
					};
					layer.registerHandler("featureselected", selectedhandler);
				}
				control = SelectFeature.create(layer);
				getMap().addControl(control);
			}
			control.activate();
		} else if (control != null) {
			control.deActivate();
			getMap().removeControl(control);
			control = null;
		}
	}

	public String getDisplay() {
		return display;
	}

	public Boolean isBaseLayer() {
		return isBaseLayer;
	}

	public Double getOpacity() {
		return opacity;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public StyleMap getStyleMap() {
		return styleMap;
	}

	public void setStyleMap(StyleMap styleMap) {
		this.styleMap = styleMap;
	}

}