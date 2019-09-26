package com.example.converter.controller;


import com.example.converter.service.ConverterToRoman;
import com.example.converter.service.ConverterToArabic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;


@RestController
public class ConverterController {

    @Autowired
    ConverterToRoman converterToRoman;
    @Autowired
    ConverterToArabic converterToArabic;

    @GetMapping("/arabska/{roman}")
    public int getConvertedToArab(@PathVariable("roman") String romanNumber){
//        ConverterToArabic converterToArabic = new ConverterToArabic();
        romanNumber.toUpperCase();
        System.out.println(romanNumber);
        return converterToArabic.convert(romanNumber);
    }
    @GetMapping("/rzymska/{arabska}")
    public String getConvertedToRoman(@PathVariable("arabska") String arabicNumberString){
//        ConverterToRoman converterToRoman = new ConverterToRoman();
        int arabicNumber = Integer.parseInt(arabicNumberString);
        System.out.println(arabicNumber);
        String rzymska = converterToRoman.getRzymska(arabicNumber);
        return rzymska;
    }



}
