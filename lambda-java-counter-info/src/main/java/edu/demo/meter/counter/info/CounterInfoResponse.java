package edu.demo.meter.counter.info;

public class CounterInfoResponse {
    private String id;
    private String village_name;

    public CounterInfoResponse(String id, String village_name) {
        this.id = id;
        this.village_name = village_name;
    }

    public CounterInfoResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }
}
