package com.vtradex.wms.webservice.client.sap.invmove;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-08-09T15:54:32.729+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "https://wms.tcl.com/stock_move", name = "SI_Wms_Stock_Move_OB")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SIWmsStockMoveOB {

    @WebResult(name = "MM_Stock_Move_Resp", targetNamespace = "https://wms.tcl.com/stock_move", partName = "MM_Stock_Move_Resp")
    @WebMethod(operationName = "SI_Wms_Stock_Move_OB", action = "http://sap.com/xi/WebService/soap1.1")
    public DTRetMsg siWmsStockMoveOB(
        @WebParam(partName = "MM_Stock_Move_Req", name = "MM_Stock_Move_Req", targetNamespace = "https://wms.tcl.com/stock_move")
        DTStockMove mmStockMoveReq
    );
}
