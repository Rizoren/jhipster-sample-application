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
 *         &lt;element name="quarter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "quarter",
    "year",
    "regioncode"
})
@XmlRootElement(name = "getSubsistenceByQYRCRequest", namespace = "http://spring.io/guides/gs-producing-web-service")
public class GetSubsistenceByQYRCRequest {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @XmlElement(name = "quarter", namespace = NAMESPACE_URI)
    protected String quarter;
    @XmlElement(name = "year", namespace = NAMESPACE_URI)
    protected String year;
    @XmlElement(name = "regioncode", namespace = NAMESPACE_URI)
    protected String regioncode;

    public String getQuarter() {
        return this.quarter;
    }

    public String getYear() {
        return this.year;
    }

    public String getRegioncode() {
        return this.regioncode;
    }
}
