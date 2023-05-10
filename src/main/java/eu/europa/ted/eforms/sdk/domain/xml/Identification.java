package eu.europa.ted.eforms.sdk.domain.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LongName" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                 &lt;attribute name="Identifier" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CanonicalUri" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="CanonicalVersionUri" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="Agency"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="LongName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "shortName",
    "longName",
    "version",
    "canonicalUri",
    "canonicalVersionUri",
    "agency"
})
@XmlRootElement(name = "Identification")
public class Identification {

  @XmlElement(name = "ShortName", required = true)
  protected String shortName;
  @XmlElement(name = "LongName", required = true)
  protected List<Identification.LongName> longName;
  @XmlElement(name = "Version", required = true)
  protected String version;
  @XmlElement(name = "CanonicalUri", required = true)
  protected Object canonicalUri;
  @XmlElement(name = "CanonicalVersionUri", required = true)
  protected Object canonicalVersionUri;
  @XmlElement(name = "Agency", required = true)
  protected Identification.Agency agency;

  /**
   * Gets the value of the shortName property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Sets the value of the shortName property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setShortName(String value) {
    this.shortName = value;
  }

  /**
   * Gets the value of the longName property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the Jakarta XML Binding
   * object. This is why there is not a <CODE>set</CODE> method for the longName property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getLongName().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Identification.LongName }
   * 
   * 
   */
  public List<Identification.LongName> getLongName() {
    if (longName == null) {
      longName = new ArrayList<Identification.LongName>();
    }
    return this.longName;
  }

  /**
   * Gets the value of the version property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the value of the version property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setVersion(String value) {
    this.version = value;
  }

  /**
   * Gets the value of the canonicalUri property.
   * 
   * @return possible object is {@link Object }
   * 
   */
  public Object getCanonicalUri() {
    return canonicalUri;
  }

  /**
   * Sets the value of the canonicalUri property.
   * 
   * @param value allowed object is {@link Object }
   * 
   */
  public void setCanonicalUri(Object value) {
    this.canonicalUri = value;
  }

  /**
   * Gets the value of the canonicalVersionUri property.
   * 
   * @return possible object is {@link Object }
   * 
   */
  public Object getCanonicalVersionUri() {
    return canonicalVersionUri;
  }

  /**
   * Sets the value of the canonicalVersionUri property.
   * 
   * @param value allowed object is {@link Object }
   * 
   */
  public void setCanonicalVersionUri(Object value) {
    this.canonicalVersionUri = value;
  }

  /**
   * Gets the value of the agency property.
   * 
   * @return possible object is {@link Identification.Agency }
   * 
   */
  public Identification.Agency getAgency() {
    return agency;
  }

  /**
   * Sets the value of the agency property.
   * 
   * @param value allowed object is {@link Identification.Agency }
   * 
   */
  public void setAgency(Identification.Agency value) {
    this.agency = value;
  }


  /**
   * <p>
   * Java class for anonymous complex type.
   * 
   * <p>
   * The following schema fragment specifies the expected content contained within this class.
   * 
   * <pre>
   * &lt;complexType&gt;
   *   &lt;complexContent&gt;
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *       &lt;sequence&gt;
   *         &lt;element name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *         &lt;element name="LongName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *       &lt;/sequence&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"shortName", "longName"})
  public static class Agency {

    @XmlElement(name = "ShortName", required = true)
    protected String shortName;
    @XmlElement(name = "LongName", required = true)
    protected String longName;

    /**
     * Gets the value of the shortName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getShortName() {
      return shortName;
    }

    /**
     * Sets the value of the shortName property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setShortName(String value) {
      this.shortName = value;
    }

    /**
     * Gets the value of the longName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLongName() {
      return longName;
    }

    /**
     * Sets the value of the longName property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setLongName(String value) {
      this.longName = value;
    }

  }


  /**
   * <p>
   * Java class for anonymous complex type.
   * 
   * <p>
   * The following schema fragment specifies the expected content contained within this class.
   * 
   * <pre>
   * &lt;complexType&gt;
   *   &lt;simpleContent&gt;
   *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
   *       &lt;attribute name="Identifier" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *     &lt;/extension&gt;
   *   &lt;/simpleContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"value"})
  public static class LongName {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "Identifier")
    protected String identifier;

    /**
     * Gets the value of the value property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getValue() {
      return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
      this.value = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIdentifier() {
      return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setIdentifier(String value) {
      this.identifier = value;
    }
  }
}
