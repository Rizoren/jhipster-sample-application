package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.config.Constants;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.Subsistence;
import com.mycompany.myapp.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a subsistence, without Region and Document.
 */
public class SubsistenceDTO {

    private String quarterSL;
    private String yearSL;
    private Date dateAcceptSL;
    private Double valuePerCapitaSL;
    private Double valueForCapableSL;
    private Double valueForPensionersSL;
    private Double valueForChildrenSL;

    public SubsistenceDTO() {
        // Empty constructor needed for Jackson.
    }

    public SubsistenceDTO(Subsistence subsistence) {
        this.quarterSL = subsistence.getQuarterSL().toString();
        this.yearSL = subsistence.getYearSL();
        this.dateAcceptSL = Date.from(subsistence.getDateAcceptSL());
        this.valuePerCapitaSL = subsistence.getValuePerCapitaSL();
        this.valueForCapableSL = subsistence.getValueForCapableSL();
        this.valueForPensionersSL = subsistence.getValueForPensionersSL();
        this.valueForChildrenSL = subsistence.getValueForChildrenSL();
    }

    public String getQuarterSL() {
        return quarterSL;
    }

    public void setQuarterSL(String quarterSL) {
        this.quarterSL = quarterSL;
    }

    public String getYearSL() {
        return yearSL;
    }

    public void setYearSL(String yearSL) {
        this.yearSL = yearSL;
    }

    public Date getDateAcceptSL() {
        return dateAcceptSL;
    }

    public void setDateAcceptSL(Date dateAcceptSL) {
        this.dateAcceptSL = dateAcceptSL;
    }

    public Double getValuePerCapitaSL() {
        return valuePerCapitaSL;
    }

    public void setValuePerCapitaSL(Double valuePerCapitaSL) {
        this.valuePerCapitaSL = valuePerCapitaSL;
    }

    public Double getValueForCapableSL() {
        return valueForCapableSL;
    }

    public void setValueForCapableSL(Double valueForCapableSL) {
        this.valueForCapableSL = valueForCapableSL;
    }

    public Double getValueForPensionersSL() {
        return valueForPensionersSL;
    }

    public void setValueForPensionersSL(Double valueForPensionersSL) {
        this.valueForPensionersSL = valueForPensionersSL;
    }

    public Double getValueForChildrenSL() {
        return valueForChildrenSL;
    }

    public void setValueForChildrenSL(Double valueForChildrenSL) {
        this.valueForChildrenSL = valueForChildrenSL;
    }

    @Override
    public String toString() {
        return "SubsistenceDTO{" +
            "quarterSL='" + quarterSL + '\'' +
            ", yearSL='" + yearSL + '\'' +
            ", dateAcceptSL='" + dateAcceptSL.toString() + '\'' +
            ", valuePerCapitaSL='" + valuePerCapitaSL + '\'' +
            ", valueForCapableSL='" + valueForCapableSL + '\'' +
            ", valueForPensionersSL=" + valueForPensionersSL +
            ", valueForChildrenSL='" + valueForChildrenSL + '\'' +
            "}";
    }
}
