package uk.tw.energy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.service.MeterReadingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/readings")
public class MeterReadingController {

    private static final Logger log = LoggerFactory.getLogger(MeterReadingController.class);

    @Autowired
    private final MeterReadingService meterReadingService;

    public MeterReadingController(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    
    @PostMapping("/store")
    public ResponseEntity<?> storeReadings(@RequestBody MeterReadings meterReadings) {
        log.info("Received request to store readings for smart meter ID: {}", meterReadings.smartMeterId());

        if (!isMeterReadingsValid(meterReadings)) {
            log.warn("Invalid readings received for smart meter ID: {}", meterReadings.smartMeterId());
            // Return BAD_REQUEST when the readings are not valid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

            /*  ISSUE - FIRST

             when the readings are not valid. It might be more appropriate to use HttpStatus.BAD_REQUEST 
             to indicate that the client's request is not valid
             Internal server error is usually reserved for issues on the server side
             
             dont USE : - 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();


             */
        }

        try {
            meterReadingService.storeReadings(meterReadings.smartMeterId(), meterReadings.electricityReadings());
            log.info("Readings stored successfully for smart meter ID: {}", meterReadings.smartMeterId());
            // Return OK when readings are successfully stored
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Failed to store readings for smart meter ID: {}. Error: {}", meterReadings.smartMeterId(), e.getMessage());
            // Return INTERNAL_SERVER_ERROR if an exception occurs during storage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isMeterReadingsValid(MeterReadings meterReadings) {
        String smartMeterId = meterReadings.smartMeterId();
        List<ElectricityReading> electricityReadings = meterReadings.electricityReadings();
        return smartMeterId != null && !smartMeterId.isEmpty()
                && electricityReadings != null && !electricityReadings.isEmpty();
    }

    @GetMapping("/read/{smartMeterId}")
    public ResponseEntity<List<ElectricityReading>> readReadings(@PathVariable String smartMeterId) {
        log.info("Received request to read readings for smart meter ID: {}", smartMeterId);

        Optional<List<ElectricityReading>> readings = meterReadingService.getReadings(smartMeterId);
        return readings.isPresent()
                ? ResponseEntity.ok(readings.get())
                : ResponseEntity.notFound().build();
    }
}
