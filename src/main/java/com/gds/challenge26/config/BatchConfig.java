package com.gds.challenge26.config;

import com.gds.challenge26.batch.UserItemProcessor;
import com.gds.challenge26.batch.UserItemWriter;
import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import com.gds.challenge26.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Spring Batch configuration class responsible for defining
 * job, step, reader, processor, and writer beans.
 * <p>
 * This configuration imports user data from a CSV file and
 * persists it using Spring Batch chunk-oriented processing.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    /**
     * Creates a {@link FlatFileItemReader} for reading {@link UserDto}
     * records from a CSV file.
     * <p>
     * The file path is supplied via job parameters and may reference
     * either a classpath or filesystem resource.
     *
     * @param filePath the CSV file path provided as a job parameter
     * @return a configured {@link FlatFileItemReader}
     */
    @Bean
    @StepScope
    public FlatFileItemReader<UserDto> userItemReader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        Resource resource;
        if(filePath.startsWith("classpath:")) {
            resource = new ClassPathResource(filePath.substring(10)); // remove "classpath:"
        } else {
            resource = new FileSystemResource(filePath);
        }

        FlatFileItemReader<UserDto> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(0); // your CSV has no header

        DefaultLineMapper<UserDto> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("name");

        BeanWrapperFieldSetMapper<UserDto> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(UserDto.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        reader.setLineMapper(lineMapper);

        return reader;
    }


    /**
     * Creates the {@link UserItemProcessor} responsible for transforming
     * {@link UserDto} objects into {@link User} entities.
     *
     * @return a new {@link UserItemProcessor}
     */
    @Bean
    public UserItemProcessor userItemProcessor() {
        return new UserItemProcessor();
    }

    /**
     * Creates the {@link UserItemWriter} responsible for persisting
     * {@link User} entities to the database.
     *
     * @param repository the user repository
     * @return a configured {@link UserItemWriter}
     */
    @Bean
    public UserItemWriter userItemWriter(UserRepository repository) {
        return new UserItemWriter(repository);
    }

    /**
     * Defines the batch {@link Step} used to process user records.
     * <p>
     * The step uses chunk-oriented processing with a chunk size of 10.
     *
     * @param jobRepository the job repository
     * @param txManager the transaction manager
     * @param userItemReader the item reader
     * @param processor the item processor
     * @param writer the item writer
     * @return a configured {@link Step}
     */
    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager txManager,
                     FlatFileItemReader<UserDto> userItemReader,
                     UserItemProcessor processor,
                     UserItemWriter writer) {

        return new StepBuilder("step", jobRepository)
                .<UserDto, User>chunk(10, txManager)
                .reader(userItemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    /**
     * Defines the Spring Batch {@link Job} that imports users from a CSV file.
     *
     * @param jobRepository the job repository
     * @param step the step responsible for processing users
     * @return a configured {@link Job}
     */
    @Bean
    public Job importUsersJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("importUsersJob", jobRepository)
                .start(step)
                .build();
    }

}
