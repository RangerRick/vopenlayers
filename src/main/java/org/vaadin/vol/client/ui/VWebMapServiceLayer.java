package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.layer.WebMapServiceLayer;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWebMapServiceLayer extends VAbstracMapLayer<WebMapServiceLayer> {

	private String uri;
	private String layers;
	private String display;
	private Boolean isBaseLayer;
	private Double opacity;
	private String format;
	private boolean transparent;

	@Override
	WebMapServiceLayer createLayer() {
		return WebMapServiceLayer.create(display, uri, layers, format, isBaseLayer, transparent);
	}
	

	
//	target.addAttribute("uri", uri);
//	target.addAttribute("type", type);
//	target.addAttribute("layers", layers);
//	target.addAttribute("display", display_name);
//	target.addAttribute("isBaseLayer", isBaseLayer);
//	target.addAttribute("opacity", opacity);
//	target.addAttribute("featureid", feature_id);

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if(!uidl.hasAttribute("cached")) {
			uri = uidl.getStringAttribute("uri");
			layers = uidl.getStringAttribute("layers");
			display = uidl.getStringAttribute("display");
			isBaseLayer = uidl.getBooleanAttribute("isBaseLayer");
			transparent = uidl.getBooleanAttribute("transparent");
			opacity = uidl.getDoubleAttribute("opacity");
			format = uidl.getStringAttribute("format");
		}
		super.updateFromUIDL(uidl, client);
	}

}
