package org.vaadin.vol;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

public abstract class Vector extends AbstractComponent {

    private String projection = "EPSG:4326";

    private Point[] points;

    private Style style;

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

    /**
     * @return the custom style declaration assosicated with this Vector
     */
    public Style setStyle() {
        return style;
    }

    /**
     * @param style
     *            the custom style declaration to be used for rendering this
     *            Vector
     */
    public void setCustomStyle(Style style) {
        this.style = style;
        requestRepaint();
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("projection", getProjection());
        target.addAttribute("points", getPoints());
        if (style != null) {
            style.paint("olStyle", target);
        }
    }

    /**
     * Vectors styleName does not modify CSS style name as the method does for
     * standard Components. Instead the style name defines rendered intent that
     * will be used by OpenLayers to style the Vector. Rendered intents can be
     * configured with {@link StyleMap}s.
     * 
     * @see com.vaadin.ui.AbstractComponent#setStyleName(java.lang.String)
     */
    @Override
    public void setStyleName(String style) {
        super.setStyleName(style);
    }

}
