package org.vaadin.vol.client.ui;

import org.vaadin.vol.client.wrappers.Map;
import org.vaadin.vol.client.wrappers.Projection;
import org.vaadin.vol.client.wrappers.Vector;
import org.vaadin.vol.client.wrappers.layer.VectorLayer;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VAbstractVector extends Widget implements Paintable {

    protected Vector vector;
    private Projection projection;

    public VAbstractVector() {
        setElement(Document.get().createDivElement());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.terminal.gwt.client.Paintable#updateFromUIDL(com.vaadin.terminal
     * .gwt.client.UIDL, com.vaadin.terminal.gwt.client.ApplicationConnection)
     */
    public void updateFromUIDL(UIDL childUIDL,
            final ApplicationConnection client) {
        if (client.updateComponent(this, childUIDL, false)) {
            return;
        }
        projection = Projection.get(childUIDL.getStringAttribute("projection"));

        if (vector != null) {
            getLayer().removeFeature(vector);
        }
        updateVector(childUIDL, client);
        updateStyle(childUIDL, client);

        getLayer().addFeature(vector);
    }

    private void updateStyle(UIDL childUIDL, ApplicationConnection client) {
        if (childUIDL.hasAttribute("olStyle")) {
            vector.setStyle(childUIDL.getMapAttribute("olStyle"));
        }
    }

    protected Projection getProjection() {
        return projection;
    }

    protected abstract void updateVector(UIDL childUIDL,
            ApplicationConnection client);

    private VectorLayer getLayer() {
        return ((VVectorLayer) getParent()).getLayer();
    }

    protected Map getMap() {
        return ((VOpenLayersMap) getParent().getParent().getParent()).getMap();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
        getLayer().removeFeature(vector);
    }

    public Vector getVector() {
        return vector;
    }

}
