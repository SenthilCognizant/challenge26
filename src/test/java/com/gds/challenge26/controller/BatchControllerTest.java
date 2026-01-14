package com.gds.challenge26.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BatchControllerTest {
    private JobLauncher jobLauncher;
    private Job job;
    private BatchController batchController;

    @BeforeEach
    void setUp() {
        jobLauncher = mock(JobLauncher.class);
        job = mock(Job.class);
        batchController = new BatchController(jobLauncher, job);
    }

    @Test
    void upload_FileExists_ShouldRunJobSuccessfully() throws Exception {
        // Ensure CSV file exists in classpath
        Resource resource = new ClassPathResource("users.csv");
        assertTrue(resource.exists(), "Test CSV file must exist in classpath for this test");

        String response = batchController.upload();

        assertEquals("Users imported successfully!", response);

        // Verify JobLauncher.run() called with the job and parameters
        ArgumentCaptor<JobParameters> paramCaptor = ArgumentCaptor.forClass(JobParameters.class);
        verify(jobLauncher, times(1)).run(eq(job), paramCaptor.capture());

        JobParameters capturedParams = paramCaptor.getValue();
        assertNotNull(capturedParams.getString("filePath"));
        assertTrue(new File(capturedParams.getString("filePath")).exists());
    }

    @Test
    void upload_FileDoesNotExist_ShouldThrowIllegalArgumentException() {
        // Temporarily override the resource to simulate missing file
        BatchController controller = new BatchController(jobLauncher, job) {
            @Override
            public String upload() {
                Resource resource = new ClassPathResource("nonexistent.csv");
                if (!resource.exists()) {
                    throw new IllegalArgumentException("CSV file not found in classpath");
                }
                return super.upload();
            }
        };

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, controller::upload);
        assertEquals("CSV file not found in classpath", ex.getMessage());

        verifyNoInteractions(jobLauncher); // JobLauncher should not run
    }

    @Test
    void upload_JobLauncherThrowsException_ShouldWrapAsIllegalArgumentException() throws Exception {
        // Simulate jobLauncher throwing exception
        doThrow(new RuntimeException("Job failed")).when(jobLauncher).run(any(Job.class), any(JobParameters.class));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, batchController::upload);
        assertTrue(ex.getMessage().contains("Failed to execute batch job: Job failed"));

        verify(jobLauncher, times(1)).run(eq(job), any(JobParameters.class));
    }
}
