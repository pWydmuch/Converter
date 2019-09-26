package com.example.converter.controller;


import com.example.converter.service.ConverterToRoman;
import com.example.converter.service.ConverterToArabic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ConverterController {
    @Autowired
    ConverterToRoman converterToRoman;
    @Autowired
    ConverterToArabic converterToArabic;
    @GetMapping("/arabska/{rzymska}")
    public int getConvertedToArab(@PathVariable String romanNumber){
        romanNumber.toUpperCase();
        return converterToArabic.convert(romanNumber);
    }
    @GetMapping("/rzymska/{arabska}")
    public String getConvertedToRoman(@PathVariable String arabicNumberString){
        int arabicNumber = Integer.parseInt(arabicNumberString);
        String rzymska = converterToRoman.getRzymska(arabicNumber);
        return rzymska;
    }



}
