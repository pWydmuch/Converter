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
    static final private String TO_ARABIC_LIST = "recent:roman_to_arabic";
    private static final String TO_ROMAN_LIST = "recent:arabic_to_roman";

    public ConverterController(ConverterToRoman converterToRoman, ConverterToArabic converterToArabic, Jedis jedis) {
        this.converterToRoman = converterToRoman;
        this.converterToArabic = converterToArabic;
        this.jedis = jedis;
    }

    @GetMapping("/arabic-equiv")
    public Integer getConvertedToArab(@RequestParam("number") String romanNumber) {
        Integer result = converterToArabic.convert(romanNumber);
        String value = romanNumber.toUpperCase() + "=" + result;
        jedis.lpush(TO_ARABIC_LIST, value);
        jedis.ltrim(TO_ARABIC_LIST, 0, 4);
        return result;
    }

    @GetMapping("/roman-equiv")
    public String getConvertedToRoman(@RequestParam("number") Integer arabicNumber) {
        String result = converterToRoman.convert(arabicNumber);
        String value = arabicNumber + "=" + result;
        jedis.lpush(TO_ROMAN_LIST, value);
        jedis.ltrim(TO_ROMAN_LIST, 0, 4);
        return result;
    }

    @GetMapping("/recents-to-arabic")
    public Map<String, Integer> getRecentToArabicConversions() {
        List<String> recentConversions = jedis.lrange(TO_ARABIC_LIST, 0, 4);
        return recentConversions.stream()
                .map(s -> s.split("="))
                .collect(
                        Collectors.toMap(
                                parts -> parts[0],
                                parts -> Integer.parseInt(parts[1]),
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        )
                );

    }

    @GetMapping("/recents-to-roman")
    public Map<Integer, String> getRecentToRomanConversions() {
        List<String> recentConversions = jedis.lrange(TO_ROMAN_LIST, 0, 4);
        return recentConversions.stream()
                .map(s -> s.split("="))
                .collect(
                        Collectors.toMap(
                                parts -> Integer.parseInt(parts[0]),
                                parts -> parts[1],
                                (existing, replacement) -> existing,
                                LinkedHashMap::new
                        )
                );

    }

}
