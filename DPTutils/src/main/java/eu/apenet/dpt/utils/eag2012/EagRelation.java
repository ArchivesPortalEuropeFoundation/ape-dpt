//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 08:52:22 AM CEST 
//


package eu.apenet.dpt.utils.eag2012;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}relationEntry" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}descriptiveNote" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://www.archivesportaleurope.eu/profiles/eag/}href"/>
 *       &lt;attribute name="eagRelationType" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;enumeration value="hierarchical-child"/>
 *             &lt;enumeration value="hierarchical-parent"/>
 *             &lt;enumeration value="temporal-earlier"/>
 *             &lt;enumeration value="temporal-later"/>
 *             &lt;enumeration value="associative"/>
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
@XmlRootElement(name = "eagRelation")
public class EagRelation {

    protected List<RelationEntry> relationEntry;
    protected DescriptiveNote descriptiveNote;
    protected Date date;
    protected DateRange dateRange;
    protected DateSet dateSet;
    @XmlAttribute(name = "eagRelationType", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String eagRelationType;
    @XmlAttribute(name = "href")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(name = "lastDateTimeVerified")
    protected String lastDateTimeVerified;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange;
    }

    public DateSet getDateSet() {
        return dateSet;
    }

    public void setDateSet(DateSet dateSet) {
        this.dateSet = dateSet;
    }

    /**
     * Gets the value of the relationEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relationEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelationEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelationEntry }
     * 
     * 
     */
    public List<RelationEntry> getRelationEntry() {
        if (relationEntry == null) {
            relationEntry = new ArrayList<RelationEntry>();
        }
        return this.relationEntry;
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
     * Gets the value of the eagRelationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEagRelationType() {
        return eagRelationType;
    }

    /**
     * Sets the value of the eagRelationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEagRelationType(String value) {
        this.eagRelationType = value;
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
