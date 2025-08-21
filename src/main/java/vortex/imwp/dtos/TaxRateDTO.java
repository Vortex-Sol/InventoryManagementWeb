package vortex.imwp.dtos;

import vortex.imwp.mappers.SettingsDTOMapper;
import vortex.imwp.models.Country;
import vortex.imwp.models.Settings;

import java.util.ArrayList;
import java.util.List;

public class TaxRateDTO {
    private Long id;
    private String country;
    private Double standardRate;
    private Double reducedRate;
    private Double superReducedRate;
    private Double noneRate;
    private Double otherRate;


    public TaxRateDTO(Long id, String country, Double standardRate, Double reducedRate, Double superReducedRate, Double noneRate, Double otherRate) {
        this.id = id;
        this.country = country;
        this.standardRate = standardRate;
        this.reducedRate = reducedRate;
        this.superReducedRate = superReducedRate;
        this.noneRate = noneRate;
        this.otherRate = otherRate;

    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getCountry() {return country;}
    public void setCountry(String country) {this.country = country;}
    public Double getStandardRate() {return standardRate;}
    public void setStandardRate(Double standardRate) {this.standardRate = standardRate;}
    public Double getReducedRate() {return reducedRate;}
    public void setReducedRate(Double reducedRate) {this.reducedRate = reducedRate;}
    public Double getSuperReducedRate() {return superReducedRate;}
    public void setSuperReducedRate(Double superReducedRate) {this.superReducedRate = superReducedRate;}
    public Double getNoneRate() {return noneRate;}
    public void setNoneRate(Double noneRate) {this.noneRate = noneRate;}
    public Double getOtherRate() {return otherRate;}
    public void setOtherRate(Double otherRate) {this.otherRate = otherRate;}



    @Override
    public String toString() {
        return "TaxRateDTO{" +
                "otherRate=" + otherRate +
                ", noneRate=" + noneRate +
                ", superReducedRate=" + superReducedRate +
                ", reducedRate=" + reducedRate +
                ", standardRate=" + standardRate +
                ", country='" + country + '\'' +
                ", id=" + id +
                '}';
    }
}
