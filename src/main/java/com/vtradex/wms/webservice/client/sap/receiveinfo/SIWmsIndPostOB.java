package com.vtradex.wms.webservice.client.sap.receiveinfo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-09-07T11:31:33.368+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "http://wms.tcl.com/indelivery_Post", name = "SI_Wms_Ind_Post_OB")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SIWmsIndPostOB {

    @WebResult(name = "MM_Ind_Post_Resp", targetNamespace = "http://wms.tcl.com/indelivery_Post", partName = "MM_Ind_Post_Resp")
    @WebMethod(operationName = "SI_Wms_Ind_Post_OB", action = "http://sap.com/xi/WebService/soap1.1")
    public DTRetMsg siWmsIndPostOB(
        @WebParam(partName = "MM_Ind_Post_Req", name = "MM_Ind_Post_Req", targetNamespace = "http://wms.tcl.com/indelivery_Post")
        DTINDPOST mmIndPostReq
    );
}