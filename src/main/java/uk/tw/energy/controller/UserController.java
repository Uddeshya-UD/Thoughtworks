package uk.tw.energy.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.energy.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}/smart-meters")
    public ResponseEntity<List<String>> getSmartMetersForUser(@PathVariable String userId) {
        List<String> smartMeters = userService.getSmartMetersForUser(userId);

        if (smartMeters != null) {
            return new ResponseEntity<>(smartMeters, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
