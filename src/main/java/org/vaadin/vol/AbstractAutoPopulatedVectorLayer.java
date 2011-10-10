package org.vaadin.vol;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

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

    public AbstractAutoPopulatedVectorLayer() {
        super();
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

}