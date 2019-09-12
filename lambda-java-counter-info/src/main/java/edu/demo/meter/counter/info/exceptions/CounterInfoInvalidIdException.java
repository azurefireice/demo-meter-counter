package edu.demo.meter.counter.info.exceptions;

public class CounterInfoInvalidIdException extends RuntimeException {
    public CounterInfoInvalidIdException() {
        super("Counter id is invalid or empty");
    }
}
