package com.example.converter.roman_arabic_converter;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConverterController {

    final
    private ConverterToRoman converterToRoman;
    final
    private ConverterToArabic converterToArabic;

    public ConverterController(ConverterToRoman converterToRoman, ConverterToArabic converterToArabic) {
        this.converterToRoman = converterToRoman;
        this.converterToArabic = converterToArabic;
    }

    @GetMapping("/arabic-equiv")
    public Integer getConvertedToArab(@RequestParam("number") String romanNumber) {
        return converterToArabic.convert(romanNumber);
    }

    @GetMapping("/roman-equiv")
    public String getConvertedToRoman(@RequestParam("number") Integer arabicNumber) {
        return converterToRoman.convert(arabicNumber);
    }

}
