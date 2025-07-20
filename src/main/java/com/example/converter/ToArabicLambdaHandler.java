package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import redis.clients.jedis.Jedis;

public class ToArabicLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String RECENTS_LIST_NAME = "recents";
    private final ConverterToArabic converter = new ConverterToArabic();
    private final Jedis jedis = new Jedis(System.getenv("REDIS_HOST"), 6379);

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String romanNumber = input.getQueryStringParameters().get("number");
            System.out.println("The test value is: " + System.getenv("TEST_ENV_VAR"));
            int result = converter.convert(romanNumber);
            String value = romanNumber.toUpperCase() + "=" + result;
            jedis.lpush(RECENTS_LIST_NAME, value);
            jedis.ltrim(RECENTS_LIST_NAME, 0, 4);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(String.valueOf(result));
        } catch (ConverterToArabic.BadRomanNumberException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody(e.getMessage());
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Internal server error");
        }
    }
} 