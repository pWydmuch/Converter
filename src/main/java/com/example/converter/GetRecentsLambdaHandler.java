package com.example.converter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import java.util.List;
import java.util.Map;

public class GetRecentsLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String RECENTS_LIST_NAME = "recents";
    private final Jedis jedis = new Jedis(System.getenv("REDIS_HOST"), 6379);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        List<String> recentConversions = jedis.lrange(RECENTS_LIST_NAME, 0, 4);
        List<List<String>> list = recentConversions.stream()
                .map(conversion -> conversion.split("="))
                .map(parts -> List.of(parts[0], parts[1]))
                .toList();
        try {
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withHeaders(Map.of("Content-Type", "application/json"))
                    .withBody(objectMapper.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
