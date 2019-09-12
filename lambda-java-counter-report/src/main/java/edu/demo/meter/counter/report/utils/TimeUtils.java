package edu.demo.meter.counter.report.utils;

import edu.demo.meter.counter.report.exceptions.CounterReportException;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.time.temporal.TemporalAmount;

public class TimeUtils {

    public static Long subtractIntervalFromInstant(String interval, Instant instant) {
        Instant result;
        try {
            TemporalAmount amount = parse(interval);
            result = instant.minus(amount);
        } catch (DateTimeException dte) {
            String msg = "Error while parsing and converting interval \"" + interval + "\"";
            throw new CounterReportException(msg, dte);
        }

        return result.toEpochMilli();
    }

    private static TemporalAmount parse(String value) {
        if (Character.isUpperCase(value.charAt(value.length() - 1))) {
            return Period.parse("P" + value);
        } else {
            return Duration.parse("PT" + value);
        }
    }
}
