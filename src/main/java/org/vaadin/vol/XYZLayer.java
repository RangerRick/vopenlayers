/**
 * 
 */
package org.vaadin.vol;

import org.vaadin.vol.client.ui.VXYZLayer;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

/**
 * see great tool: www.maptiler.org
 */
@ClientWidget(VXYZLayer.class)
public class XYZLayer extends AbstractComponent implements Layer {
    private String uri = "";
    private String name = "";
    private boolean sphericalMercator = false;

    public XYZLayer() {
    }

    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute("uri", uri);
        target.addAttribute("name", name);
        if(isSphericalMercator()) {
            target.addAttribute("sphericalMercator", true);
        }
    }

    public void setUri(String uri) {
        this.uri = uri;
        requestRepaint();
    }

    public String getDisplayName() {
        return name;
    }

    public void setDisplayName(String displayName) {
        this.name = displayName;
        setCaption(displayName);
        requestRepaint();
    }

    public String getUri() {
        return uri;
    }

    public boolean isSphericalMercator() {
        return sphericalMercator;
    }

    public void setSphericalMercator(boolean sphericalMercator) {
        this.sphericalMercator = sphericalMercator;
    }
}