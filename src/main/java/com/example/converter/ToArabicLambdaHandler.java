package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ToArabicLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ConverterToArabic converter = new ConverterToArabic();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String romanNumber = input.getQueryStringParameters().get("number");
            System.out.println("The test value is: " + System.getenv("TEST_ENV_VAR"));
            int result = converter.convert(romanNumber);
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