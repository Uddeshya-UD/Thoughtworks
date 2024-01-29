package uk.tw.energy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import uk.tw.energy.App;
import uk.tw.energy.domain.ElectricityReading;
import java.util.stream.Collectors;

import java.util.*;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Map<String, List<String>> userToSmartMeters;

    @PostConstruct
    public void seedData() {
        SmartMeterToUserAccounts().forEach((userId, meters) -> this.userToSmartMeters.put(userId, meters));
        log.info("Seeded User Data");
    }

    public List<String> getSmartMetersForUser(String userId) {
        return userToSmartMeters.getOrDefault(userId, new ArrayList<>());
    }

    public Map<String, List<String>> SmartMeterToUserAccounts() {
        return Map.of(
                "Sarah", Arrays.asList("smart-meter-0", "smart-meter-0.2"),
                "Peter", Arrays.asList("smart-meter-1"),
                "Charlie", Arrays.asList("smart-meter-2"),
                "Andrea", Arrays.asList("smart-meter-3"),
                "Alex", Arrays.asList("smart-meter-4"));
    }

    public UserService(Map<String, List<String>> userToSmartMeters) {
        this.userToSmartMeters = userToSmartMeters;

    }

    public void saveUserWithMeters(String userId, List<String> newMeters) {
        try {
            List<String> existingMeters = getSmartMetersForUser(userId);
            existingMeters = new ArrayList<>(existingMeters);

            // Check for duplicates and add only unique new meters
            for (String newMeter : newMeters) {
                if (!existingMeters.contains(newMeter)) {
                    existingMeters.add(newMeter);
                }
            }

            userToSmartMeters.put(userId, existingMeters);

            log.info("List of users: {}", userToSmartMeters);
        } catch (Exception e) {
            log.error("An error occurred while saving user and meters.", e);
            // Handle the exception or rethrow if needed
        }
    }
}
