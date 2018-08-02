
package com.vtradex.wms.webservice.client.sap.saleoutdelivery;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.saleoutdelivery package. 
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

    private final static QName _MMOutPostReq_QNAME = new QName("http://wms.tcl.com/Outbound_Post", "MM_Out_Post_Req");
    private final static QName _MMOutPostResp_QNAME = new QName("http://wms.tcl.com/Outbound_Post", "MM_Out_Post_Resp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.saleoutdelivery
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTODVPOST }
     * 
     */
    public DTODVPOST createDTODVPOST() {
        return new DTODVPOST();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTODVPOST.ITEMS }
     * 
     */
    public DTODVPOST.ITEMS createDTODVPOSTITEMS() {
        return new DTODVPOST.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTODVPOST }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Outbound_Post", name = "MM_Out_Post_Req")
    public JAXBElement<DTODVPOST> createMMOutPostReq(DTODVPOST value) {
        return new JAXBElement<DTODVPOST>(_MMOutPostReq_QNAME, DTODVPOST.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Outbound_Post", name = "MM_Out_Post_Resp")
    public JAXBElement<DTRetMsg> createMMOutPostResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMOutPostResp_QNAME, DTRetMsg.class, null, value);
    }

}
