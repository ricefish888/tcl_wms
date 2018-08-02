package com.vtradex.edi.server.service.sap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import com.vtradex.thorn.server.service.BaseManager;
import com.vtradex.wms.webservice.model.WsConstants;
import com.vtradex.wms.webservice.sap.model.SapReservedDataArray;
import com.vtradex.wms.webservice.sap.model.SapResponse;

/** 
* @ClassName: SAP预留主数据接口
* @Description: SapWebServiceReservedData 
* @author <a href="huayang.yuan@tech.vtradex.com">袁华洋</a>  
* @date 2017-7-12 上午9:51:20  
*/
@WebService(targetNamespace =WsConstants.NS_SAP_BASIC)
@BindingType(value =SOAPBinding.SOAP12HTTP_BINDING)
public interface SapWebServiceReservedData extends BaseManager{
	@WebMethod 
    @WebResult SapResponse reservedDataInfo(@WebParam SapReservedDataArray datas);
}
