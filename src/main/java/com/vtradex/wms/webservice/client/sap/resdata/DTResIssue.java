
package com.vtradex.wms.webservice.client.sap.resdata;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DT_Res_Issue complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DT_Res_Issue">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COMMAND" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COUNT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FRBNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BUDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BLDAT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ITEMS" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LINE_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MATNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="WERKS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LGORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SOBKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="BWART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SGTXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="MENGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="RSNUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="RSPOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DT_Res_Issue", propOrder = {
    "command",
    "count",
    "frbnr",
    "budat",
    "bldat",
    "items"
})
public class DTResIssue {

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
    @XmlElement(name = "ITEMS")
    protected List<DTResIssue.ITEMS> items;

    /**
     * ��ȡcommand���Ե�ֵ��
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
     * ����command���Ե�ֵ��
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
     * ��ȡcount���Ե�ֵ��
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
     * ����count���Ե�ֵ��
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
     * ��ȡfrbnr���Ե�ֵ��
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
     * ����frbnr���Ե�ֵ��
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
     * ��ȡbudat���Ե�ֵ��
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
     * ����budat���Ե�ֵ��
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
     * ��ȡbldat���Ե�ֵ��
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
     * ����bldat���Ե�ֵ��
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
     * {@link DTResIssue.ITEMS }
     * 
     * 
     */
    public List<DTResIssue.ITEMS> getITEMS() {
        if (items == null) {
            items = new ArrayList<DTResIssue.ITEMS>();
        }
        return this.items;
    }


    /**
     * <p>anonymous complex type�� Java �ࡣ
     * 
     * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
     *         &lt;element name="SOBKZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="LIFNR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="BWART" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="SGTXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="MENGE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RSNUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="RSPOS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "sobkz",
        "lifnr",
        "bwart",
        "sgtxt",
        "menge",
        "rsnum",
        "rspos"
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
        @XmlElement(name = "SOBKZ")
        protected String sobkz;
        @XmlElement(name = "LIFNR")
        protected String lifnr;
        @XmlElement(name = "BWART")
        protected String bwart;
        @XmlElement(name = "SGTXT")
        protected String sgtxt;
        @XmlElement(name = "MENGE")
        protected String menge;
        @XmlElement(name = "RSNUM")
        protected String rsnum;
        @XmlElement(name = "RSPOS")
        protected String rspos;

        /**
         * ��ȡlineno���Ե�ֵ��
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
         * ����lineno���Ե�ֵ��
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
         * ��ȡmatnr���Ե�ֵ��
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
         * ����matnr���Ե�ֵ��
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
         * ��ȡwerks���Ե�ֵ��
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
         * ����werks���Ե�ֵ��
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
         * ��ȡlgort���Ե�ֵ��
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
         * ����lgort���Ե�ֵ��
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
         * ��ȡsobkz���Ե�ֵ��
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
         * ����sobkz���Ե�ֵ��
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
         * ��ȡlifnr���Ե�ֵ��
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
         * ����lifnr���Ե�ֵ��
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
         * ��ȡbwart���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBWART() {
            return bwart;
        }

        /**
         * ����bwart���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBWART(String value) {
            this.bwart = value;
        }

        /**
         * ��ȡsgtxt���Ե�ֵ��
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
         * ����sgtxt���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSGTXT(String value) {
            this.sgtxt = value;
        }

        /**
         * ��ȡmenge���Ե�ֵ��
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
         * ����menge���Ե�ֵ��
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
         * ��ȡrsnum���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRSNUM() {
            return rsnum;
        }

        /**
         * ����rsnum���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRSNUM(String value) {
            this.rsnum = value;
        }

        /**
         * ��ȡrspos���Ե�ֵ��
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRSPOS() {
            return rspos;
        }

        /**
         * ����rspos���Ե�ֵ��
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRSPOS(String value) {
            this.rspos = value;
        }

    }

}
