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
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}refreshment" minOccurs="0"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}exhibition" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}toursSessions" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.archivesportaleurope.eu/profiles/eag/}otherServices" maxOccurs="unbounded" minOccurs="0"/>
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
    "refreshment",
    "exhibition",
    "toursSessions",
    "otherServices"
})
@XmlRootElement(name = "recreationalServices")
public class RecreationalServices {

    protected Refreshment refreshment;
    protected List<Exhibition> exhibition;
    protected List<ToursSessions> toursSessions;
    protected List<OtherServices> otherServices;

    /**
     * Gets the value of the refreshment property.
     * 
     * @return
     *     possible object is
     *     {@link Refreshment }
     *     
     */
    public Refreshment getRefreshment() {
        return refreshment;
    }

    /**
     * Sets the value of the refreshment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Refreshment }
     *     
     */
    public void setRefreshment(Refreshment value) {
        this.refreshment = value;
    }

    /**
     * Gets the value of the exhibition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exhibition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExhibition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Exhibition }
     * 
     * 
     */
    public List<Exhibition> getExhibition() {
        if (exhibition == null) {
            exhibition = new ArrayList<Exhibition>();
        }
        return this.exhibition;
    }

    /**
     * Gets the value of the toursSessions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the toursSessions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getToursSessions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ToursSessions }
     * 
     * 
     */
    public List<ToursSessions> getToursSessions() {
        if (toursSessions == null) {
            toursSessions = new ArrayList<ToursSessions>();
        }
        return this.toursSessions;
    }

    /**
     * Gets the value of the otherServices property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherServices property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherServices().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OtherServices }
     * 
     * 
     */
    public List<OtherServices> getOtherServices() {
        if (otherServices == null) {
            otherServices = new ArrayList<OtherServices>();
        }
        return this.otherServices;
    }

}
