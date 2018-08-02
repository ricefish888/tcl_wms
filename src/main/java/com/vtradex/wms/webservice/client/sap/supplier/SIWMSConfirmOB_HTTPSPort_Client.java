
package com.vtradex.wms.webservice.client.sap.supplier;

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
 * 2017-07-18T14:09:26.677+08:00
 * Generated source version: 2.6.2
 * 
 */
public final class SIWMSConfirmOB_HTTPSPort_Client {

    private static final QName SERVICE_NAME = new QName("http://wms.tcl.com/vendor_confirm", "SI_WMS_Confirm_OBService");

    private SIWMSConfirmOB_HTTPSPort_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = SIWMSConfirmOBService.WSDL_LOCATION;
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
      
        SIWMSConfirmOBService ss = new SIWMSConfirmOBService(wsdlURL, SERVICE_NAME);
        SIWMSConfirmOB port = ss.getHTTPSPort();  
        
        {
        System.out.println("Invoking siWMSConfirmOB...");
        com.vtradex.wms.webservice.client.sap.supplier.DTVENConfirm _siWMSConfirmOB_mmConfirmReq = null;
        com.vtradex.wms.webservice.client.sap.supplier.DTRetMsg _siWMSConfirmOB__return = port.siWMSConfirmOB(_siWMSConfirmOB_mmConfirmReq);
        System.out.println("siWMSConfirmOB.result=" + _siWMSConfirmOB__return);


        }

        System.exit(0);
    }

}
