package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.OpenStreetMapLayer;

public class VOpenStreetMapLayer extends VAbstracMapLayer<OpenStreetMapLayer> {

	@Override
	OpenStreetMapLayer createLayer() {
		return OpenStreetMapLayer.create();
	}

}
