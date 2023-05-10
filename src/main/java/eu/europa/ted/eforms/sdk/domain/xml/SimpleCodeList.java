package eu.europa.ted.eforms.sdk.domain.xml;

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
 *         &lt;element name="Row" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="Value" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="SimpleValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *                           &lt;/sequence&gt;
 *                           &lt;attribute name="ColumnRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
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
@XmlType(name = "", propOrder = {"row"})
@XmlRootElement(name = "SimpleCodeList")
public class SimpleCodeList {
  @XmlElement(name = "Row", required = true)
  protected List<SimpleCodeList.Row> row;

  /**
   * Gets the value of the row property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the Jakarta XML Binding
   * object. This is why there is not a <CODE>set</CODE> method for the row property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getRow().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link SimpleCodeList.Row }
   * 
   * 
   */
  public List<SimpleCodeList.Row> getRow() {
    if (row == null) {
      row = new ArrayList<SimpleCodeList.Row>();
    }
    return this.row;
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
   *         &lt;element name="Value" maxOccurs="unbounded"&gt;
   *           &lt;complexType&gt;
   *             &lt;complexContent&gt;
   *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   *                 &lt;sequence&gt;
   *                   &lt;element name="SimpleValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   *                 &lt;/sequence&gt;
   *                 &lt;attribute name="ColumnRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
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
  @XmlType(name = "", propOrder = {"value"})
  public static class Row {

    @XmlElement(name = "Value", required = true)
    protected List<SimpleCodeList.Row.Value> value;

    /**
     * Gets the value of the value property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the Jakarta XML Binding
     * object. This is why there is not a <CODE>set</CODE> method for the value property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link SimpleCodeList.Row.Value }
     * 
     * 
     */
    public List<SimpleCodeList.Row.Value> getValue() {
      if (value == null) {
        value = new ArrayList<SimpleCodeList.Row.Value>();
      }
      return this.value;
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
     *         &lt;element name="SimpleValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="ColumnRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"simpleValue"})
    public static class Value {

      @XmlElement(name = "SimpleValue", required = true)
      protected String simpleValue;
      @XmlAttribute(name = "ColumnRef", required = true)
      protected String columnRef;

      /**
       * Gets the value of the simpleValue property.
       * 
       * @return possible object is {@link String }
       * 
       */
      public String getSimpleValue() {
        return simpleValue;
      }

      /**
       * Sets the value of the simpleValue property.
       * 
       * @param value allowed object is {@link String }
       * 
       */
      public void setSimpleValue(String value) {
        this.simpleValue = value;
      }

      /**
       * Gets the value of the columnRef property.
       * 
       * @return possible object is {@link String }
       * 
       */
      public String getColumnRef() {
        return columnRef;
      }

      /**
       * Sets the value of the columnRef property.
       * 
       * @param value allowed object is {@link String }
       * 
       */
      public void setColumnRef(String value) {
        this.columnRef = value;
      }
    }
  }
}
