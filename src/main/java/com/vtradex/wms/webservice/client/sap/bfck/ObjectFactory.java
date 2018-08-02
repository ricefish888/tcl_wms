
package com.vtradex.wms.webservice.client.sap.bfck;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.vtradex.wms.webservice.client.sap.bfck package. 
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

    private final static QName _MMScrapOutResp_QNAME = new QName("http://wms.tcl.com/Scrap_Out", "MM_Scrap_Out_Resp");
    private final static QName _MMScrapOutReq_QNAME = new QName("http://wms.tcl.com/Scrap_Out", "MM_Scrap_Out_Req");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.vtradex.wms.webservice.client.sap.bfck
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DTScrapOut }
     * 
     */
    public DTScrapOut createDTScrapOut() {
        return new DTScrapOut();
    }

    /**
     * Create an instance of {@link DTRetMsg }
     * 
     */
    public DTRetMsg createDTRetMsg() {
        return new DTRetMsg();
    }

    /**
     * Create an instance of {@link DTScrapOut.ITEMS }
     * 
     */
    public DTScrapOut.ITEMS createDTScrapOutITEMS() {
        return new DTScrapOut.ITEMS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTRetMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Scrap_Out", name = "MM_Scrap_Out_Resp")
    public JAXBElement<DTRetMsg> createMMScrapOutResp(DTRetMsg value) {
        return new JAXBElement<DTRetMsg>(_MMScrapOutResp_QNAME, DTRetMsg.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DTScrapOut }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://wms.tcl.com/Scrap_Out", name = "MM_Scrap_Out_Req")
    public JAXBElement<DTScrapOut> createMMScrapOutReq(DTScrapOut value) {
        return new JAXBElement<DTScrapOut>(_MMScrapOutReq_QNAME, DTScrapOut.class, null, value);
    }

}
