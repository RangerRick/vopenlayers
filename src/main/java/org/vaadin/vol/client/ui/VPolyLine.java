package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.geometry.LineString;
import org.vaadin.vol.client.wrappers.geometry.Point;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPolyLine extends VAbstractVector {

	@Override
	protected void updateVector(UIDL childUIDL, ApplicationConnection client) {
		Projection mapProjection = getMap().getProjection();
		String[] stringArrayAttribute = childUIDL
				.getStringArrayAttribute("points");
		JsArray<Point> points = (JsArray<Point>) JsArray.createArray();
		for (int i = 0; i < stringArrayAttribute.length; i++) {
			String[] split = stringArrayAttribute[i].split(":");
			float lon = Float.parseFloat(split[0]);
			float lat = Float.parseFloat(split[1]);
			Point p = Point.create(lon, lat);
			p.transform(getProjection(), mapProjection);
			points.push(p);
		}
		
		LineString lr = LineString.create(points);

		JavaScriptObject style = null;
		JavaScriptObject attributes = null;
		vector = Vector.create(lr, attributes, style);

	}

}
