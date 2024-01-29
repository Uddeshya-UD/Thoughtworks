package uk.tw.energy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import uk.tw.energy.domain.User;
import uk.tw.energy.generator.ElectricityReadingsGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import static java.util.Collections.emptyList;
/**
 * Configuration class for seeding application data.
 */

@Service
public class SeedingApplicationDataConfiguration {

    private final ElectricityReadingsGenerator electricityReadingsGenerator;

    @Autowired
    public SeedingApplicationDataConfiguration(ElectricityReadingsGenerator electricityReadingsGenerator) {
        this.electricityReadingsGenerator = electricityReadingsGenerator;
    }

    // === 1 ===

    // ADDING THESE Constants AS ENUMS for better maintainability and readability.

    // private static final String MOST_EVIL_PRICE_PLAN_ID = "price-plan-0";
    // private static final String RENEWABLES_PRICE_PLAN_ID = "price-plan-1";
    // private static final String STANDARD_PRICE_PLAN_ID = "price-plan-2";

    /*
     *  @Bean
    public List<PricePlan> pricePlans() {
        final List<PricePlan> pricePlans = new ArrayList<>();
        pricePlans.add(new PricePlan(MOST_EVIL_PRICE_PLAN_ID, "Dr Evil's Dark Energy", BigDecimal.TEN, emptyList()));
        pricePlans.add(new PricePlan(RENEWABLES_PRICE_PLAN_ID, "The Green Eco", BigDecimal.valueOf(2), emptyList()));
        pricePlans.add(new PricePlan(STANDARD_PRICE_PLAN_ID, "Power for Everyone", BigDecimal.ONE, emptyList()));
        return pricePlans;
    }

     */

    public enum PricePlanId {
        MOST_EVIL("price-plan-0"),
        RENEWABLES("price-plan-1"),
        STANDARD("price-plan-2");
    
        private final String id;
    
        PricePlanId(String id) {
            this.id = id;
        }
    
        public String getId() {
            return id;
        }
    }
    

    @Bean
    public List<PricePlan> pricePlans() {
        return List.of(
            new PricePlan(PricePlanId.MOST_EVIL.getId(), "Dr Evil's Dark Energy", BigDecimal.TEN, emptyList()),
            new PricePlan(PricePlanId.RENEWABLES.getId(), "The Green Eco", BigDecimal.valueOf(2), emptyList()),
            new PricePlan(PricePlanId.STANDARD.getId(), "Power for Everyone", BigDecimal.ONE, emptyList())
    );
    }


    // === 2 ===

    // ISSUE - REMOVING DUPLICATES
        /*
         * Deleting the creation of new instance of ElectricityReadingsGenerator as we have already injected it through the constructor. 
         */


    // @Bean
    // public Map<String, List<ElectricityReading>> perMeterElectricityReadings() {
    //     final Map<String, List<ElectricityReading>> readings = new HashMap<>();
    //     final ElectricityReadingsGenerator electricityReadingsGenerator = new ElectricityReadingsGenerator();
    //     smartMeterToPricePlanAccounts()
    //             .keySet()
    //             .forEach(smartMeterId -> readings.put(smartMeterId, electricityReadingsGenerator.generate(20)));
    //     return readings;
    // }



     /**
     * Bean method to create a map of smart meter IDs to lists of electricity readings.
     *
     * @return Map of smart meter IDs to lists of electricity readings.
     */

    @Bean
    public Map<String, List<ElectricityReading>> perMeterElectricityReadings() {
        final Map<String, List<ElectricityReading>> readings = new HashMap<>();
        smartMeterToPricePlanAccounts().keySet().forEach(smartMeterId -> readings.put(smartMeterId, electricityReadingsGenerator.generate(20)));
        return readings;
}

@Bean
public Map<String, List<String>> getUserDetails() {
    final Map<String, List<String>> userSmartMeterDetails = new HashMap<>();

    SmartMeterToUserAccounts().forEach((user, meters) -> {
        // Add the user to the map with the list of smart meters
        userSmartMeterDetails.put(user, meters);
    });

    return userSmartMeterDetails;
}



    // === 3 ===

    // Smaller syntax

    // @Bean
    // public Map<String, String> smartMeterToPricePlanAccounts() {
    //     final Map<String, String> smartMeterToPricePlanAccounts = new HashMap<>();
    //     smartMeterToPricePlanAccounts.put("smart-meter-0", MOST_EVIL_PRICE_PLAN_ID);
    //     smartMeterToPricePlanAccounts.put("smart-meter-1", RENEWABLES_PRICE_PLAN_ID);
    //     smartMeterToPricePlanAccounts.put("smart-meter-2", MOST_EVIL_PRICE_PLAN_ID);
    //     smartMeterToPricePlanAccounts.put("smart-meter-3", STANDARD_PRICE_PLAN_ID);
    //     smartMeterToPricePlanAccounts.put("smart-meter-4", RENEWABLES_PRICE_PLAN_ID);
    //     return smartMeterToPricePlanAccounts;
    // }



    /**
     * Bean method to create a map of smart meter IDs to corresponding price plan IDs.
     *
     * @return Map of smart meter IDs to price plan IDs.
     */
    
    @Bean
    public Map<String, String> smartMeterToPricePlanAccounts() {
        return Map.of(
            "smart-meter-0", PricePlanId.MOST_EVIL.getId(),
            "smart-meter-1", PricePlanId.RENEWABLES.getId(),
            "smart-meter-2", PricePlanId.MOST_EVIL.getId(),
            "smart-meter-3", PricePlanId.STANDARD.getId(),
            "smart-meter-4", PricePlanId.RENEWABLES.getId()
        );
    }

    
    public Map<String, List<String>> SmartMeterToUserAccounts() {
        return Map.of(
                "Sarah", Arrays.asList("smart-meter-0","smart-meter-0.2"),
                "Peter", Arrays.asList("smart-meter-1"),
                "Charlie", Arrays.asList("smart-meter-2"),
                "Andrea", Arrays.asList("smart-meter-3"),
                "Alex", Arrays.asList("smart-meter-4")
        );
    }




    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }
}
