//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 08:52:22 AM CEST 
//


package eu.apenet.dpt.utils.eag2012;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}relationEntry"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}descriptiveNote" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.archivesportaleurope.eu/profiles/eag/}href"/>
 *       &lt;attribute name="resourceRelationType" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="creatorOf"/>
 *             &lt;enumeration value="subjectOf"/>
 *             &lt;enumeration value="other"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "relationEntry",
    "descriptiveNote",
    "date",
    "dateRange",
    "dateSet"
})
@XmlRootElement(name = "resourceRelation")
public class ResourceRelation {

    @XmlElement(required = true)
    protected RelationEntry relationEntry;
    protected DescriptiveNote descriptiveNote;
    protected Date date;
    protected DateRange dateRange;
    protected DateSet dateSet;
    @XmlAttribute(name = "resourceRelationType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String resourceRelationType;
    @XmlAttribute(name = "href")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "lastDateTimeVerified")
    protected String lastDateTimeVerified;

    /**
     * Gets the value of the date property.
     *
     * @return
     *     possible object is
     *     {@link Date }
     *
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value
     *     allowed object is
     *     {@link Date }
     *
     */
    public void setDate(Date value) {
        this.date = value;
    }

    /**
     * Gets the value of the dateRange property.
     *
     * @return
     *     possible object is
     *     {@link DateRange }
     *
     */
    public DateRange getDateRange() {
        return dateRange;
    }

    /**
     * Sets the value of the dateRange property.
     *
     * @param value
     *     allowed object is
     *     {@link DateRange }
     *
     */
    public void setDateRange(DateRange value) {
        this.dateRange = value;
    }

    /**
     * Gets the value of the dateSet property.
     *
     * @return
     *     possible object is
     *     {@link DateSet }
     *
     */
    public DateSet getDateSet() {
        return dateSet;
    }

    /**
     * Sets the value of the dateSet property.
     *
     * @param value
     *     allowed object is
     *     {@link DateSet }
     *
     */
    public void setDateSet(DateSet value) {
        this.dateSet = value;
    }

    /**
     * Gets the value of the relationEntry property.
     * 
     * @return
     *     possible object is
     *     {@link RelationEntry }
     *     
     */
    public RelationEntry getRelationEntry() {
        return relationEntry;
    }

    /**
     * Sets the value of the relationEntry property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelationEntry }
     *     
     */
    public void setRelationEntry(RelationEntry value) {
        this.relationEntry = value;
    }

    /**
     * Gets the value of the descriptiveNote property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptiveNote }
     *     
     */
    public DescriptiveNote getDescriptiveNote() {
        return descriptiveNote;
    }

    /**
     * Sets the value of the descriptiveNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptiveNote }
     *     
     */
    public void setDescriptiveNote(DescriptiveNote value) {
        this.descriptiveNote = value;
    }

    /**
     * Gets the value of the resourceRelationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceRelationType() {
        return resourceRelationType;
    }

    /**
     * Sets the value of the resourceRelationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceRelationType(String value) {
        this.resourceRelationType = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    public String getLastDateTimeVerified() {
        return lastDateTimeVerified;
    }

    public void setLastDateTimeVerified(String lastDateTimeVerified) {
        this.lastDateTimeVerified = lastDateTimeVerified;
    }
}
