/**
 * 
 */
package org.vaadin.vol;

import org.vaadin.vol.client.ui.VOpenStreetMapLayer;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

/**
 * OpenStreeMap layer that can be added to {@link OpenLayersMap}.
 * <p>
 * <strong>Note</strong> that to use this layer, developer must ensure the host
 * page has open street map javascripts inlcuded. This can be achived for example by adding a following script tag to widgetset (...gwt.xml).
 * 
 * <p>
 * <code>
 *  &lt;script src="http://www.openstreetmap.org/openlayers/OpenStreetMap.js"&gt;&lt;/script&gt;
 * </code>
 */
@ClientWidget(VOpenStreetMapLayer.class)
public class OpenStreetMapLayer extends AbstractComponent implements Layer {
}