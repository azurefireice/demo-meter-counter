package edu.demo.meter.counter.report.exceptions;

public class CounterReportException extends RuntimeException {

    public CounterReportException() {
    }

    public CounterReportException(String message) {
        super(message);
    }

    public CounterReportException(String message, Throwable cause) {
        super(message, cause);
    }

    public CounterReportException(Throwable cause) {
        super(cause);
    }

    public CounterReportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
