package org.vaadin.vol;

import java.util.HashMap;
import java.util.Map;

/**
 * A helper class to reproduce Javascript Object on server side
 * 
 */
public class JsObject {
    private Map<String, Object> keyvalue;

    public JsObject() {
        keyvalue = new HashMap<String, Object>();
    }

    public void setProperty(String key, Object value) {
        keyvalue.put(key, value);
    }

    public String getPropertyAsString(String key) {
        return (String) keyvalue.get(key);
    }

    public int getPropertyAsInt(String key) {
        return (Integer) keyvalue.get(key);
    }

    public double getPropertyAsDouble(String key) {
        return (Double) keyvalue.get(key);
    }

    public float getPropertyAsFloat(String key) {
        return (Float) keyvalue.get(key);
    }

    public boolean getPropertyAsBoolean(String key) {
        return (Boolean) keyvalue.get(key);
    }

    public Object getProperty(String key) {
        return keyvalue.get(key);
    }

    public Map<String, Object> getKeyValueMap() {
        return keyvalue;
    }
}
