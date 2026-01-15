package com.gds.challenge26.batch;

import com.gds.challenge26.model.User;
import com.gds.challenge26.repository.UserRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * Spring Batch {@link ItemWriter} implementation responsible for
 * persisting {@link User} entities.
 * <p>
 * This writer saves processed user records to the database
 * using {@link UserRepository} in a batch-oriented manner.
 */
public record UserItemWriter(UserRepository userRepository) implements ItemWriter<User> {

    /**
     * Writes a chunk of {@link User} entities to the data store.
     * <p>
     * This method is invoked by Spring Batch during chunk processing.
     *
     * @param chunk the chunk containing user entities to be persisted
     */
    @Override
    public void write(Chunk<? extends User> chunk) {
        List<? extends User> users = chunk.getItems();
        if (users.isEmpty())
            return;
        userRepository.saveAll(users);
    }
}
