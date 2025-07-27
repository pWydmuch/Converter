package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static com.example.converter.RedisClient.RECENTS_LIST_NAME;
import static com.example.converter.RedisClient.JEDIS;

public class ToArabicLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ConverterToArabic converter = new ConverterToArabic();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String romanNumber = input.getQueryStringParameters().get("number");
            int result = converter.convert(romanNumber);
            String value = romanNumber.toUpperCase() + "=" + result;
            JEDIS.lpush(RECENTS_LIST_NAME, value);
            JEDIS.ltrim(RECENTS_LIST_NAME, 0, 4);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(String.valueOf(result));
        } catch (ConverterToArabic.BadRomanNumberException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody(e.getMessage());
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
            e.printStackTrace();
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(500)
                    .withBody("Internal server error");
        }
    }
} 