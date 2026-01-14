package com.gds.challenge26.batch;

import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import org.springframework.batch.item.ItemProcessor;

/**
 * Spring Batch {@link ItemProcessor} implementation that transforms
 * {@link UserDto} objects into {@link User} entities.
 * <p>
 * This processor is typically used in a batch step to convert
 * input records read from a CSV file into domain objects
 * suitable for persistence.
 */
public class UserItemProcessor implements ItemProcessor<UserDto, User> {

    /**
     * Processes a {@link UserDto} and converts it into a {@link User}.
     *
     * @param dto the input data transfer object
     * @return a new {@link User} instance created from the DTO
     */
    @Override
    public User process(UserDto dto){
        return new User(dto.getName());
    }
}
