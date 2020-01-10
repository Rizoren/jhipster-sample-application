package com.mycompany.myapp.jaxb;

import javax.xml.bind.annotation.*;

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
 *         &lt;element name="acceptdate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="regioncode" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "acceptdate",
    "regioncode"
})
@XmlRootElement(name = "getSubsistenceByQYRCRequest", namespace = "http://localhost:8080")
public class GetSubsistenceByDateRequest {

    private static final String NAMESPACE_URI = "http://localhost:8080";

    @XmlElement(name = "acceptdate")
    protected String acceptdate;
    @XmlElement(name = "regioncode", namespace = NAMESPACE_URI)
    protected String regioncode;

    public String getAcceptdate() {
        return this.acceptdate;
    }

    public String getRegioncode() {
        return this.regioncode;
    }
}
