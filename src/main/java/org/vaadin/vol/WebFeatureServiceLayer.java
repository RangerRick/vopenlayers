/**
 * 
 */
package org.vaadin.vol;

import java.util.Map;

import org.vaadin.vol.client.ui.VWebFeatureServiceLayer;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VWebFeatureServiceLayer.class)
public class WebFeatureServiceLayer extends AbstractAutoPopulatedVectorLayer implements Layer {
    private String uri = "";
    private String featureType = "basic";
    private String featureNS;


    public WebFeatureServiceLayer() {

    }
    
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("uri", uri);
        target.addAttribute("featureType", featureType);
        target.addAttribute("featureNS", featureNS);
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

    public void setFeatureNS(String ns) {
        this.featureNS = ns;
    }

    public String getFeatureNS() {
        return featureNS;
    }

}