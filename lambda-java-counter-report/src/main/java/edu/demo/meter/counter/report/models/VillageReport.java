package edu.demo.meter.counter.report.models;

import java.util.Objects;

public class VillageReport {
    private String village_name;
    private Double consumption; // It might make sense to use BigDecimal, but as current task states - no need.

    public VillageReport() {
    }

    public VillageReport(String village_name, Double consumption) {
        this.village_name = village_name;
        this.consumption = consumption;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VillageReport that = (VillageReport) o;
        return Objects.equals(getVillage_name(), that.getVillage_name()) &&
                Objects.equals(getConsumption(), that.getConsumption());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVillage_name(), getConsumption());
    }

    @Override
    public String toString() {
        return "VillageReport{" +
                "village_name='" + village_name + '\'' +
                ", consumption=" + consumption +
                '}';
    }

}
