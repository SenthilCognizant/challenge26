package com.gds.challenge26.controller;

import com.gds.challenge26.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/batch")
@Tag(name = "Batch APIs")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job job;

    public BatchController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Operation(summary = "Upload user CSV file and start batch import")
    @PostMapping("/upload")
    public String upload() {
        try {
            Resource resource = new ClassPathResource("users.csv");
            if (!resource.exists()) {
                throw new IllegalArgumentException("CSV file not found in classpath");
            }

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", resource.getFile().getAbsolutePath())
                    .addLong("run.id", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, params);
            return "Users imported successfully!";
        } catch (IllegalArgumentException ex) {
            throw ex; // will return 400
        } catch (Exception ex) {
            throw new IllegalArgumentException("Failed to execute batch job: " + ex.getMessage(), ex);
        }
    }
}
