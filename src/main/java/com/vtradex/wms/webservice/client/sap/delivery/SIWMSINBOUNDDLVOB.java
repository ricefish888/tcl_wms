package com.vtradex.wms.webservice.client.sap.delivery;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-07-14T14:30:32.064+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "http://wms.tcl.com/inbound_delivery", name = "SI_WMS_INBOUND_DLV_OB")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SIWMSINBOUNDDLVOB {

    @WebResult(name = "MM_INDLV_Resp", targetNamespace = "http://wms.tcl.com/inbound_delivery", partName = "MM_INDLV_Resp")
    @WebMethod(operationName = "SI_WMS_INBOUND_DLV_OB", action = "http://sap.com/xi/WebService/soap1.1")
    public DTRetMsg siWMSINBOUNDDLVOB(
        @WebParam(partName = "MM_INDLV_Req", name = "MM_INDLV_Req", targetNamespace = "http://wms.tcl.com/inbound_delivery")
        DTINDLV mmINDLVReq
    );
}
