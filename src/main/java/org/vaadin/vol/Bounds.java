package org.vaadin.vol;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public class Bounds {

	private double top;
	private double bottom;
	private double left;
	private double right;

	public Bounds(Point... points) {
		init();

		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			extend(p);
		}
	}

	/**
	 * Init value are first point check and speed up bounds computing with huge
	 * arrays
	 */
	private void init() {
		bottom = +90.00;
		top = -90.00;
		right = -180.00;
		left = +180.00;
	}

	/**
	 * extend(Point... points) will be useful in case of multiple vector on the
	 * same map to compute the bounds that surround all the vectors
	 * 
	 * Notes : there is no check of the starting bounds values the method will
	 * fail if bounds values are not correctly initialized
	 */
	public void extend(Point... points) {
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

	/**
	 * Alias for {@link #setTop(double)}
	 * 
	 * @param lat
	 */
	public void setMaxLat(double lat) {
		setTop(lat);
	}

	/**
	 * Alias for {@link #getTop()}
	 * 
	 * @return
	 */
	public double getMaxLat() {
		return getTop();
	}

	public void setBottom(double bottom) {
		this.bottom = bottom;
	}

	public double getBottom() {
		return bottom;
	}

	/**
	 * Alias for {@link #setBottom(double)}
	 * 
	 * @param lat
	 */
	public void setMinLat(double lat) {
		setBottom(lat);
	}

	/**
	 * Alias for {@link #getBottom()}
	 * 
	 * @return
	 */
	public double getMinLat() {
		return getBottom();
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getLeft() {
		return left;
	}

	/**
	 * Alias for {@link #setLeft(double)}
	 * 
	 * @param lon
	 */
	public void setMinLon(double lon) {
		setLeft(lon);
	}

	/**
	 * Alias for {@link #getLeft()}
	 * 
	 * @return
	 */
	public double getMinLon() {
		return getLeft();
	}

	public void setRight(double right) {
		this.right = right;
	}

	public double getRight() {
		return right;
	}

	/**
	 * Alias for {@link #setRight(double)}
	 * 
	 * @param lon
	 */
	public void setMaxLon(double lon) {
		setRight(lon);
	}

	/**
	 * Alias for {@link #getRight()}
	 * 
	 * @return
	 */
	public double getMaxLon() {
		return getRight();
	}

	public void paint(String string, PaintTarget target) throws PaintException {
		target.addAttribute(string + "_top", top);
		target.addAttribute(string + "_right", right);
		target.addAttribute(string + "_bottom", bottom);
		target.addAttribute(string + "_left", left);
	}

}
