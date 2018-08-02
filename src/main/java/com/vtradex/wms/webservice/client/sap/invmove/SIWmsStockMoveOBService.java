package com.vtradex.wms.webservice.client.sap.invmove;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-08-09T15:54:32.740+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "SI_Wms_Stock_Move_OBService", 
                  wsdlLocation = "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/invMove.wsdl",
                  targetNamespace = "https://wms.tcl.com/stock_move") 
public class SIWmsStockMoveOBService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("https://wms.tcl.com/stock_move", "SI_Wms_Stock_Move_OBService");
    public final static QName HTTPSPort = new QName("https://wms.tcl.com/stock_move", "HTTPS_Port");
    public final static QName HTTPPort = new QName("https://wms.tcl.com/stock_move", "HTTP_Port");
    static {
        URL url = SIWmsStockMoveOBService.class.getResource("/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/invMove.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(SIWmsStockMoveOBService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/invMove.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SIWmsStockMoveOBService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIWmsStockMoveOBService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIWmsStockMoveOBService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns SIWmsStockMoveOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsStockMoveOB getHTTPSPort() {
        return super.getPort(HTTPSPort, SIWmsStockMoveOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsStockMoveOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsStockMoveOB getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, SIWmsStockMoveOB.class, features);
    }
    /**
     *
     * @return
     *     returns SIWmsStockMoveOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsStockMoveOB getHTTPPort() {
        return super.getPort(HTTPPort, SIWmsStockMoveOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsStockMoveOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsStockMoveOB getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, SIWmsStockMoveOB.class, features);
    }

}
