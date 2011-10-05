package org.vaadin.vol.demo;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class AbstractVOLTest extends Window {
    
    private VerticalLayout content;

    public AbstractVOLTest() {
        content = new VerticalLayout();
        setContent(content);
    }
    
    @Override
    public void attach() {
        super.attach();
        setup();
    }

    protected void setup() {
        Component map = getMap();
        content.setSizeFull();
        content.addComponent(map);
        content.setExpandRatio(map, 1);
    }

    abstract Component getMap();

}
