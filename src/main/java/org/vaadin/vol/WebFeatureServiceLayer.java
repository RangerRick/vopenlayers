/**
 * 
 */
package org.vaadin.vol;

import java.util.Map;

import org.vaadin.vol.VectorLayer.VectorSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorSelectedListener;
import org.vaadin.vol.VectorLayer.VectorUnSelectedEvent;
import org.vaadin.vol.VectorLayer.VectorUnSelectedListener;
import org.vaadin.vol.client.ui.VWebFeatureServiceLayer;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VWebFeatureServiceLayer.class)
public class WebFeatureServiceLayer extends AbstractComponent implements Layer {
    private String uri = "";
    private String featureType = "basic";
    private String projection;
    private String featureNS;

    private String displayName = "WFS";

    public WebFeatureServiceLayer() {

    }

    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute("display", displayName);
        target.addAttribute("uri", uri);
        target.addAttribute("featureType", featureType);
        target.addAttribute("featureNS", featureNS);
        if (projection != null) {
            target.addAttribute("projection", projection);
        }
    }
    
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        String fid = (String) variables.get("fid");
        Map<String,Object> attr = (Map<String, Object>) variables.get("attr");
        featureSelected(fid, attr);
    }

    protected void featureSelected(String fid, Map<String, Object> attr) {
        // TODO FeatureSelectedEvent
    }

    public void setUri(String uri) {
        this.uri = uri;
        requestRepaint();
    }

    public String getUri() {
        return uri;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
        requestRepaint();
    }

    public String getFeatureType() {
        return featureType;
    }

    public String getProjection() {
        return projection;
    }

    public void setProjection(String projection) {
        this.projection = projection;
    }

    public void setFeatureNS(String ns) {
        this.featureNS = ns;
    }

    public String getFeatureNS() {
        return featureNS;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String display_name) {
        this.displayName = display_name;
    }

}