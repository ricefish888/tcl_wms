
package com.vtradex.wms.webservice.client.sap.delivery;

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
 * 2017-07-14T14:30:31.946+08:00
 * Generated source version: 2.6.2
 * 
 */
public final class SIWMSINBOUNDDLVOB_HTTPSPort_Client {

    private static final QName SERVICE_NAME = new QName("http://wms.tcl.com/inbound_delivery", "SI_WMS_INBOUND_DLV_OBService");

    private SIWMSINBOUNDDLVOB_HTTPSPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = SIWMSINBOUNDDLVOBService.WSDL_LOCATION;
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
      
        SIWMSINBOUNDDLVOBService ss = new SIWMSINBOUNDDLVOBService(wsdlURL, SERVICE_NAME);
        SIWMSINBOUNDDLVOB port = ss.getHTTPSPort();  
        
        {
        System.out.println("Invoking siWMSINBOUNDDLVOB...");
        com.vtradex.wms.webservice.client.sap.delivery.DTINDLV _siWMSINBOUNDDLVOB_mmINDLVReq = null;
        com.vtradex.wms.webservice.client.sap.delivery.DTRetMsg _siWMSINBOUNDDLVOB__return = port.siWMSINBOUNDDLVOB(_siWMSINBOUNDDLVOB_mmINDLVReq);
        System.out.println("siWMSINBOUNDDLVOB.result=" + _siWMSINBOUNDDLVOB__return);


        }

        System.exit(0);
    }

}
