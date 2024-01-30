package uk.tw.energy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.energy.domain.User;
import uk.tw.energy.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/smart-meters")
    public ResponseEntity<List<String>> getSmartMetersForUser(@PathVariable String userId) {
        logger.info("Received request to get smart meters for user with ID: {}", userId);
        List<String> smartMeters = userService.getSmartMetersForUser(userId);

        if (smartMeters != null) {
            logger.info("Smart meters found for user with ID {}: {}", userId, smartMeters);
            return new ResponseEntity<>(smartMeters, HttpStatus.OK);
        } else {
            logger.warn("No smart meters found for user with ID: {}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUserWithMeters(@RequestBody User user) {
        try {
            logger.info("Received request to save user with meters: {}", user);
            userService.saveUserWithMeters(user.getUserId(), user.getSmartMeterId());
            logger.info("User and meters saved successfully for user with ID: {}", user.getUserId());
            return ResponseEntity.ok("User and meters saved successfully");
        } catch (Exception e) {
            logger.error("Failed to save user and meters. Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save user and meters");
        }
    }
}
