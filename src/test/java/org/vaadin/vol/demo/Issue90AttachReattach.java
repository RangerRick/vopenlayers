package org.vaadin.vol.demo;

import org.vaadin.vol.GoogleStreetMapLayer;
import org.vaadin.vol.OpenLayersMap;
import org.vaadin.vol.OpenStreetMapLayer;
import org.vaadin.vol.PointVector;
import org.vaadin.vol.VectorLayer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Issue90AttachReattach extends AbstractVOLTest {

    private boolean isInLayout1 = true;

    @Override
    public String getDescription() {
        return "Should work.";
    }

    @Override
    Component getMap() {
        VerticalLayout content = new VerticalLayout();
        final OpenLayersMap map = new OpenLayersMap();
        OpenStreetMapLayer osm = new OpenStreetMapLayer();
        GoogleStreetMapLayer googleStreets = new GoogleStreetMapLayer();
        map.addLayer(osm);
        map.addLayer(googleStreets);
        
        VectorLayer vectorLayer = new VectorLayer();
        vectorLayer.addVector(new PointVector(22.30083, 60.452541));
        map.addLayer(vectorLayer);
        
        map.setCenter(22.30083, 60.452541);

        final HorizontalLayout layout1 = new HorizontalLayout();
        layout1.setSizeFull();
        final HorizontalLayout layout2 = new HorizontalLayout();
        layout2.setSizeFull();
        layout1.addComponent(map);
        final Label label2 = new Label("Some other component");
        layout2.addComponent(label2);

        Button toggle = new Button("Toggle");
        toggle.addListener(new ClickListener() {
            private static final long serialVersionUID = 1L;

            public void buttonClick(final ClickEvent event) {
                if (isInLayout1) {
                    layout2.addComponent(map);
                    layout1.addComponent(label2);
                    isInLayout1 = false;
                } else {
                    layout1.addComponent(map);
                    layout2.addComponent(label2);
                    isInLayout1 = true;
                }
            }
        });

        content.setSizeFull();
        content.addComponent(toggle);
        content.addComponent(layout1);
        content.addComponent(layout2);
        content.setExpandRatio(layout1, 1);
        content.setExpandRatio(layout2, 1);
        return content;
    }

}
