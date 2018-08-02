
package com.vtradex.wms.webservice.client.sap.invmove;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.invmove package. 
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

    private final static QName _MMStockMoveReq_QNAME = new QName("https://wms.tcl.com/stock_move", "MM_Stock_Move_Req");
    private final static QName _MMStockMoveResp_QNAME = new QName("https://wms.tcl.com/stock_move", "MM_Stock_Move_Resp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.invmove
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTStockMove }
     * 
     */
    public DTStockMove createDTStockMove() {
        return new DTStockMove();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTStockMove.ITEMS }
     * 
     */
    public DTStockMove.ITEMS createDTStockMoveITEMS() {
        return new DTStockMove.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTStockMove }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://wms.tcl.com/stock_move", name = "MM_Stock_Move_Req")
    public JAXBElement<DTStockMove> createMMStockMoveReq(DTStockMove value) {
        return new JAXBElement<DTStockMove>(_MMStockMoveReq_QNAME, DTStockMove.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://wms.tcl.com/stock_move", name = "MM_Stock_Move_Resp")
    public JAXBElement<DTRetMsg> createMMStockMoveResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMStockMoveResp_QNAME, DTRetMsg.class, null, value);
    }

}
