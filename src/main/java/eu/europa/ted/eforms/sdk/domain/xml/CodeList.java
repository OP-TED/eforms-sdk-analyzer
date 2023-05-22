package eu.europa.ted.eforms.sdk.domain.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}Identification"/&gt;
 *         &lt;element ref="{}ColumnSet"/&gt;
 *         &lt;element ref="{}SimpleCodeList"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"identification", "columnSet", "simpleCodeList"})
@XmlRootElement(name = "gc:CodeList")
public class CodeList {

  @XmlElement(name = "Identification", required = true)
  protected Identification identification;
  @XmlElement(name = "ColumnSet", required = true)
  protected ColumnSet columnSet;
  @XmlElement(name = "SimpleCodeList", required = true)
  protected SimpleCodeList simpleCodeList;

  /**
   * Gets the value of the identification property.
   * 
   * @return possible object is {@link Identification }
   * 
   */
  public Identification getIdentification() {
    return identification;
  }

  /**
   * Sets the value of the identification property.
   * 
   * @param value allowed object is {@link Identification }
   * 
   */
  public void setIdentification(Identification value) {
    this.identification = value;
  }

  /**
   * Gets the value of the columnSet property.
   * 
   * @return possible object is {@link ColumnSet }
   * 
   */
  public ColumnSet getColumnSet() {
    return columnSet;
  }

  /**
   * Sets the value of the columnSet property.
   * 
   * @param value allowed object is {@link ColumnSet }
   * 
   */
  public void setColumnSet(ColumnSet value) {
    this.columnSet = value;
  }

  /**
   * Gets the value of the simpleCodeList property.
   * 
   * @return possible object is {@link SimpleCodeList }
   * 
   */
  public SimpleCodeList getSimpleCodeList() {
    return simpleCodeList;
  }

  /**
   * Sets the value of the simpleCodeList property.
   * 
   * @param value allowed object is {@link SimpleCodeList }
   * 
   */
  public void setSimpleCodeList(SimpleCodeList value) {
    this.simpleCodeList = value;
  }

}
