//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 08:52:22 AM CEST 
//


package eu.apenet.dpt.utils.eag2012;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}restorationlab" minOccurs="0"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}reproductionser"/>
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
    "restorationlab",
    "reproductionser"
})
@XmlRootElement(name = "techservices")
public class Techservices {

    protected Restorationlab restorationlab;
    @XmlElement(required = true)
    protected Reproductionser reproductionser;

    /**
     * Gets the value of the restorationlab property.
     * 
     * @return
     *     possible object is
     *     {@link Restorationlab }
     *     
     */
    public Restorationlab getRestorationlab() {
        return restorationlab;
    }

    /**
     * Sets the value of the restorationlab property.
     * 
     * @param value
     *     allowed object is
     *     {@link Restorationlab }
     *     
     */
    public void setRestorationlab(Restorationlab value) {
        this.restorationlab = value;
    }

    /**
     * Gets the value of the reproductionser property.
     * 
     * @return
     *     possible object is
     *     {@link Reproductionser }
     *     
     */
    public Reproductionser getReproductionser() {
        return reproductionser;
    }

    /**
     * Sets the value of the reproductionser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reproductionser }
     *     
     */
    public void setReproductionser(Reproductionser value) {
        this.reproductionser = value;
    }

}
