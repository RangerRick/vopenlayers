package org.vaadin.vol.client.ui;

import java.util.HashMap;
import java.util.Map;

import org.vaadin.vol.client.wrappers.GwtOlHandler;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.control.SelectFeature;
import org.vaadin.vol.client.wrappers.layer.WebFeatureServiceLayer;

import com.google.gwt.core.client.JsArray;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ValueMap;

public class VWebFeatureServiceLayer extends
        VAbstracMapLayer<WebFeatureServiceLayer> {

    private String uri;
    private String layers;
    private Boolean isBaseLayer;
    private Double opacity;
    private String format;
    private boolean transparent;
    private String cqlFilter;
    private String projection;
    private String featureType;
    private String ns;
    private String display;
    private ApplicationConnection client;
    private WebFeatureServiceLayer layer;
    private SelectFeature control;
    private String paintableId;

    @Override
    WebFeatureServiceLayer createLayer() {
        if (layer == null) {
            layer = WebFeatureServiceLayer.create(display, uri, featureType,
                    ns, projection);
        }
        return layer;
    }

    @Override
    public void updateFromUIDL(UIDL uidl, final ApplicationConnection client) {
        this.client = client;
        this.paintableId = uidl.getId();
        if (!uidl.hasAttribute("cached")) {
            display = uidl.getStringAttribute("display");
            uri = uidl.getStringAttribute("uri");
            featureType = uidl.getStringAttribute("featureType");
            ns = uidl.getStringAttribute("featureNS");
            projection = uidl.hasAttribute("projection") ? uidl
                    .getStringAttribute("projection") : null;
        }
        super.updateFromUIDL(uidl, client);
        
        if(control == null) {
            
            // TODO create select/unselect feature and communicate fid to server
            layer.registerHandler("featureselected", new GwtOlHandler() {
                public void onEvent(JsArray arguments) {
                    ValueMap javaScriptObject = arguments.get(0).cast();
                    Vector vector = javaScriptObject.getValueMap("feature")
                            .cast();
                    String fid = vector.getFeatureId();
                    ValueMap attr = vector.getAttributes();
                    client.updateVariable(paintableId, "fid", fid, false);
                    Map<String, Object> hashMap = new HashMap<String, Object>();
                    for(String key :attr.getKeySet()) {
                        hashMap.put(key, attr.getString(key));
                    }
                    client.updateVariable(paintableId, "attr", hashMap, false);
                    client.sendPendingVariableChanges();
                }
            });
            control = SelectFeature.create(layer);
            getMap().addControl(control );
            control.activate();

            
        }


    }

    public String getUri() {
        return uri;
    }

    public String getLayers() {
        return layers;
    }

    public String getDisplay() {
        return display;
    }

    public Boolean isBaseLayer() {
        return isBaseLayer;
    }

    public Double getOpacity() {
        return opacity;
    }

    public String getFormat() {
        return format;
    }

    public boolean isTransparent() {
        return transparent;
    }
}
