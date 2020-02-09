package com.example.converter.controller;


import com.example.converter.exceptions.BadArabicNumberException;
import com.example.converter.exceptions.BadRomanNumberException;
import com.example.converter.model.ArabicContainer;
import com.example.converter.model.RomanContainer;
import com.example.converter.service.ConverterToRoman;
import com.example.converter.service.ConverterToArabic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ConverterController {

    final
    private ConverterToRoman converterToRoman;
    final
    private ConverterToArabic converterToArabic;

    @Autowired
    public ConverterController(ConverterToRoman converterToRoman, ConverterToArabic converterToArabic) {
        this.converterToRoman = converterToRoman;
        this.converterToArabic = converterToArabic;
    }

    @GetMapping("/arabska/{roman}")
    public String getConvertedToArab(@PathVariable("roman") String romanNumber) throws BadRomanNumberException {
        romanNumber = romanNumber.toUpperCase();
        int arabicEquiv = converterToArabic.convert(new RomanContainer(romanNumber));
        return String.valueOf(arabicEquiv);
    }

    @GetMapping("/rzymska/{arabska}")
    public String getConvertedToRoman(@PathVariable("arabska") String arabicNumberString) throws BadArabicNumberException {
        int arabicNumber = Integer.parseInt(arabicNumberString);
        String romanEquiv = converterToRoman.convert(new ArabicContainer(arabicNumber));
        return romanEquiv;
    }



}
