package eu.europa.ted.eforms.sdk.analysis.domain.xml;

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
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="entry" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                 &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
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
@XmlType(name = "", propOrder = {"comment", "entry"})
@XmlRootElement(name = "properties")
public class Properties {

  @XmlElement(required = true)
  protected String comment;
  @XmlElement(required = true)
  protected List<Properties.Entry> entry;

  /**
   * Gets the value of the comment property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the value of the comment property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setComment(String value) {
    this.comment = value;
  }

  /**
   * Gets the value of the entry property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the Jakarta XML Binding
   * object. This is why there is not a <CODE>set</CODE> method for the entry property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getEntry().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Properties.Entry }
   * 
   * 
   */
  public List<Properties.Entry> getEntry() {
    if (entry == null) {
      entry = new ArrayList<Properties.Entry>();
    }
    return this.entry;
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
   *       &lt;attribute name="key" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *     &lt;/extension&gt;
   *   &lt;/simpleContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"value"})
  public static class Entry {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "key", required = true)
    protected String key;

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
     * Gets the value of the key property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getKey() {
      return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setKey(String value) {
      this.key = value;
    }
  }
}
