/**
 * 
 */
package org.vaadin.vol;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Component;

@ClientWidget(org.vaadin.vol.client.ui.VVectorLayer.class)
public class VectorLayer extends AbstractComponentContainer implements Layer {
	
	private String displayName = "Vector layer";

	private List<Vector> vectors = new LinkedList<Vector>();
	
	public enum DrawingMode {
		NONE, 
		/** NOT YET IMPLEMENTED */
		LINE, 
		AREA, 
		/** NOT YET IMPLEMENTED */
		POINT
	}
	
	private DrawingMode drawindMode = DrawingMode.NONE;

	public void addVector(Vector m) {
		addComponent(m);
	}

	public void paintContent(PaintTarget target) throws PaintException {
		target.addAttribute("name", displayName);
		target.addAttribute("dmode", drawindMode.toString());
		for (Vector m : vectors) {
			m.paint(target);
		}
	}
	
	public void replaceComponent(Component oldComponent, Component newComponent) {
		throw new UnsupportedOperationException();
	}

	public Iterator<Component> getComponentIterator() {
		LinkedList<Component> list = new LinkedList<Component>(vectors);
		return list.iterator();
	}
	
	@Override
	public void addComponent(Component c) {
		if(c instanceof Vector) {
			vectors.add((Vector) c);
			super.addComponent(c);
		} else {
			throw new IllegalArgumentException("VectorLayer supports only Vectors");
		}
	}
	
	@Override
	public void removeComponent(Component c) {
		vectors.remove(c);
		super.removeComponent(c);
		requestRepaint();
	}

	public void setDrawindMode(DrawingMode drawindMode) {
		this.drawindMode = drawindMode;
		requestRepaint();
	}

	public DrawingMode getDrawindMode() {
		return drawindMode;
	}
	
	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);
		// TODO support other drawing modes than area
		// TODO make events fired when new object is drawn/edited
		if(variables.containsKey("vertices")) {
			String[] object = (String[]) variables.get("vertices");
			Point[] points = new Point[object.length];
			for (int i = 0; i < points.length; i++) {
				points[i] = Point.valueOf(object[i]);
			}
			Area area = new Area();
			area.setPoints(points);
			newAreaPainted(area);
		}
	}

	protected void newAreaPainted(Area area) {
		VectorDrawnEvent vectorDrawnEvent = new VectorDrawnEvent(this, area);
		fireEvent(vectorDrawnEvent);
		requestRepaint();
	}
	
	public interface VectorDrawnListener {
		
		public final Method method = ReflectTools.findMethod(VectorDrawnListener.class, "vectorDrawn", VectorDrawnEvent.class); 
		
		public void vectorDrawn(VectorDrawnEvent event);
		
	}
	
	public void addListener(VectorDrawnListener listener) {
		addListener(VectorDrawnEvent.class, listener, VectorDrawnListener.method);
	}
	
	public void removeListener(VectorDrawnListener listener) {
		removeListener(VectorDrawnEvent.class, listener, VectorDrawnListener.method);
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public class VectorDrawnEvent extends Event {

		private Vector vector;

		public VectorDrawnEvent(Component source, Vector vector) {
			super(source);
			this.setVector(vector);
		}

		private void setVector(Vector vector) {
			this.vector = vector;
		}

		public Vector getVector() {
			return vector;
		}
		
	}
}