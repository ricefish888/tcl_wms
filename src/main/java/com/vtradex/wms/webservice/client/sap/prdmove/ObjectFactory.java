
package com.vtradex.wms.webservice.client.sap.prdmove;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.prdmove package. 
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

    private final static QName _MMPrdMoveReq_QNAME = new QName("https://wms.tcl.com/prd_move", "MM_Prd_Move_Req");
    private final static QName _MMPrdMoveResp_QNAME = new QName("https://wms.tcl.com/prd_move", "MM_Prd_Move_Resp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.prdmove
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTPrdMove }
     * 
     */
    public DTPrdMove createDTPrdMove() {
        return new DTPrdMove();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTPrdMove.ITEMS }
     * 
     */
    public DTPrdMove.ITEMS createDTPrdMoveITEMS() {
        return new DTPrdMove.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTPrdMove }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://wms.tcl.com/prd_move", name = "MM_Prd_Move_Req")
    public JAXBElement<DTPrdMove> createMMPrdMoveReq(DTPrdMove value) {
        return new JAXBElement<DTPrdMove>(_MMPrdMoveReq_QNAME, DTPrdMove.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "https://wms.tcl.com/prd_move", name = "MM_Prd_Move_Resp")
    public JAXBElement<DTRetMsg> createMMPrdMoveResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMPrdMoveResp_QNAME, DTRetMsg.class, null, value);
    }

}
