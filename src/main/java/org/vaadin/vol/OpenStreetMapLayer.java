/**
 * 
 */
package org.vaadin.vol;

import org.vaadin.vol.client.ui.VOpenStreetMapLayer;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VOpenStreetMapLayer.class)
public class OpenStreetMapLayer extends AbstractComponent implements Layer {
}