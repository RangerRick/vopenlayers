package org.vaadin.vol;

import java.util.HashMap;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public class StyleMap {

    private HashMap<String, Style> styles = new HashMap<String, Style>();

    /**
     * Creates a StyleMap setting the same style for all renderer intents.
     * <p>
     * From OpenLayers documentation: If just one style hash or style object is
     * passed, this will be used for all known render intents (default, select,
     * temporary)
     */
    public StyleMap(Style style) {
        setStyle("default", style);
    }

    /**
     * @param renderIntent
     * @param style
     */
    public void setStyle(String renderIntent, Style style) {
        styles.put(renderIntent, style);
    }

    private boolean extendDefault = false;

    /**
     * Creates a StyleMap setting with different styles for renderer intents.
     * <p>
     * Avoid passing null for one of the styles. If just want to use 2 style,
     * pass the same style for selectStyle and tempraryStyle parameters.
     * 
     * @param defaultStyle
     *            the default style to render the feature
     * @param selectStyle
     *            the style to render the feature when it is selected
     * @param temporaryStyle
     *            style to render the feature when it is temporarily selected
     */
    public StyleMap(Style defaultStyle, Style selectStyle, Style temporaryStyle) {
        setStyle("default", defaultStyle);
        if (selectStyle != null) {
            setStyle("select", selectStyle);
        }
        if (temporaryStyle != null) {
            setStyle("temporary", temporaryStyle);
        }
    }

    public StyleMap() {
    }

    public void paint(PaintTarget target) throws PaintException {
        String[] intents = styles.keySet().toArray(
                new String[styles.size() + (extendDefault ? 1 : 0)]);
        if (extendDefault) {
            intents[intents.length - 1] = "__extendDefault__";
        }
        target.addAttribute("olStyleMap", intents);
        for (String object : intents) {
            if (!object.startsWith("__")) {
                styles.get(object).paint("olStyle_" + object, target);
            }
        }
    }

    /**
     * @param extendDefault
     *            true if other styles should extend 'default' style from this
     *            style map
     */
    public void setExtendDefault(boolean extendDefault) {
        this.extendDefault = extendDefault;
    }

    public boolean isExtendDefault() {
        return extendDefault;
    }

}