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
		double lon = childUIDL.getDoubleAttribute("x");
		double lat = childUIDL.getDoubleAttribute("y");

		Point p = Point.create((float) lon, (float) lat);
		p.transform(getProjection(), mapProjection);
		
		JavaScriptObject style = null;
		JavaScriptObject attributes = null;
		vector = Vector.create(p, attributes, style);

	}

}
