package org.vaadin.vol;

import org.vaadin.vol.client.ui.VArea;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VArea.class)
public class Area extends Vector {
	
	private String projection = "EPSG:4326";
	
	private Point[] points;

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public String getProjection() {
		return projection;
	}

	public void setPoints(Point[] points) {
		this.points = points;
	}

	public Point[] getPoints() {
		return points;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("projection", projection);
		target.addAttribute("points", points);
		
	}
	
}
