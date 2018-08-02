package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapResponse;
import com.vtradex.wms.webservice.sap.model.SapSupplierArray;

/***
 * SAP2wms  供应商主数据  SAP要求一个接口一个服务，不能在一个服务中写多个方法实现多个服务
 * @param sapSuppliers
 * @return
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebServiceSupplier {
	
	  /***
     * SAP2wms  供应商主数据
     * @param sapSuppliers
     * @return
     */
    @WebMethod 
    @WebResult SapResponse supplierInfo(@WebParam SapSupplierArray sapSuppliers);
    
}
