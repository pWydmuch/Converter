package com.example.converter;

import redis.clients.jedis.Jedis;

public class RedisClient {

    static final String RECENTS_LIST_NAME = "recents";
    static final Jedis JEDIS = new Jedis(System.getenv("REDIS_HOST"), 6379);

}
