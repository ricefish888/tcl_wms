
package com.vtradex.wms.webservice.client.sap.commcallback;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-07-04T12:04:30.885+08:00
 * Generated source version: 2.6.2
 * 
 */
public final class ZRFCWMSMSGRETURNPortType_HTTPSPort_Client {

    private static final QName SERVICE_NAME = new QName("urn:sap-com:document:sap:rfc:functions", "ZRFC_WMS_MSG_RETURNService");

    private ZRFCWMSMSGRETURNPortType_HTTPSPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = ZRFCWMSMSGRETURNService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ZRFCWMSMSGRETURNService ss = new ZRFCWMSMSGRETURNService(wsdlURL, SERVICE_NAME);
        ZRFCWMSMSGRETURNPortType port = ss.getHTTPSPort();  
        
        {
        System.out.println("Invoking zrfcWMSMSGRETURN...");
        com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURN _zrfcWMSMSGRETURN_parameters = null;
        com.vtradex.wms.webservice.client.sap.commcallback.ZRFCWMSMSGRETURNResponse _zrfcWMSMSGRETURN__return = port.zrfcWMSMSGRETURN(_zrfcWMSMSGRETURN_parameters);
        System.out.println("zrfcWMSMSGRETURN.result=" + _zrfcWMSMSGRETURN__return);


        }

        System.exit(0);
    }

}