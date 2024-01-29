package uk.tw.energy.domain;
import java.util.List;
import java.math.BigDecimal;

// Added User model as it was present in the Git Documentation
public record User(String userId ,List<String> smartMeterId) {

}
