
package com.vtradex.wms.webservice.client.sap.commcallback;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="I_LOG_NO">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="32"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="I_TYP">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="TAB_RET">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZWMS_MSG_RET" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "ZRFC_WMS_MSG_RETURN")
public class ZRFCWMSMSGRETURN {

    @XmlElement(name = "I_LOG_NO", required = true)
    protected String ilogno;
    @XmlElement(name = "I_TYP", required = true)
    protected String ityp;
    @XmlElement(name = "TAB_RET", required = true)
    protected ZRFCWMSMSGRETURN.TABRET tabret;

    /**
     * 获取ilogno属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getILOGNO() {
        return ilogno;
    }

    /**
     * 设置ilogno属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setILOGNO(String value) {
        this.ilogno = value;
    }

    /**
     * 获取ityp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getITYP() {
        return ityp;
    }

    /**
     * 设置ityp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setITYP(String value) {
        this.ityp = value;
    }

    /**
     * 获取tabret属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ZRFCWMSMSGRETURN.TABRET }
     *     
     */
    public ZRFCWMSMSGRETURN.TABRET getTABRET() {
        return tabret;
    }

    /**
     * 设置tabret属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ZRFCWMSMSGRETURN.TABRET }
     *     
     */
    public void setTABRET(ZRFCWMSMSGRETURN.TABRET value) {
        this.tabret = value;
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
     *         &lt;element name="item" type="{urn:sap-com:document:sap:rfc:functions}ZWMS_MSG_RET" maxOccurs="unbounded" minOccurs="0"/>
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
        "item"
    })
    public static class TABRET {

        protected List<ZWMSMSGRET> item;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ZWMSMSGRET }
         * 
         * 
         */
        public List<ZWMSMSGRET> getItem() {
            if (item == null) {
                item = new ArrayList<ZWMSMSGRET>();
            }
            return this.item;
        }

    }

}
