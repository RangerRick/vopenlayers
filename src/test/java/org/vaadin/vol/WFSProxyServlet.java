package org.vaadin.vol;

import java.net.MalformedURLException;

import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.servlets.ProxyServlet;

public class WFSProxyServlet extends ProxyServlet {

    @Override
    protected HttpURI proxyHttpURI(String scheme, String serverName,
            int serverPort, String uri) throws MalformedURLException {
        
        HttpURI httpURI = new HttpURI("http://demo.opengeo.org/geoserver/wfs");
        
        return httpURI;
    }
}
