package com.gds.challenge26.batch;

import com.gds.challenge26.model.User;
import com.gds.challenge26.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.springframework.batch.item.Chunk;

import java.util.List;

class UserItemWriterTest {

    @Test
    void testWriteCallsSaveAll() {
        // Arrange
        UserRepository repo = mock(UserRepository.class);
        UserItemWriter writer = new UserItemWriter(repo);

        User u1 = new User("John");
        User u2 = new User("David");

        Chunk<User> chunk = new Chunk<>(List.of(u1, u2));

        // Act
        writer.write(chunk);

        // Assert
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<User>> captor = (ArgumentCaptor<List<User>>) (ArgumentCaptor<?>) ArgumentCaptor.forClass(List.class);
        verify(repo, times(1)).saveAll(captor.capture());

        List<User> savedUsers = captor.getValue();

        assertThat(savedUsers).hasSize(2);
        assertThat(savedUsers.get(0).getName()).isEqualTo("John");
        assertThat(savedUsers.get(1).getName()).isEqualTo("David");
    }

    @Test
    void testWriteEmptyChunkDoesNotCallSaveAll() {
        UserRepository repo = mock(UserRepository.class);
        UserItemWriter writer = new UserItemWriter(repo);

        Chunk<User> emptyChunk = new Chunk<>(List.of());

        writer.write(emptyChunk);

        verify(repo, never()).saveAll(any());
    }
}
