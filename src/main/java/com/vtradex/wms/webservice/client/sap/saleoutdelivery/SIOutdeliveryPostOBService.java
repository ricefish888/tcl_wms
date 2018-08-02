package com.vtradex.wms.webservice.client.sap.saleoutdelivery;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-08-02T16:57:58.684+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "SI_Outdelivery_Post_OBService", 
                  wsdlLocation = "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/saleOutDelivery.wsdl",
                  targetNamespace = "http://wms.tcl.com/Outbound_Post") 
public class SIOutdeliveryPostOBService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://wms.tcl.com/Outbound_Post", "SI_Outdelivery_Post_OBService");
    public final static QName HTTPSPort = new QName("http://wms.tcl.com/Outbound_Post", "HTTPS_Port");
    public final static QName HTTPPort = new QName("http://wms.tcl.com/Outbound_Post", "HTTP_Port");
    static {
        URL url = SIOutdeliveryPostOBService.class.getResource("/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/saleOutDelivery.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(SIOutdeliveryPostOBService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/saleOutDelivery.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SIOutdeliveryPostOBService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIOutdeliveryPostOBService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIOutdeliveryPostOBService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns SIOutdeliveryPostOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIOutdeliveryPostOB getHTTPSPort() {
        return super.getPort(HTTPSPort, SIOutdeliveryPostOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIOutdeliveryPostOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIOutdeliveryPostOB getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, SIOutdeliveryPostOB.class, features);
    }
    /**
     *
     * @return
     *     returns SIOutdeliveryPostOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIOutdeliveryPostOB getHTTPPort() {
        return super.getPort(HTTPPort, SIOutdeliveryPostOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIOutdeliveryPostOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIOutdeliveryPostOB getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, SIOutdeliveryPostOB.class, features);
    }

}