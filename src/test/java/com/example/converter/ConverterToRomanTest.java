package com.example.converter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ConverterToRomanTest {

    private static final ConverterToRoman converter = new ConverterToRoman();

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void convertToRoman(int arabic, String expectedRoman) {
        assertThat(converter.convert(arabic)).isEqualTo(expectedRoman);
    }

    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments(1, "I"),
                arguments(4, "IV"),
                arguments(10, "X"),
                arguments(99, "XCIX"),
                arguments(158, "CLVIII"),
                arguments(3000, "MMM")
        );
    }


    @ParameterizedTest
    @ValueSource(ints = {0,100000})
    public void checkIfExceptionIsThrown(int arabic) {
        assertThatExceptionOfType(ConverterToRoman.BadArabicNumberException.class)
                .isThrownBy(() -> converter.convert(arabic));
    }
}