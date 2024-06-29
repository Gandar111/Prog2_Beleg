package contract;

import java.math.BigDecimal;
import java.time.Duration;

public interface Uploadable {
    Uploader getUploader();  //Gibt die Dauer des Audio-Mediums oder Video zurück
    Duration getAvailability();
    BigDecimal getCost();
}
