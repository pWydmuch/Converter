package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import static com.example.converter.RedisClient.RECENTS_LIST_NAME;
import static com.example.converter.RedisClient.JEDIS;


public class ToRomanLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ConverterToRoman converter = new ConverterToRoman();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            String arabicNumberStr = input.getQueryStringParameters().get("number");
            String result = converter.convert(Integer.parseInt(arabicNumberStr));
            String value = arabicNumberStr + "=" + result;
            JEDIS.lpush(RECENTS_LIST_NAME, value);
            JEDIS.ltrim(RECENTS_LIST_NAME, 0, 4);

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