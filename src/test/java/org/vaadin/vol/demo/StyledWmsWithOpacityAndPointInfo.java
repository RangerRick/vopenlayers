package org.vaadin.vol.demo;

import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenLayersMap.MapClickEvent;
import org.vaadin.vol.OpenLayersMap.MapClickListener;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.PointInformation;
import org.vaadin.vol.WebMapServiceLayerStyled;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;

public class StyledWmsWithOpacityAndPointInfo extends AbstractVOLTest {
    
	WebMapServiceLayerStyled wms;
	
    @Override
    public String getDescription() {
        return "Demonstrates using SLD to style WMS layer and how to change opacity of an overlay. Bonus : Shows point info for the clicked point on map. Functional, any known public WMS server that could be used for this demo.";
    }


    @Override
    Component getMap() {
        final OpenLayersMap map = new OpenLayersMap();

        map.setJsMapOptions("{projection: "
                + "new OpenLayers.Projection(\"EPSG:900913\"),"
                + "units: \"m\","
                + "numZoomLevels: 22,"
                + "maxResolution: 156543.0339, "
                + "maxExtent: new OpenLayers.Bounds(-20037508, -20037508,20037508, 20037508.34)}");

        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        googleStreets.setProjection("EPSG:900913");
        map.addLayer(googleStreets);

        OpenStreetMapLayer osm = new OpenStreetMapLayer();
        osm.setProjection("EPSG:900913");
        map.addLayer(osm);

        wms = new WebMapServiceLayerStyled();
        //wms.setUri("http://giswebservices.massgis.state.ma.us/geoserver/wms");
        wms.setUri("http://dsmce.itc.nl:8090/geoserver/wms");
        // wms.setLayers("topp:states");
        wms.setLayers("states");
        wms.setTransparent(true);
        wms.setFormat("image/gif");
        wms.setBaseLayer(false);
        wms.setDisplayName("states");
        // Be careful about namespaces in SLD like topp:states or massgis:states
        // otherwise it will render the map using defaultstyle. Basically a
        // mistake in the namespace will prevent overriding the style with the
        // one provided.
        // String sld =
        // "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>topp:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke/></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        //String sld = "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>massgis:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke><CssParameter name=\"stroke\">#FF0000</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        
        //Line Symbolizer
        //String sld = "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>topp:states</Name><UserStyle><FeatureTypeStyle><Rule><LineSymbolizer><Stroke><CssParameter name=\"stroke\">#FF0000</CssParameter></Stroke></LineSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        
        //Polygon Symbolizer
        String sld = "<StyledLayerDescriptor version=\"1.0.0\"><NamedLayer><Name>topp:states</Name><UserStyle><FeatureTypeStyle><Rule><PolygonSymbolizer>" +
        "<Fill><CssParameter name=\"fill\">#000080</CssParameter></Fill>" +
        "<Stroke><CssParameter name=\"stroke\">#FFFFFF</CssParameter><CssParameter name=\"stroke-width\">2</CssParameter></Stroke>" +
        "</PolygonSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";
        wms.setSld(sld);
        map.addLayer(wms);

        map.setSizeFull();
        
        final Slider opacitySlider = new Slider(0.1, 1D, 1);
        try {
			opacitySlider.setValue(1D);
		} catch (ValueOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        opacitySlider.setCaption("Change Opacity of Styled WMS Overlay");
        opacitySlider.addListener(new ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) {
				
					Double newValue = (Double) opacitySlider.getValue();
					wms.setOpacity(newValue);
			}
		});
        opacitySlider.setSizeUndefined();
        content.addComponent(opacitySlider);
        
        map.addListener(new MapClickListener() {
			
			public void mapClicked(MapClickEvent event) {
				PointInformation info = event.getPointInfo();
				 showNotification("Point Information", info.toString(),
			                Notification.TYPE_WARNING_MESSAGE, true);
			}
		});
        map.setImmediate(true);
        opacitySlider.setImmediate(true);
        return map;
    }


}
