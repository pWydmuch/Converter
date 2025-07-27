package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import static com.example.converter.RedisClient.RECENTS_LIST_NAME;
import static com.example.converter.RedisClient.JEDIS;


public class GetRecentsLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        List<String> recentConversions = JEDIS.lrange(RECENTS_LIST_NAME, 0, 4);
        List<List<String>> list = recentConversions.stream()
                .map(conversion -> conversion.split("="))
                .map(parts -> List.of(parts[0], parts[1]))
                .toList();
        try {
            String body = objectMapper.writeValueAsString(list);
            context.getLogger().log("Recent conversions: " + body);
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
