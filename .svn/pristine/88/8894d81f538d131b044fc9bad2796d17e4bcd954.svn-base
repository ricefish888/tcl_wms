package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapJSCheckOrderArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;

/** 
* @ClassName: 采购对账单寄售处理类
* @Description: SapJSCheckOrderWebService 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-14 下午5:43:28  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapJSCheckOrderWebService extends BaseManager{
	@WebMethod 
    @WebResult SapResponse checkJSOrderInfo(@WebParam SapJSCheckOrderArray scods);
}
