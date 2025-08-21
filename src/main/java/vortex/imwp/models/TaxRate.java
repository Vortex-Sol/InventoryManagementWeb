package vortex.imwp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.mappers.SettingsDTOMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name ="Tax_Rate")
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "Country", nullable = false)
    @NotNull(message = "Country is required")
    private Country.Name country;
    @Column(name = "Standard_Rate", nullable = false)
    private Double standardRate;
    @Column(name = "Reduced_Rate")
    private Double reducedRate;
    @Column(name = "Super_Reduced_Rate")
    private Double superReducedRate;
    @Column(name = "None_Rate")
    private Double noneRate;
    @Column(name = "Other_Rate")
    private Double otherRate;

    public TaxRate(Country.Name country, Double standardRate, Double reducedRate, Double superReducedRate, Double noneRate, Double otherRate) {
        this.country = country;
        this.standardRate = standardRate;
        this.reducedRate = reducedRate;
        this.superReducedRate = superReducedRate;
        this.noneRate = noneRate;
        this.otherRate = otherRate;
    }
    public TaxRate() {}

    public TaxRate(Long id, Country.Name country, Double standardRate, Double reducedRate, Double superReducedRate, Double noneRate, Double otherRate) {
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
    public Country.Name getCountry() {return country;}
    public void setCountry(Country.Name country) {this.country = country;}
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


    public Double getRateByCategory(Category category) {
        if (category == null) return null;

        String name = category.getName();
        if (name == null) return null;

        return switch (name) {
            case "A" -> standardRate;
            case "B" -> reducedRate;
            case "C" -> superReducedRate;
            case "D" -> noneRate;
            case "E" -> otherRate;
            default -> null;
        };
    }

    public void editRateByCategory(Category category, Double newRate) {
        if (category == null || category.getName() == null) return;
        switch (category.getName()) {
            case "A" -> this.standardRate = newRate;
            case "B" -> this.reducedRate = newRate;
            case "C" -> this.superReducedRate = newRate;
            case "D" -> this.noneRate = newRate;
            case "E" -> this.otherRate = newRate;
            default -> {}
        }
    }

    public void removeRateByCategory(Category category) {
        if (category == null || category.getName() == null) return;
        switch (category.getName()) {
            case "A" -> this.standardRate = null;
            case "B" -> this.reducedRate = null;
            case "C" -> this.superReducedRate = null;
            case "D" -> this.noneRate = null;
            case "E" -> this.otherRate = null;
            default -> {}
        }
    }



    @Override
    public String toString() {
        return "TaxRate{" +
                "otherRate=" + otherRate +
                ", noneRate=" + noneRate +
                ", superReducedRate=" + superReducedRate +
                ", reducedRate=" + reducedRate +
                ", standardRate=" + standardRate +
                ", country=" + country +
                ", id=" + id +
                '}';
    }
}
