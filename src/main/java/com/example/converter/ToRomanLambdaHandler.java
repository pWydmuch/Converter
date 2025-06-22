package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public class ToRomanLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final ConverterToRoman converter = new ConverterToRoman();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        System.out.println("The method is: " + input.getHttpMethod());
        try {
            String arabicNumberStr = input.getQueryStringParameters().get("number");
            String result = converter.convert(Integer.parseInt(arabicNumberStr));
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody(result);
        } catch (NumberFormatException e) {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("Invalid number format");
        } catch (ConverterToRoman.BadArabicNumberException e) {
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