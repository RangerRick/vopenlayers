package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.geometry.Point;

import com.google.gwt.core.client.JavaScriptObject;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPointVector extends VAbstractVector {

    @Override
    protected void updateVector(UIDL childUIDL, ApplicationConnection client) {
        Projection mapProjection = getMap().getProjection();

        Point p = Point.create(childUIDL.getStringArrayAttribute("points")[0]);
        p.transform(getProjection(), mapProjection);

        JavaScriptObject style = null;
        JavaScriptObject attributes = getAttributes();
        vector = Vector.create(p, attributes, style);

    }

}
