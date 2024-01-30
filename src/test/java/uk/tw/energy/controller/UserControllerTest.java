package uk.tw.energy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.tw.energy.domain.User;
import uk.tw.energy.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

        private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);


    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSmartMetersForUser_ValidUserId_ReturnsSmartMeters() {
        // Arrange
        String userId = "testUser";
        List<String> expectedSmartMeters = Arrays.asList("smartMeter1", "smartMeter2");

        when(userService.getSmartMetersForUser(userId)).thenReturn(expectedSmartMeters);

        // Act
        ResponseEntity<List<String>> responseEntity = userController.getSmartMetersForUser(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSmartMeters, responseEntity.getBody());
    }

    @Test
    void getSmartMetersForUser_InvalidUserId_ReturnsNotFound() {
        // Arrange
        String userId = "nonExistentUser";

        when(userService.getSmartMetersForUser(userId)).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<String>> responseEntity = userController.getSmartMetersForUser(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void saveUserWithMeters_ValidUser_ReturnsOk() {
        // Arrange
        User user = new User("testUser", Arrays.asList("smartMeter1", "smartMeter2"));

        // Act
        ResponseEntity<String> responseEntity = userController.saveUserWithMeters(user);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User and meters saved successfully", responseEntity.getBody());

        // Verify that the service method was called
        verify(userService, times(1)).saveUserWithMeters(user.getUserId(), user.getSmartMeterId());
    }

    @Test
    void saveUserWithMeters_ExceptionThrown_ReturnsInternalServerError() {
        // Arrange
        User user = new User("testUser", Arrays.asList("smartMeter1", "smartMeter2"));

        // Mocking the service method to throw an exception
        doThrow(new RuntimeException("Mocked exception")).when(userService).saveUserWithMeters(anyString(), anyList());

        // Act
        ResponseEntity<String> responseEntity = userController.saveUserWithMeters(user);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to save user and meters", responseEntity.getBody());

        // Verify that the service method was called
        verify(userService, times(1)).saveUserWithMeters(user.getUserId(), user.getSmartMeterId());
    }
}
