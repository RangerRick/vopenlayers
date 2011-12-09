package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.layer.OpenStreetMapLayer;

public class VOpenStreetMapOsmarenderLayer extends
        VAbstracMapLayer<OpenStreetMapLayer> {

    @Override
    OpenStreetMapLayer createLayer() {
        return OpenStreetMapLayer.createOsmarenderMap(getDisplayName(),
                getProjection());
    }

}
