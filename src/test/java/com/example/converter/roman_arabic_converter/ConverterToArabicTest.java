package com.example.converter.roman_arabic_converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ConverterToArabicTest {

    @Test
    void convert() {
        int result = new ConverterToArabic().convert("I");
        Assertions.assertThat(result).isEqualTo(1);
    }
}