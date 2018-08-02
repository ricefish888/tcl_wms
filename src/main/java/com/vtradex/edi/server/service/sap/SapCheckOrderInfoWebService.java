package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.base.SapCommonCallbackDetail;
import com.vtradex.wms.webservice.sap.model.SapCheckOrderInfoArray;
/**
 * 校验SAP单据是否可以修改
 * @author Administrator
 *
 */
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapCheckOrderInfoWebService extends BaseManager{
	@WebMethod 
	@WebResult SapCommonCallbackDetail CheckOrderInfo(@WebParam SapCheckOrderInfoArray scoi);
}
