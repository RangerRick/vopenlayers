package org.vaadin.vol.filter;

import org.vaadin.vol.JsObject;

public class Filter {

    protected JsObject filter;

    public Filter() {
        filter = new JsObject();
    }

    /**
     * @return the type
     */
    public String getType() {
        return filter.getPropertyAsString("type");
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        filter.setProperty("type", type);
    }

    public JsObject getFilter() {
        return filter;
    }
}
