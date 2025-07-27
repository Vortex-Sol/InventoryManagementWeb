package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.DTOs.JobDTO;
import vortex.imwp.Mappers.ItemDTOMapper;
import vortex.imwp.Mappers.JobDTOMapper;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Job;
import vortex.imwp.Repositories.JobRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Optional<List<JobDTO>> getAll(){
        Iterable<Job> list = jobRepository.findAll();
        List<JobDTO> jobs = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Job job : list) jobs.add(JobDTOMapper.map(job));
            return Optional.of(jobs);
        }
        return Optional.empty();
    }

    public Optional<Job> getJobById(Long id) { return jobRepository.findById(id); }

    public Job addJob(JobDTO job) { return jobRepository.save(JobDTOMapper.map(job)); }

    public Job updateJob(Job job) { return jobRepository.save(job); }

    public void deleteJob(Long id) { jobRepository.deleteById(id); }
}
