package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapDeliveryOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;


/** 
* @ClassName: 采购交货单
* @Description: SapWebServicePurchaseOrder 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-5 下午2:03:02  
* 
*/
/**
 * SAP2wms  采购交货单数据
 * @return
 * */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebServiceDeliveryOrder {
	 	@WebMethod 
	    @WebResult SapResponse deliveryOrderInfo(@WebParam SapDeliveryOrderArray sapDeliveryOrders);
}
