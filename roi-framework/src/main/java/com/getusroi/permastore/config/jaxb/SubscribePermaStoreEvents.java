//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 03:02:31 PM IST 
//


package com.getusroi.permastore.config.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PermaStoreEvent">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="EventName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PermaStoreEventHandler" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="onEvent" use="required" type="{}event" />
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
@XmlType(name = "", propOrder = {
    "permaStoreEvent"
})
public class SubscribePermaStoreEvents
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "PermaStoreEvent", required = true)
    protected PermaStoreEvent permaStoreEvent;

    /**
     * Gets the value of the permaStoreEvent property.
     * 
     * @return
     *     possible object is
     *     {@link PermaStoreEvent }
     *     
     */
    public PermaStoreEvent getPermaStoreEvent() {
        return permaStoreEvent;
    }

    /**
     * Sets the value of the permaStoreEvent property.
     * 
     * @param value
     *     allowed object is
     *     {@link PermaStoreEvent }
     *     
     */
    public void setPermaStoreEvent(PermaStoreEvent value) {
        this.permaStoreEvent = value;
    }

}
