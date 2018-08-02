package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapProductOrderInArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;

/** 
* @ClassName: Sap-Wms生产订单入库接口
* @Description: SapProductOrderInWebService 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-18 上午10:26:11  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapProductOrderInWebService extends BaseManager{
	@WebMethod 
    @WebResult SapResponse productOrderIn(@WebParam SapProductOrderInArray spois);
}
