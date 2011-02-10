package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.GoogleStreetLayer;

public class VGoogleStreetMapLayer extends VAbstracMapLayer<GoogleStreetLayer> {

	@Override
	GoogleStreetLayer createLayer() {
		return GoogleStreetLayer.create();
	}

}
