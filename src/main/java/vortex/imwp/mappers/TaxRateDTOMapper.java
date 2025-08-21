package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.dtos.TaxRateDTO;
import vortex.imwp.models.Country;
import vortex.imwp.models.TaxRate;

public class TaxRateDTOMapper {
    public static TaxRateDTO map(TaxRate taxRate) {
        return new TaxRateDTO(
                taxRate.getId(),
                taxRate.getCountry().name(),
                taxRate.getStandardRate(),
                taxRate.getReducedRate(),
                taxRate.getSuperReducedRate(),
                taxRate.getNoneRate(),
                taxRate.getOtherRate()

        );
    }
    public static TaxRate map(TaxRateDTO taxRateDTO) {
        return new TaxRate(
                taxRateDTO.getId(),
                Country.fromString(taxRateDTO.getCountry()),
                taxRateDTO.getStandardRate(),
                taxRateDTO.getReducedRate(),
                taxRateDTO.getSuperReducedRate(),
                taxRateDTO.getNoneRate(),
                taxRateDTO.getOtherRate()
        );
    }
}
