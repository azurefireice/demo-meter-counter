package edu.demo.meter.counter.report;

import edu.demo.meter.counter.report.models.VillageReport;

import java.util.LinkedList;
import java.util.List;

public class CounterReportResponse {
    private List<VillageReport> villages = new LinkedList<>();

    public CounterReportResponse(List<VillageReport> villages) {
        this.villages = villages;
    }

    public List<VillageReport> getVillages() {
        return villages;
    }

    @Override
    public String toString() {
        return "CounterReportResponse{" +
                "villages=" + villages +
                '}';
    }
}
