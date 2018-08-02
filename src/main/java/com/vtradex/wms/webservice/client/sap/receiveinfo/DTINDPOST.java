
package com.vtradex.wms.webservice.client.sap.receiveinfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_IND_POST complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="DT_IND_POST">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COMMAND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FRBNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BUDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BLDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="XBLNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEMS" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LINE_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SOBKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MENGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="EBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="EBELP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="INSMK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TYPE_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBELN_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="VBELP_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SGTXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DT_IND_POST", propOrder = {
    "command",
    "count",
    "frbnr",
    "budat",
    "bldat",
    "xblnr",
    "items"
})
public class DTINDPOST {

    @XmlElement(name = "COMMAND")
    protected String command;
    @XmlElement(name = "COUNT")
    protected String count;
    @XmlElement(name = "FRBNR")
    protected String frbnr;
    @XmlElement(name = "BUDAT")
    protected String budat;
    @XmlElement(name = "BLDAT")
    protected String bldat;
    @XmlElement(name = "XBLNR")
    protected String xblnr;
    @XmlElement(name = "ITEMS")
    protected List<DTINDPOST.ITEMS> items;

    /**
     * 获取command属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOMMAND() {
        return command;
    }

    /**
     * 设置command属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOMMAND(String value) {
        this.command = value;
    }

    /**
     * 获取count属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCOUNT() {
        return count;
    }

    /**
     * 设置count属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCOUNT(String value) {
        this.count = value;
    }

    /**
     * 获取frbnr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFRBNR() {
        return frbnr;
    }

    /**
     * 设置frbnr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFRBNR(String value) {
        this.frbnr = value;
    }

    /**
     * 获取budat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUDAT() {
        return budat;
    }

    /**
     * 设置budat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUDAT(String value) {
        this.budat = value;
    }

    /**
     * 获取bldat属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBLDAT() {
        return bldat;
    }

    /**
     * 设置bldat属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBLDAT(String value) {
        this.bldat = value;
    }

    /**
     * 获取xblnr属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXBLNR() {
        return xblnr;
    }

    /**
     * 设置xblnr属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXBLNR(String value) {
        this.xblnr = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the items property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getITEMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DTINDPOST.ITEMS }
     * 
     * 
     */
    public List<DTINDPOST.ITEMS> getITEMS() {
        if (items == null) {
            items = new ArrayList<DTINDPOST.ITEMS>();
        }
        return this.items;
    }


    /**
     * <p>anonymous complex type的 Java 类。
     * 
     * <p>以下模式片段指定包含在此类中的预期内容。
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="LINE_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SOBKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MENGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MEINS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="EBELN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="EBELP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="INSMK" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TYPE_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBELN_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="VBELP_IM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SGTXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "lineno",
        "matnr",
        "werks",
        "lgort",
        "lifnr",
        "sobkz",
        "menge",
        "meins",
        "ebeln",
        "ebelp",
        "insmk",
        "typeim",
        "vbelnim",
        "vbelpim",
        "sgtxt"
    })
    public static class ITEMS {

        @XmlElement(name = "LINE_NO")
        protected String lineno;
        @XmlElement(name = "MATNR")
        protected String matnr;
        @XmlElement(name = "WERKS")
        protected String werks;
        @XmlElement(name = "LGORT")
        protected String lgort;
        @XmlElement(name = "LIFNR")
        protected String lifnr;
        @XmlElement(name = "SOBKZ")
        protected String sobkz;
        @XmlElement(name = "MENGE")
        protected String menge;
        @XmlElement(name = "MEINS")
        protected String meins;
        @XmlElement(name = "EBELN")
        protected String ebeln;
        @XmlElement(name = "EBELP")
        protected String ebelp;
        @XmlElement(name = "INSMK")
        protected String insmk;
        @XmlElement(name = "TYPE_IM")
        protected String typeim;
        @XmlElement(name = "VBELN_IM")
        protected String vbelnim;
        @XmlElement(name = "VBELP_IM")
        protected String vbelpim;
        @XmlElement(name = "SGTXT")
        protected String sgtxt;

        /**
         * 获取lineno属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLINENO() {
            return lineno;
        }

        /**
         * 设置lineno属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLINENO(String value) {
            this.lineno = value;
        }

        /**
         * 获取matnr属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMATNR() {
            return matnr;
        }

        /**
         * 设置matnr属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMATNR(String value) {
            this.matnr = value;
        }

        /**
         * 获取werks属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getWERKS() {
            return werks;
        }

        /**
         * 设置werks属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setWERKS(String value) {
            this.werks = value;
        }

        /**
         * 获取lgort属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLGORT() {
            return lgort;
        }

        /**
         * 设置lgort属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLGORT(String value) {
            this.lgort = value;
        }

        /**
         * 获取lifnr属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLIFNR() {
            return lifnr;
        }

        /**
         * 设置lifnr属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLIFNR(String value) {
            this.lifnr = value;
        }

        /**
         * 获取sobkz属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSOBKZ() {
            return sobkz;
        }

        /**
         * 设置sobkz属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSOBKZ(String value) {
            this.sobkz = value;
        }

        /**
         * 获取menge属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMENGE() {
            return menge;
        }

        /**
         * 设置menge属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMENGE(String value) {
            this.menge = value;
        }

        /**
         * 获取meins属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMEINS() {
            return meins;
        }

        /**
         * 设置meins属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMEINS(String value) {
            this.meins = value;
        }

        /**
         * 获取ebeln属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEBELN() {
            return ebeln;
        }

        /**
         * 设置ebeln属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEBELN(String value) {
            this.ebeln = value;
        }

        /**
         * 获取ebelp属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEBELP() {
            return ebelp;
        }

        /**
         * 设置ebelp属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEBELP(String value) {
            this.ebelp = value;
        }

        /**
         * 获取insmk属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getINSMK() {
            return insmk;
        }

        /**
         * 设置insmk属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setINSMK(String value) {
            this.insmk = value;
        }

        /**
         * 获取typeim属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTYPEIM() {
            return typeim;
        }

        /**
         * 设置typeim属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTYPEIM(String value) {
            this.typeim = value;
        }

        /**
         * 获取vbelnim属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVBELNIM() {
            return vbelnim;
        }

        /**
         * 设置vbelnim属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVBELNIM(String value) {
            this.vbelnim = value;
        }

        /**
         * 获取vbelpim属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getVBELPIM() {
            return vbelpim;
        }

        /**
         * 设置vbelpim属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setVBELPIM(String value) {
            this.vbelpim = value;
        }

        /**
         * 获取sgtxt属性的值。
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSGTXT() {
            return sgtxt;
        }

        /**
         * 设置sgtxt属性的值。
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSGTXT(String value) {
            this.sgtxt = value;
        }

    }

}
