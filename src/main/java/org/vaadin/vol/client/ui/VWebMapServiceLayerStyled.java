package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.layer.WebMapServiceLayerStyled;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWebMapServiceLayerStyled extends VWebMapServiceLayer
{

    private String sld;

    @Override
    WebMapServiceLayerStyled createLayer() {
	return WebMapServiceLayerStyled.create(super.getDisplay(), super.getUri(), super.getLayers(), super.getFormat(), 
		super.isBaseLayer(), super.isTransparent(), sld);
    }
    
    @Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if(!uidl.hasAttribute("cached")) {
			sld = uidl.getStringAttribute("sld");
		}
		super.updateFromUIDL(uidl, client);
	}


}
