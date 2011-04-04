package org.vaadin.vol;

import org.vaadin.vol.client.ui.VPointVector;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VPointVector.class)
public class PointVector extends Vector {
	
	private Point point;

	public PointVector(double x, double y) {
		point = new Point(x, y);
	}
	
	public PointVector() {
		point = new Point(0, 0);
	}
	
	public Point getPoint() {
		return point;
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("x", point.getLon());
		target.addAttribute("y", point.getLat());
	}

}
