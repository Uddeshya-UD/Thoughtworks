package uk.tw.energy.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final Map<String, List<String>> userToSmartMeters;

    public UserService(Map<String, List<String>> userToSmartMeters) {
        this.userToSmartMeters = userToSmartMeters;
    }

    public List<String> getSmartMetersForUser(String userId) {
        return userToSmartMeters.get(userId);
    }
}
