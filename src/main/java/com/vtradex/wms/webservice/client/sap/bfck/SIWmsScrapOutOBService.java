package com.vtradex.wms.webservice.client.sap.bfck;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-08-02T16:57:53.858+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "SI_Wms_Scrap_Out_OBService", 
                  wsdlLocation = "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/bfck.wsdl",
                  targetNamespace = "http://wms.tcl.com/Scrap_Out") 
public class SIWmsScrapOutOBService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://wms.tcl.com/Scrap_Out", "SI_Wms_Scrap_Out_OBService");
    public final static QName HTTPSPort = new QName("http://wms.tcl.com/Scrap_Out", "HTTPS_Port");
    public final static QName HTTPPort = new QName("http://wms.tcl.com/Scrap_Out", "HTTP_Port");
    static {
        URL url = SIWmsScrapOutOBService.class.getResource("/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/bfck.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(SIWmsScrapOutOBService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/bfck.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SIWmsScrapOutOBService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIWmsScrapOutOBService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIWmsScrapOutOBService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns SIWmsScrapOutOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsScrapOutOB getHTTPSPort() {
        return super.getPort(HTTPSPort, SIWmsScrapOutOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsScrapOutOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsScrapOutOB getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, SIWmsScrapOutOB.class, features);
    }
    /**
     *
     * @return
     *     returns SIWmsScrapOutOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsScrapOutOB getHTTPPort() {
        return super.getPort(HTTPPort, SIWmsScrapOutOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsScrapOutOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsScrapOutOB getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, SIWmsScrapOutOB.class, features);
    }

}
