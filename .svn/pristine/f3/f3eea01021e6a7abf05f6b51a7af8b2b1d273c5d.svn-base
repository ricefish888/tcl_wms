package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapItemArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;



@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebService {  
    
    /***
     * SAP2wms  物料主数据
     * @param sapItems
     * @return
     */
    @WebMethod 
    @WebResult SapResponse itemInfo(@WebParam SapItemArray sapItems);
    
    
}  