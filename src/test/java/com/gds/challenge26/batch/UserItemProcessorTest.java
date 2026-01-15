package com.gds.challenge26.batch;

import com.gds.challenge26.model.User;
import com.gds.challenge26.model.UserDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserItemProcessorTest {

    @Test
    void testProcess()  {
        // Arrange
        UserItemProcessor processor = new UserItemProcessor();
        UserDto dto = new UserDto("John");

        // Act
        User result = processor.process(dto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John");
    }

    @Test
    void testProcessWithNullName(){
        UserItemProcessor processor = new UserItemProcessor();
        UserDto dto = new UserDto(null);

        User result = processor.process(dto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isNull();
    }
}
