package edu.demo.meter.counter.report.utils;

import edu.demo.meter.counter.report.exceptions.CounterReportException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TimeUtilsTest {

    @Test
    void testParse() {
        String[] timeAmountStrings = {"1m", "5M", "3D", "30m", "2h", "1Y", "3W"};
        Instant now = Instant.now();
        Long expected = now.minus(Duration.ofHours(24)).toEpochMilli();
        Long actual = TimeUtils.subtractIntervalFromInstant("24h", now);
        assertEquals(expected, actual);

        expected = now.minus(Duration.ofMinutes(5)).toEpochMilli();
        actual = TimeUtils.subtractIntervalFromInstant("5m", now);
        assertEquals(expected, actual);

        expected = now.minus(Duration.ofMinutes(0)).toEpochMilli();
        actual = TimeUtils.subtractIntervalFromInstant("0m", now);
        assertEquals(expected, actual);

        expected = now.minus(Duration.ofDays(7)).toEpochMilli();
        actual = TimeUtils.subtractIntervalFromInstant("7D", now);
        assertEquals(expected, actual);
    }

    @Test
    void testParseWithException() {
        String[] timeAmountStrings = {"1m", "5M", "3D", "30m", "2h", "1Y", "3W"};
        Instant now = Instant.now();
        assertThrows(CounterReportException.class, () -> {
            TimeUtils.subtractIntervalFromInstant("1d", now);
            ;
        });
        assertThrows(CounterReportException.class, () -> {
            TimeUtils.subtractIntervalFromInstant("1y", now);
            ;
        });
        //Test whether 13-th Black Crusade date is invalid.
        assertThrows(CounterReportException.class, () -> {
            TimeUtils.subtractIntervalFromInstant("-41999Y", now);
        });
    }
}