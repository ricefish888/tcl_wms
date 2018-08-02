package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.sap.model.SapReturnOrderCodeArray;

 
/**
 * SAP2wms  单号回传接口  交货单WMS传给SAP,SAP处理成功后回传单号给WMS
 * @return
 * */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebServiceReturnOrderCode {
	 	@WebMethod 
	    @WebResult SapResponse returnOrderCodeInfo(@WebParam SapReturnOrderCodeArray sapReturnOrderCodes);
}
