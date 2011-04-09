package org.vaadin.vol;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

public abstract class Vector extends AbstractComponent {

	private String projection = "EPSG:4326";
	
	private Point[] points;

	public void setPoints(Point... points) {
		this.points = points;
		requestRepaint();
	}

	public Point[] getPoints() {
		return points;
	}


	public void setProjection(String projection) {
		this.projection = projection;
	}

	public String getProjection() {
		return projection;
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("projection", getProjection());
		target.addAttribute("points", getPoints());
	}

}
