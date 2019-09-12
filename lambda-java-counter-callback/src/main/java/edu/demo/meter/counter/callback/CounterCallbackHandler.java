package edu.demo.meter.counter.callback;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class CounterCallbackHandler implements RequestHandler<CounterCallbackPayload, String> {
    private static final Logger LOGGER = Logger.getLogger(CounterCallbackHandler.class.getName());

    private static final int DEFAULT_TIMEOUT = 11000;
    private static final String COUNTERS_TABLE_NAME = System.getenv("COUNTERS_TABLE_NAME");

    private static ClientConfiguration clientConfiguration = new ClientConfiguration()
            .withConnectionTimeout(DEFAULT_TIMEOUT)
            .withClientExecutionTimeout(DEFAULT_TIMEOUT)
            .withRequestTimeout(DEFAULT_TIMEOUT)
            .withSocketTimeout(DEFAULT_TIMEOUT)
            .withRetryPolicy(PredefinedRetryPolicies.getDynamoDBDefaultRetryPolicyWithCustomMaxRetries(3));

    private static AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard()
            .withRegion(System.getenv("DDB_REGION"))
            .withClientConfiguration(clientConfiguration)
            .build();

    public String handleRequest(CounterCallbackPayload payload, Context context) {
        LOGGER.info("Counter payload: " + payload);
        if (payload == null || payload.getCounter_id() == null || payload.getAmount() == null)
            throw new RuntimeException("Counter payload is invalid");
        PutItemResult result = persistData(payload);
        LOGGER.info("Put item result: " + result);
        return "success";
    }

    private PutItemResult persistData(CounterCallbackPayload payload) throws AmazonServiceException {
        Map<String, AttributeValue> item_values = new HashMap<>();
        item_values.put("counter_id", new AttributeValue(payload.getCounter_id()));
        item_values.put("amount", new AttributeValue().withN(payload.getAmount().toString()));
        item_values.put("timestamp", new AttributeValue().withN(getMillisTimestamp()));
        LOGGER.info("Putting record to dynamoDB: " + item_values);
        return dynamoDb.putItem(COUNTERS_TABLE_NAME, item_values);
    }

    private String getMillisTimestamp() {
        return String.valueOf(Instant.now().toEpochMilli());
    }
}