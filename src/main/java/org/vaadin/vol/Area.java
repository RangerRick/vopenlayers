package org.vaadin.vol;

import org.vaadin.vol.client.ui.VArea;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VArea.class)
public class Area extends Vector {
	
	private Point[] points;

	public void setPoints(Point[] points) {
		this.points = points;
		requestRepaint();
	}

	public Point[] getPoints() {
		return points;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("points", points);
		
	}
	
}
