//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.04 at 12:52:00 PM CET 
//


package eu.apenet.dpt.utils.eaccpf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cpfDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cpfDescription"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="identity" type="{urn:isbn:1-931666-33-4}identity"/&gt;
 *         &lt;element name="description" type="{urn:isbn:1-931666-33-4}description"/&gt;
 *         &lt;element name="relations" type="{urn:isbn:1-931666-33-4}relations" minOccurs="0"/&gt;
 *         &lt;element name="alternativeSet" type="{urn:isbn:1-931666-33-4}alternativeSet" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cpfDescription", propOrder = {
    "identity",
    "description",
    "relations",
    "alternativeSet"
})
public class CpfDescription {

    @XmlElement(required = true)
    protected Identity identity;
    @XmlElement(required = true)
    protected Description description;
    protected Relations relations;
    protected AlternativeSet alternativeSet;

    /**
     * Gets the value of the identity property.
     * 
     * @return
     *     possible object is
     *     {@link Identity }
     *     
     */
    public Identity getIdentity() {
        return identity;
    }

    /**
     * Sets the value of the identity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identity }
     *     
     */
    public void setIdentity(Identity value) {
        this.identity = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the relations property.
     * 
     * @return
     *     possible object is
     *     {@link Relations }
     *     
     */
    public Relations getRelations() {
        return relations;
    }

    /**
     * Sets the value of the relations property.
     * 
     * @param value
     *     allowed object is
     *     {@link Relations }
     *     
     */
    public void setRelations(Relations value) {
        this.relations = value;
    }

    /**
     * Gets the value of the alternativeSet property.
     * 
     * @return
     *     possible object is
     *     {@link AlternativeSet }
     *     
     */
    public AlternativeSet getAlternativeSet() {
        return alternativeSet;
    }

    /**
     * Sets the value of the alternativeSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlternativeSet }
     *     
     */
    public void setAlternativeSet(AlternativeSet value) {
        this.alternativeSet = value;
    }

}
