package edu.demo.meter.counter.callback;

public class CounterCallbackPayload {
    private String counter_id;
    private Double amount; // It might make sense to use BigDecimal, but as current task states - no need.

    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "edu.demo.meter.CounterPayload{" +
                "counter_id='" + counter_id + '\'' +
                ", amount=" + amount +
                '}';
    }
}
