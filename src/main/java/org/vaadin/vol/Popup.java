/**
 * 
 */
package org.vaadin.vol;

import java.lang.reflect.Method;
import java.util.Map;

import org.vaadin.vol.client.ui.VPopup;

import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

@SuppressWarnings("serial")
@ClientWidget(VPopup.class)
public class Popup extends AbstractComponent {
	
	public class CloseEvent extends Event {
		public CloseEvent() {
			super(Popup.this);
		}
	}
	
	public interface CloseListener {
		public void onClose(CloseEvent event);
		final Method method = ReflectTools.findMethod(CloseListener.class, "onClose", CloseEvent.class);
		final String id = "close";
	}

	public enum PopupStyle {
		DEFAULT, ANCHORED, ANCHORED_BUBBLE, FRAMED, FRAMED_CLOUD
	}

	private double lon;
	private double lat;
	private String html_content = "";
	private String projection = "EPSG:4326";
	private PopupStyle popupstyle = PopupStyle.DEFAULT;
	private Marker anchor;

	public Popup(double lon, double lat, String content) {
		this.lon = lon;
		this.lat = lat;
		html_content = content;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
		requestRepaint();
	}

	public void setLat(double lat) {
		this.lat = lat;
		requestRepaint();
	}

	public void setContent(String content) {
		this.html_content = content;
		requestRepaint();
	}

	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		target.addAttribute("lon", lon);
		target.addAttribute("lat", lat);
		target.addAttribute("content", html_content);
		target.addAttribute("pr", projection);
		target.addAttribute("style", popupstyle.toString());
		if (popupstyle != PopupStyle.DEFAULT) {
			if (anchor == null) {
				throw new IllegalStateException(
						"All but default style popups require an anchor element to be define.");
			}
			target.addAttribute("anchor", anchor);
		}
	}

	public void addClickListener(ClickListener listener) {
		addListener("click", ClickEvent.class, listener,
				ClickListener.clickMethod);
	}

	public void removeClickListener(ClickListener listener) {
		removeListener(ClickEvent.class, listener);
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		super.changeVariables(source, variables);
		if (variables.containsKey("close")) {
			fireEvent(new CloseEvent());
		}
	}

	public void setPopupStyle(PopupStyle style) {
		this.popupstyle = style;
		requestRepaint();
	}

	public PopupStyle getPopupStyle() {
		return popupstyle;
	}

	public void setAnchor(Marker marker) {
		this.anchor = marker;
		requestRepaint();
	}
	
	public void addListener(CloseListener listener) {
		super.addListener(CloseListener.id, CloseEvent.class, listener, CloseListener.method);
	}
	
	public void removeListener(CloseListener listener) {
		super.removeListener(CloseListener.id, CloseEvent.class, listener);
	}

}