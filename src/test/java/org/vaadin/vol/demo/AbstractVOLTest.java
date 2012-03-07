package org.vaadin.vol.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

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

    @SuppressWarnings("unchecked")
    static Class<? extends AbstractVOLTest>[] a = new Class[] {
            Demo.class, ActionHandlers.class, BingMapTypes.class,
            MarkerAddingAndRemoving.class, OpenStreetMapTypes.class,
            WebFeatureServiceLayerTest.class };
    static Collection<Class<? extends AbstractVOLTest>> suitableOnlineDemos = Collections
            .synchronizedSet(new HashSet<Class<? extends AbstractVOLTest>>(
                    Arrays.asList(a)));

    public boolean isSuitebleOnlineDemo() {
        return suitableOnlineDemos.contains(getClass());
    }

}
