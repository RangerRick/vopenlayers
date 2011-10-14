package org.vaadin.vol;

import java.lang.reflect.Method;
import java.util.Map;

import org.vaadin.vol.VectorLayer.SelectionMode;
import org.vaadin.vol.VectorLayer.VectorSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorSelectedListener;
import org.vaadin.vol.VectorLayer.VectorUnSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorUnSelectedListener;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;

/**
 * An abstract implementation (based on client side vector layer) that populates
 * it content on the client side only. Server side only controls how it is
 * populated, styled and provides methods for selection features.
 * 
 */
public abstract class AbstractAutoPopulatedVectorLayer extends
        AbstractComponent {

    private StyleMap stylemap;
    private String projection;
    private String displayName = "WFS";
	private SelectionMode selectionMode;

    public AbstractAutoPopulatedVectorLayer() {
        super();
    }
    
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        String fid = (String) variables.get("fid");
        Map<String,Object> attr = (Map<String, Object>) variables.get("attr");
        String wkt = (String) variables.get("wkt");
        featureSelected(fid, attr, wkt);
        
        // TODO create unselected event
    }

    private void featureSelected(String fid, Map<String, Object> attr, String wkt) {
    	FeatureSelectedEvent featureSelectedEvent = new FeatureSelectedEvent(this, fid, attr, wkt);
    	fireEvent(featureSelectedEvent);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("display", displayName);
        if (projection != null) {
            target.addAttribute("projection", projection);
        }
        if (stylemap != null) {
            stylemap.paint(target);
        }
    }

    /**
     * @return the stylemap
     */
    public StyleMap getStyleMap() {
        return stylemap;
    }

    /**
     * @param stylemap
     *            the stylemap to set
     */
    public void setStyleMap(StyleMap stylemap) {
        this.stylemap = stylemap;
        requestRepaint();
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String display_name) {
        this.displayName = display_name;
    }
    
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
        requestRepaint();
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }
    
    public interface FeatureSelectedListener {

        public final String EVENT_ID = "vsel";

        public final Method method = ReflectTools.findMethod(
                FeatureSelectedListener.class, "featureSelected",
                FeatureSelectedEvent.class);

        public void featureSelected(FeatureSelectedEvent event);

    }

    public void addListener(FeatureSelectedListener listener) {
        addListener(FeatureSelectedListener.EVENT_ID, FeatureSelectedEvent.class,
                listener, FeatureSelectedListener.method);
    }

    public void removeListener(FeatureSelectedListener listener) {
        removeListener(FeatureSelectedListener.EVENT_ID,
                FeatureSelectedEvent.class, listener);
    }

    public class FeatureSelectedEvent extends Event {

        private String featureId;
		private Map<String, Object> attributes;
		private String wkt;

		public FeatureSelectedEvent(Component source, String featureId, Map<String, Object> attr, String wkt2) {
            super(source);
            this.setFeatureId(featureId);
            this.setAttributes(attr);
            this.setWkt(wkt2);
        }

		private void setWkt(String wkt2) {
			this.wkt = wkt2;
		}

		public void setAttributes(Map<String, Object> attr) {
			this.attributes = attr;
		}
		
		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public String getFeatureId() {
			return featureId;
		}

		public void setFeatureId(String featureId) {
			this.featureId = featureId;
		}

		public String getWkt() {
			return wkt;
		}

    }

    public interface FeatureUnSelectedListener {

        public final String EVENT_ID = "vusel";

        public final Method method = ReflectTools.findMethod(
                FeatureUnSelectedListener.class, "featureUnSelected",
                FeatureUnSelectedEvent.class);

        public void featureUnSelected(FeatureUnSelectedEvent event);

    }

    public void addListener(FeatureUnSelectedListener listener) {
        addListener(FeatureUnSelectedListener.EVENT_ID,
                FeatureUnSelectedEvent.class, listener,
                FeatureUnSelectedListener.method);
    }

    public void removeListener(FeatureUnSelectedListener listener) {
        removeListener(FeatureUnSelectedListener.EVENT_ID,
                FeatureUnSelectedEvent.class, listener);
    }

    public class FeatureUnSelectedEvent extends FeatureSelectedEvent {

		public FeatureUnSelectedEvent(Component source, String featureId,Map<String, Object> attr, String wkt) {
			super(source, featureId, attr, wkt);
		}

    }


}