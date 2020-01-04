package com.mycompany.myapp.jaxb;

import com.mycompany.myapp.domain.Subsistence;

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
 *         &lt;element name="subsistence" type="{http://spring.io/guides/gs-producing-web-service}subsistencetype"/>
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
    "subsistence"
})
@XmlRootElement(name = "getSubsistenceByQYRCRequest", namespace = "http://spring.io/guides/gs-producing-web-service")
public class GetSubsistenceResponse {

    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    @XmlElement(name = "subsistence", namespace = NAMESPACE_URI)
    protected Subsistence subsistence;

    public Subsistence getSubsistence() {
        return this.subsistence;
    }

    public void setSubsistence(Subsistence subsistence) {
        this.subsistence = subsistence;
    }
}
