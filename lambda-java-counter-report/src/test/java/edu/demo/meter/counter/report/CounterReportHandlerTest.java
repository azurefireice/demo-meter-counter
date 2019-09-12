package edu.demo.meter.counter.report;

import edu.demo.meter.counter.report.models.Counter;
import edu.demo.meter.counter.report.models.Village;
import edu.demo.meter.counter.report.models.VillageReport;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CounterReportHandlerTest {


    @Test
    void convertDataToResponse() {
        CounterReportHandler test = new CounterReportHandler();
        CounterReportResponse expected = getExpectedData();
        List<Counter> mockCounterData = getMockCounterData();
        List<Village> mockVillageData = getMockVillageData();
        CounterReportResponse actual = test.convertDataToResponse(mockCounterData, mockVillageData);
        assertEquals(expected.getVillages(), actual.getVillages());
    }

    private List<Counter> getMockCounterData() {
        return Arrays.asList(
                new Counter("1", Instant.now().toEpochMilli(), 10D),
                new Counter("1", Instant.now().toEpochMilli(), 15D),
                new Counter("1", Instant.now().toEpochMilli(), 22D),
                new Counter("2", Instant.now().toEpochMilli(), 2D),
                new Counter("2", Instant.now().toEpochMilli(), 8D),
                new Counter("2", Instant.now().toEpochMilli(), 5D),
                new Counter("3", Instant.now().toEpochMilli(), 10D),
                new Counter("3", Instant.now().toEpochMilli(), 100D),
                new Counter("3", Instant.now().toEpochMilli(), 130D),
                new Counter("3", Instant.now().toEpochMilli(), 180D)
        );
    }

    private List<Village> getMockVillageData() {
        return Arrays.asList(
                new Village("2", "Villabajo"),
                new Village("3", "Arroyo")
        );
    }

    private CounterReportResponse getExpectedData() {
        List<VillageReport> villages = Arrays.asList(
                new VillageReport("1", 47D),
                new VillageReport("Villabajo", 15D),
                new VillageReport("Arroyo", 420D)
        );
        return new CounterReportResponse(villages);
    }

}