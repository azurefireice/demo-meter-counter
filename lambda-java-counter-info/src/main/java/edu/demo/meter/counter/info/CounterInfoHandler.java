package edu.demo.meter.counter.info;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.demo.meter.counter.info.exceptions.CounterInfoInvalidIdException;
import edu.demo.meter.counter.info.exceptions.CounterInfoNotFoundException;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;


public class CounterInfoHandler implements RequestHandler<Map<String, String>, CounterInfoResponse> {
    private static final Logger LOGGER = Logger.getLogger(CounterInfoHandler.class.getName());

    private static final int DEFAULT_TIMEOUT = 11000;
    private static final String VILLAGES_TABLE_NAME = System.getenv("VILLAGES_TABLE_NAME");
    private static final String REGION = System.getenv("DDB_REGION");

    private static ClientConfiguration clientConfiguration = new ClientConfiguration()
            .withConnectionTimeout(DEFAULT_TIMEOUT)
            .withClientExecutionTimeout(DEFAULT_TIMEOUT)
            .withRequestTimeout(DEFAULT_TIMEOUT)
            .withSocketTimeout(DEFAULT_TIMEOUT)
            .withRetryPolicy(PredefinedRetryPolicies.getDynamoDBDefaultRetryPolicyWithCustomMaxRetries(3));

    private static AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard()
            .withRegion(REGION)
            .withClientConfiguration(clientConfiguration)
            .build();

    public CounterInfoResponse handleRequest(Map<String, String> request, Context context) {
        LOGGER.info("Request: " + request);
        if (request == null || request.get("counterId") == null || request.get("counterId").isEmpty())
            throw new CounterInfoInvalidIdException();
        String counterId = request.get("counterId");
        GetItemResult result = getData(counterId);
        LOGGER.info("Get counter info result: " + result);

        CounterInfoResponse response = prepareResponse(result);
        LOGGER.info("Get counter info response: " + result);
        return response;
    }

    private GetItemResult getData(String counterId) throws AmazonServiceException {
        Map<String, AttributeValue> key = Collections.singletonMap("id", new AttributeValue(counterId));
        LOGGER.info("Requesting key from dynamoDB: " + key);
        return dynamoDb.getItem(VILLAGES_TABLE_NAME, key);
    }

    private CounterInfoResponse prepareResponse(GetItemResult result) throws AmazonServiceException {
        Map<String, AttributeValue> item = result.getItem();
        if (item == null)
            throw new CounterInfoNotFoundException();
        return new CounterInfoResponse(
                item.get("id").getS(),
                item.get("village_name").getS()
        );
    }
}