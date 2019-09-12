package edu.demo.meter.counter.info;

public class CounterInfoRequest {
    private String counterId;

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    @Override
    public String toString() {
        return "CounterInfoRequest{" +
                "counterId='" + counterId + '\'' +
                '}';
    }
}
