package uk.tw.energy.domain;

import java.util.List;

public class User {
    private final String userId;
    private final List<String> smartMeterId;

    public User(String userId, List<String> smartMeterId) {
        this.userId = userId;
        this.smartMeterId = smartMeterId;
    }

    public User(String userId) {
        this.userId = userId;
        this.smartMeterId = List.of(); // or new ArrayList<>() if you want a mutable list
    }

    // Getter for userId
    public String getUserId() {
        return userId;
    }

    // Getter for smartMeterId
    public List<String> getSmartMeterId() {
        return smartMeterId;
    }
}
