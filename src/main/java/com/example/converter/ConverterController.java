package com.example.converter;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@RestController
//@CrossOrigin("*")
public class ConverterController {

    final private ConverterToRoman converterToRoman;
    final private ConverterToArabic converterToArabic;

    final private Jedis jedis;
    private static final String RECENTS_LIST_NAME = "recents";

    public ConverterController(ConverterToRoman converterToRoman, ConverterToArabic converterToArabic, Jedis jedis) {
        this.converterToRoman = converterToRoman;
        this.converterToArabic = converterToArabic;
        this.jedis = jedis;
    }

    @GetMapping("/arabic-equiv")
    public Integer getConvertedToArab(@RequestParam("number") String romanNumber) {
        Integer result = converterToArabic.convert(romanNumber);
        String value = romanNumber.toUpperCase() + "=" + result;
        jedis.lpush(RECENTS_LIST_NAME, value);
        jedis.ltrim(RECENTS_LIST_NAME, 0, 4);
        return result;
    }

    @GetMapping("/roman-equiv")
    public String getConvertedToRoman(@RequestParam("number") Integer arabicNumber) {
        String result = converterToRoman.convert(arabicNumber);
        String value = arabicNumber + "=" + result;
        jedis.lpush(RECENTS_LIST_NAME, value);
        jedis.ltrim(RECENTS_LIST_NAME, 0, 4);
        return result;
    }

    @GetMapping("/recents")
    public List<List<String>> getRecentConversions() {
        List<String> recentConversions = jedis.lrange(RECENTS_LIST_NAME, 0, 4);
        return recentConversions.stream()
                .map(conversion -> conversion.split("="))
                .map(parts -> List.of(parts[0], parts[1]))
                .toList();

    }

}
