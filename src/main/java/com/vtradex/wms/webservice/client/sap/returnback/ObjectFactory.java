
package com.vtradex.wms.webservice.client.sap.returnback;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.returnback package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _MMReceiptReturnReq_QNAME = new QName("http://wms.tcl.com/Receipt_Return", "MM_Receipt_Return_Req");
    private final static QName _MMReceiptReturnResp_QNAME = new QName("http://wms.tcl.com/Receipt_Return", "MM_Receipt_Return_Resp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.returnback
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTReceiptReturn }
     * 
     */
    public DTReceiptReturn createDTReceiptReturn() {
        return new DTReceiptReturn();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTReceiptReturn.ITEMS }
     * 
     */
    public DTReceiptReturn.ITEMS createDTReceiptReturnITEMS() {
        return new DTReceiptReturn.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTReceiptReturn }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Receipt_Return", name = "MM_Receipt_Return_Req")
    public JAXBElement<DTReceiptReturn> createMMReceiptReturnReq(DTReceiptReturn value) {
        return new JAXBElement<DTReceiptReturn>(_MMReceiptReturnReq_QNAME, DTReceiptReturn.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Receipt_Return", name = "MM_Receipt_Return_Resp")
    public JAXBElement<DTRetMsg> createMMReceiptReturnResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMReceiptReturnResp_QNAME, DTRetMsg.class, null, value);
    }

}
