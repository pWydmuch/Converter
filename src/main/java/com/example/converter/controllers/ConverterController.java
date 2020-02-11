package com.example.converter.controllers;


import com.example.converter.exceptions.BadArabicNumberException;
import com.example.converter.exceptions.BadRomanNumberException;
import com.example.converter.model.ArabicContainer;
import com.example.converter.model.RomanContainer;
import com.example.converter.services.ConverterToRoman;
import com.example.converter.services.ConverterToArabic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//TODO usun CrossOrigin
@CrossOrigin("*")
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

    @GetMapping("/arabic-equiv")
    public String getConvertedToArab(@RequestParam("number") String romanNumber) throws BadRomanNumberException {
        romanNumber = romanNumber.toUpperCase();
        int arabicEquiv = converterToArabic.convert(new RomanContainer(romanNumber));
        return String.valueOf(arabicEquiv);
    }

    @GetMapping("/roman-equiv")
    public String getConvertedToRoman(@RequestParam("number") String arabicNumberString) throws BadArabicNumberException {
        try {
            int arabicNumber = Integer.parseInt(arabicNumberString);
            return converterToRoman.convert(new ArabicContainer(arabicNumber));
        }catch(NumberFormatException e){
            throw new BadArabicNumberException("You can use digits only");
        }
    }



}
