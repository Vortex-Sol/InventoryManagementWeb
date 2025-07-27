package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.ReportDTO;
import vortex.imwp.Mappers.ReportDTOMapper;
import vortex.imwp.Models.Report;
import vortex.imwp.Repositories.ReportRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Optional<List<ReportDTO>> getAll() {
        Iterable<Report> list = reportRepository.findAll();
        List<ReportDTO> reports = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Report report : list) reports.add(ReportDTOMapper.map(report));
            return Optional.of(reports);
        }
        return Optional.empty();
    }

    public Optional<Report> getReportById(Long id) { return reportRepository.findById(id); }

    public Report addReport(ReportDTO report) { return reportRepository.save(ReportDTOMapper.map(report)); }

    public Report updateReport(Report report) { return reportRepository.save(report); }

    public void deleteReport(Long id) { reportRepository.deleteById(id); }
}
