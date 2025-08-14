package vortex.imwp.mappers;

import vortex.imwp.dtos.JobDTO;
import vortex.imwp.models.Job;

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
