package com.vtradex.wms.webservice.client.sap.resdata;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-08-03T10:14:08.424+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebServiceClient(name = "SI_Wms_Res_Issue_OBService", 
                  wsdlLocation = "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/resData.wsdl",
                  targetNamespace = "http://wms.tcl.com/Res_Issue") 
public class SIWmsResIssueOBService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://wms.tcl.com/Res_Issue", "SI_Wms_Res_Issue_OBService");
    public final static QName HTTPSPort = new QName("http://wms.tcl.com/Res_Issue", "HTTPS_Port");
    public final static QName HTTPPort = new QName("http://wms.tcl.com/Res_Issue", "HTTP_Port");
    static {
        URL url = SIWmsResIssueOBService.class.getResource("/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/resData.wsdl");
        if (url == null) {
            java.util.logging.Logger.getLogger(SIWmsResIssueOBService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "/Users/administrator/workspace/tcl_wms/src/main/resources/wsdl/resData.wsdl");
        }       
        WSDL_LOCATION = url;
    }

    public SIWmsResIssueOBService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SIWmsResIssueOBService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SIWmsResIssueOBService() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns SIWmsResIssueOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsResIssueOB getHTTPSPort() {
        return super.getPort(HTTPSPort, SIWmsResIssueOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsResIssueOB
     */
    @WebEndpoint(name = "HTTPS_Port")
    public SIWmsResIssueOB getHTTPSPort(WebServiceFeature... features) {
        return super.getPort(HTTPSPort, SIWmsResIssueOB.class, features);
    }
    /**
     *
     * @return
     *     returns SIWmsResIssueOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsResIssueOB getHTTPPort() {
        return super.getPort(HTTPPort, SIWmsResIssueOB.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SIWmsResIssueOB
     */
    @WebEndpoint(name = "HTTP_Port")
    public SIWmsResIssueOB getHTTPPort(WebServiceFeature... features) {
        return super.getPort(HTTPPort, SIWmsResIssueOB.class, features);
    }

}
