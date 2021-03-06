package com.vtradex.wms.webservice.client.sap.cancelreceive;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.6.2
 * 2017-07-24T17:46:57.772+08:00
 * Generated source version: 2.6.2
 * 
 */
@WebService(targetNamespace = "https://wms.tcl.com/Receipt_Undo", name = "SI_Wms_Receipt_Undo_OB")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SIWmsReceiptUndoOB {

    @WebResult(name = "MM_Receipt_Undo_Resp", targetNamespace = "https://wms.tcl.com/Receipt_Undo", partName = "MM_Receipt_Undo_Resp")
    @WebMethod(operationName = "SI_Wms_Receipt_Undo_OB", action = "http://sap.com/xi/WebService/soap1.1")
    public DTRetMsg siWmsReceiptUndoOB(
        @WebParam(partName = "MM_Receipt_Undo_Req", name = "MM_Receipt_Undo_Req", targetNamespace = "https://wms.tcl.com/Receipt_Undo")
        DTReceiptUndo mmReceiptUndoReq
    );
}
