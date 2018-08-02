package com.vtradex.wms.webservice.client.sap.commcallback;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-07-04T12:04:31+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "urn:sap-com:document:sap:rfc:functions", name = "ZRFC_WMS_MSG_RETURN.PortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ZRFCWMSMSGRETURNPortType {

    @WebResult(name = "ZRFC_WMS_MSG_RETURN.Response", targetNamespace = "urn:sap-com:document:sap:rfc:functions", partName = "parameters")
    @WebMethod(operationName = "ZRFC_WMS_MSG_RETURN", action = "http://sap.com/xi/WebService/soap1.1")
    public ZRFCWMSMSGRETURNResponse zrfcWMSMSGRETURN(
        @WebParam(partName = "parameters", name = "ZRFC_WMS_MSG_RETURN", targetNamespace = "urn:sap-com:document:sap:rfc:functions")
        ZRFCWMSMSGRETURN parameters
    );
}
