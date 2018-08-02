package com.vtradex.wms.webservice.client.sap.inv;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-07-18T14:09:18.813+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "SI_Wms_Stock_OBService", 
                  wsdlLocation = "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/wmsInv2sap.wsdl",
                  targetNamespace = "http://wms.tcl.com/wms_stock") 
public class SIWmsStockOBService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://wms.tcl.com/wms_stock", "SI_Wms_Stock_OBService");
    public final static QName HTTPSPort = new QName("http://wms.tcl.com/wms_stock", "HTTPS_Port");
    public final static QName HTTPPort = new QName("http://wms.tcl.com/wms_stock", "HTTP_Port");
    static {
        URL url = SIWmsStockOBService.class.getResource("/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/wmsInv2sap.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(SIWmsStockOBService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/wmsInv2sap.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SIWmsStockOBService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIWmsStockOBService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIWmsStockOBService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns SIWmsStockOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsStockOB getHTTPSPort() {
        return super.getPort(HTTPSPort, SIWmsStockOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsStockOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsStockOB getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, SIWmsStockOB.class, features);
    }
    /**
     *
     * @return
     *     returns SIWmsStockOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsStockOB getHTTPPort() {
        return super.getPort(HTTPPort, SIWmsStockOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsStockOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsStockOB getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, SIWmsStockOB.class, features);
    }

}