package edu.demo.meter.counter.info.exceptions;

public class CounterInfoNotFoundException extends RuntimeException {
    public CounterInfoNotFoundException() {
        super("Item not found");
    }
}
