package vortex.imwp.Mappers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vortex.imwp.DTOs.ReportDTO;
import vortex.imwp.Models.Report;

public class ReportDTOMapper {
    public static ReportDTO map(Report report){
        return new ReportDTO(
                report.getType(),
                report.getEmployeeIdCreated(),
                report.getCreatedAtWarehouseID()
        );
    }

    public static Report map(ReportDTO report){
        return new Report(
                report.getType(),
                report.getEmployeeIdCreated(),
                report.getCreatedAtWarehouseID()
        );
    }

    public static Report mapWithData(ReportDTO reportDTO){
        return new Report(
                reportDTO.getType(),
                reportDTO.getEmployeeIdCreated(),
                reportDTO.getCreatedAtWarehouseID(),
                mapJSON(reportDTO.getData())
        );
    }

    public static ReportDTO mapWithData(Report report){
        return new ReportDTO(
                report.getType(),
                report.getEmployeeIdCreated(),
                report.getCreatedAtWarehouseID(),
                mapJSON(report.getData())
        );
    }

    private static JSONObject mapJSON(String data){
        JSONParser parser = new JSONParser();

        try {
            return (JSONObject) parser.parse(data);
        }catch (ParseException e){
            System.out.println("[ERROR] Mapping Report DTO:\n" + e.getMessage());
        }

        return new JSONObject();
    }
}
