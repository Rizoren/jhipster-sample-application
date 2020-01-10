package com.mycompany.myapp.jaxb;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.service.dto.SubsistenceDTO;

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
 *         &lt;element name="subsistence" type="{http://localhost:8080}subsistencetype"/>
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
@XmlRootElement(name = "getSubsistenceByQYRCRequest", namespace = Constants.NAMESPACE_URI)
public class GetSubsistenceResponse {

    @XmlElement(name = "subsistence", namespace = Constants.NAMESPACE_URI)
    protected SubsistenceDTO subsistence;

    public SubsistenceDTO getSubsistence() {
        return this.subsistence;
    }

    public void setSubsistence(SubsistenceDTO subsistence) {
        this.subsistence = subsistence;
    }
}
