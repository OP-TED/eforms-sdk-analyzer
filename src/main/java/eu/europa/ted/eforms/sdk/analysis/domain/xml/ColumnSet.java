package eu.europa.ted.eforms.sdk.analysis.domain.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="Column" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="ShortName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                   &lt;element name="Data"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;attribute name="Lang" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                 &lt;attribute name="Use" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
@XmlType(name = "", propOrder = {"column"})
@XmlRootElement(name = "ColumnSet")
public class ColumnSet {

  @XmlElement(name = "Column", required = true)
  protected List<ColumnSet.Column> column;

  /**
   * Gets the value of the column property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the Jakarta XML Binding
   * object. This is why there is not a <CODE>set</CODE> method for the column property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getColumn().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link ColumnSet.Column }
   * 
   * 
   */
  public List<ColumnSet.Column> getColumn() {
    if (column == null) {
      column = new ArrayList<>();
    }
    return this.column;
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
   *         &lt;element name="Data"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;attribute name="Lang" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *                 &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *               &lt;/restriction&gt;
   *             &lt;/complexContent&gt;
   *           &lt;/complexType&gt;
   *         &lt;/element&gt;
   *       &lt;/sequence&gt;
   *       &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *       &lt;attribute name="Use" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
   *     &lt;/restriction&gt;
   *   &lt;/complexContent&gt;
   * &lt;/complexType&gt;
   * </pre>
   * 
   * 
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"shortName", "data"})
  public static class Column {

    @XmlElement(name = "ShortName", required = true)
    protected String shortName;
    @XmlElement(name = "Data", required = true)
    protected ColumnSet.Column.Data data;
    @XmlAttribute(name = "Id", required = true)
    protected String id;
    @XmlAttribute(name = "Use", required = true)
    protected String use;

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
     * Gets the value of the data property.
     * 
     * @return possible object is {@link ColumnSet.Column.Data }
     * 
     */
    public ColumnSet.Column.Data getData() {
      return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value allowed object is {@link ColumnSet.Column.Data }
     * 
     */
    public void setData(ColumnSet.Column.Data value) {
      this.data = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getId() {
      return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setId(String value) {
      this.id = value;
    }

    /**
     * Gets the value of the use property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUse() {
      return use;
    }

    /**
     * Sets the value of the use property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setUse(String value) {
      this.use = value;
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
     *       &lt;attribute name="Lang" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Data {

      @XmlAttribute(name = "Lang", required = true)
      protected String lang;
      @XmlAttribute(name = "Type", required = true)
      protected String type;

      /**
       * Gets the value of the lang property.
       * 
       * @return possible object is {@link String }
       * 
       */
      public String getLang() {
        return lang;
      }

      /**
       * Sets the value of the lang property.
       * 
       * @param value allowed object is {@link String }
       * 
       */
      public void setLang(String value) {
        this.lang = value;
      }

      /**
       * Gets the value of the type property.
       * 
       * @return possible object is {@link String }
       * 
       */
      public String getType() {
        return type;
      }

      /**
       * Sets the value of the type property.
       * 
       * @param value allowed object is {@link String }
       * 
       */
      public void setType(String value) {
        this.type = value;
      }

    }

  }

}
