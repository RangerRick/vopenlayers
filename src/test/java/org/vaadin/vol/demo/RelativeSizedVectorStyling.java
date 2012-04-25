package org.vaadin.vol.demo;

import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.Style;
import org.vaadin.vol.StyleMap;
import org.vaadin.vol.VectorLayer;

import com.vaadin.ui.Component;

@SuppressWarnings("serial")
public class RelativeSizedVectorStyling extends AbstractVOLTest {

    private VectorLayer vectorLayer;
    private OpenLayersMap map;

    @Override
    public String getDescription() {
        return "Styling vectors relative to real world.";
    }

    @Override
    Component getMap() {
        if (map == null) {

            map = new OpenLayersMap();

            OpenStreetMapLayer osm = new OpenStreetMapLayer();

            map.setSizeFull();

            // base layers
            map.addLayer(osm);

            vectorLayer = new VectorLayer();

            /*
             * PointVector on Vaadin HQ
             */
            PointVector pointVector = new PointVector(22.30083, 60.452541);
            vectorLayer.addComponent(pointVector);

            Style style = new Style();
            style.extendCoreStyle("default");
            /*
             * Define context functions (javascript) that calculate 100 "meters"
             * as "pixel value" for the current resolution. The size of the
             * circle should stay the same (relative to real world) although the
             * zoom level changes.
             * 
             * Note that although epsg 900913 unit is m, 1px is one exactly
             * meter only at equator. So the circle is more closer to having 100
             * meter diameter than radius here at north. Some more complex
             * calculations (that would take actual point location into account)
             * would be needed to get the real 100 meters radius. The same thing
             * could be done on server side as well, but then each feature would
             * need to update its style on each zoom level change.
             */
            style.setContextJs("{ get100MetersInPx: function(feature) {"
                    + "return 100000 / feature.layer.map.getResolution();"
                    + "}" + "}");
            
            // now use context functions value as radius
            style.setPointRadiusByAttribute("get100MetersInPx");

            vectorLayer.setStyleMap(new StyleMap(style));

            map.setCenter(22.30083, 60.452541);
            map.setZoom(15);

            map.addLayer(vectorLayer);
        }
        return map;
    }

}