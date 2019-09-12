package edu.demo.meter.counter.report;

public class CounterReportRequest {
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CounterReportRequest{" +
                "duration='" + duration + '\'' +
                '}';
    }
}
