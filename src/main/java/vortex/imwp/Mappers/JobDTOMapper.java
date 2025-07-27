package vortex.imwp.Mappers;

import vortex.imwp.DTOs.JobDTO;
import vortex.imwp.Models.Job;

public class JobDTOMapper {
    public static JobDTO map(Job job) {
        return new JobDTO(
                job.getName(),
                job.getDescription()
        );
    }

    public static Job map(JobDTO job) {
        return new Job(
                job.getName(),
                job.getDescription()
        );
    }
}
