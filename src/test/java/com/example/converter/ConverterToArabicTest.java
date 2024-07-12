package com.example.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ConverterToArabicTest {

    private static final ConverterToArabic converter = new ConverterToArabic();

    @ParameterizedTest
    @MethodSource("dataProvider")
    void convert(String roman, int expectedArabic) {
        assertThat(converter.convert(roman)).isEqualTo(expectedArabic);
    }

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments("I", 1),
                arguments("II", 2),
                arguments("III", 3),
                arguments("IV", 4),
                arguments("V", 5),
                arguments("VI", 6),
                arguments("X", 10),
                arguments("XVI", 16),
                arguments("XXX", 30),
                arguments("L", 50),
                arguments("XCIX", 99),
                arguments("C", 100),
                arguments("D", 500),
                arguments("CDXCIX", 499),
                arguments("MI", 1001),
                arguments("MMM", 3000)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"IIII", "IC", "VX", "LL"})
    void checkIfExceptionIsThrown(String roman) {
        assertThatExceptionOfType(ConverterToArabic.BadRomanNumberException.class)
                .isThrownBy(() -> converter.convert(roman));
    }
}