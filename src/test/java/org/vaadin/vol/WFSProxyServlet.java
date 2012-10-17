package org.vaadin.vol;

import java.net.MalformedURLException;

import org.eclipse.jetty.http.HttpURI;
import org.eclipse.jetty.servlets.ProxyServlet;

public class WFSProxyServlet extends ProxyServlet {
    private final static String WFSPROXY_URI = "/WFSPROXY/";

    @Override
    protected HttpURI proxyHttpURI(String scheme, String serverName,
            int serverPort, String uri) throws MalformedURLException {
        if (uri.equals(WFSPROXY_URI))
            return new HttpURI("http://demo.opengeo.org/geoserver/wfs");
        else
            return new HttpURI("http://" + uri.substring(WFSPROXY_URI.length()));
    }
}
