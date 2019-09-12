package edu.demo.meter.counter.report.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "counters")
public class Counter {
    private String counter_id;
    private Long timestamp;
    private Double amount;

    public Counter() {
    }

    public Counter(String counter_id, Long timestamp, Double amount) {
        this.counter_id = counter_id;
        this.timestamp = timestamp;
        this.amount = amount;
    }

    @DynamoDBHashKey(attributeName = "counter_id")
    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
