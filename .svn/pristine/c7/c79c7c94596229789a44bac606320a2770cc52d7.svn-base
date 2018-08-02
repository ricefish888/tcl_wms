package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapProductOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;

/** 
* @ClassName: SAP2wms 生产订单接口
* @Description: SapWebServiceProductOrder 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-7 下午3:06:30  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebServiceProductOrder extends BaseManager{
	@WebMethod 
    @WebResult SapResponse productOrderInfo(@WebParam SapProductOrderArray spoas);
}
