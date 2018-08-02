
package com.vtradex.wms.webservice.client.sap.inv;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.inv package. 
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

    private final static QName _MMSTOCKReq_QNAME = new QName("http://wms.tcl.com/wms_stock", "MM_STOCK_Req");
    private final static QName _MMSTOCKResp_QNAME = new QName("http://wms.tcl.com/wms_stock", "MM_STOCK_Resp");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.inv
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTSTOCK }
     * 
     */
    public DTSTOCK createDTSTOCK() {
        return new DTSTOCK();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTSTOCK.ITEMS }
     * 
     */
    public DTSTOCK.ITEMS createDTSTOCKITEMS() {
        return new DTSTOCK.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTSTOCK }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/wms_stock", name = "MM_STOCK_Req")
    public JAXBElement<DTSTOCK> createMMSTOCKReq(DTSTOCK value) {
        return new JAXBElement<DTSTOCK>(_MMSTOCKReq_QNAME, DTSTOCK.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/wms_stock", name = "MM_STOCK_Resp")
    public JAXBElement<DTRetMsg> createMMSTOCKResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMSTOCKResp_QNAME, DTRetMsg.class, null, value);
    }

}
