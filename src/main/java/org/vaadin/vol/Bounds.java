package org.vaadin.vol;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public class Bounds {

	private double top;
	private double bottom;
	private double left;
	private double right;

	public Bounds(Point... points) {
		// pre init to avoid 
		// first point check
		// and speed up bounds computing with huge arrays
		//
		bottom = +90.00; 
		top = -90.00;
		right = -180.00;
		left = +180.00;
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
				extend(p);
		}
	}

	public void extend(Point p) {
		double lon = p.getLon();
		if (lon < left) {
			left = lon;
		} else if (lon > right) {
			right = lon;
		}
		double lat = p.getLat();
		if (lat < bottom) {
			bottom = lat;
		} else if (lat > top) {
			top = lat;
		}
		// TODO figure out how to behave on poles and in date line
	}

	public void setTop(double top) {
		this.top = top;
	}

	public double getTop() {
		return top;
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public double getBottom() {
		return bottom;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getLeft() {
		return left;
	}

	public void setRight(double right) {
		this.right = right;
	}

	public double getRight() {
		return right;
	}

	public void paint(String string, PaintTarget target) throws PaintException {
		target.addAttribute(string + "_top", top);
		target.addAttribute(string + "_right", right);
		target.addAttribute(string + "_bottom", bottom);
		target.addAttribute(string + "_left", left);
	}

}
