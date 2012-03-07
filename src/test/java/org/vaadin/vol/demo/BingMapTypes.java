package org.vaadin.vol.demo;

import org.vaadin.vol.BingMapLayer;
import org.vaadin.vol.OpenLayersMap;

import com.vaadin.ui.Component;

public class BingMapTypes extends AbstractVOLTest {

    @Override
    public String getDescription() {
        return "Demonstrates various Bing map types. Note that bing maps need API key from bingmapportal.com !";
    }

    @Override
    Component getMap() {
        OpenLayersMap map = new OpenLayersMap();

        map.setCenter(22.30, 60.452);

        // NOTE, DO NOT USE THIS API KEY IN YOUR OWN APP!! This API key is only
        // for demos (at matti.virtuallypreinstalled.com) and testing of the
        // addon. Register your own in
        // https://www.bingmapsportal.com

        for (BingMapLayer.Type t : BingMapLayer.Type.values()) {
            BingMapLayer bingMapLayer = new BingMapLayer(
                    "AhzA29X4AedR8DapHQylLbWF1I-K_n7rpKjIsnagYt1ssp4Nz1Rx7Nf0UN_KWfuH");
            bingMapLayer.setType(t);
            bingMapLayer.setDisplayName(t.toString());
            map.addLayer(bingMapLayer);
        }

        map.setSizeFull();

        return map;
    }

    @Override
    public boolean isSuitebleOnlineDemo() {
        return true;
    }

}
