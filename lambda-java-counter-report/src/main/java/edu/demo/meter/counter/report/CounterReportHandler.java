package edu.demo.meter.counter.report;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import edu.demo.meter.counter.report.models.Counter;
import edu.demo.meter.counter.report.models.Village;
import edu.demo.meter.counter.report.models.VillageReport;
import edu.demo.meter.counter.report.utils.TimeUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class CounterReportHandler implements RequestHandler<CounterReportRequest, CounterReportResponse> {
    private static final Logger LOGGER = Logger.getLogger(CounterReportHandler.class.getName());

    private static final int DEFAULT_TIMEOUT = 11000;
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

    private static DynamoDBMapper mapper = new DynamoDBMapper(dynamoDb);

    public CounterReportResponse handleRequest(CounterReportRequest request, Context context) {
        LOGGER.info("Request: " + request);
        if (request == null || request.getDuration() == null || request.getDuration().isEmpty())
            throw new RuntimeException("Duration is invalid or empty");

        String duration = request.getDuration();
        Long fromTS = TimeUtils.subtractIntervalFromInstant(duration, Instant.now());
        List<Counter> counters = queryCounterData(fromTS);
        List<Village> villages = queryVillageData();

        CounterReportResponse response = convertDataToResponse(counters, villages);
        LOGGER.info("Counter report result: " + response);
        return response;
    }

    protected List<Counter> queryCounterData(Long startingFromTS) throws AmazonServiceException {
        Condition rangeCondition = new Condition()
                .withComparisonOperator(ComparisonOperator.GE)
                .withAttributeValueList(new AttributeValue().withN("" + startingFromTS));

        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterConditionEntry("timestamp", rangeCondition);

        List<Counter> results = mapper.scan(Counter.class, scanExpression);

        LOGGER.info("Requesting data from dynamoDB: " + results);
        return results;
    }

    protected List<Village> queryVillageData() throws AmazonServiceException {
        final DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        List<Village> results = mapper.scan(Village.class, scanExpression);

        LOGGER.info("Requesting data from dynamoDB: " + results);
        return results;
    }

    protected CounterReportResponse convertDataToResponse(List<Counter> counters, List<Village> villages) {
        Map<String, String> villagesNameMap = villages.stream().collect(Collectors.toMap(Village::getId, Village::getVillage_name));
        List<VillageReport> villageReports = counters.stream()
                .collect(Collectors.groupingBy(
                        Counter::getCounter_id,
                        Collectors.reducing(0D, Counter::getAmount, Double::sum))
                ).entrySet().stream()
                .map(e -> new VillageReport(
                        getVillageNameById(e.getKey(), villagesNameMap),
                        e.getValue())
                ).collect(Collectors.toList());
        LOGGER.info("Village reports: " + villageReports);
        return new CounterReportResponse(villageReports);
    }

    private String getVillageNameById(String id, Map<String, String> villagesMap) {
        if (villagesMap.containsKey(id))
            return villagesMap.get(id);
        return id;
    }
}