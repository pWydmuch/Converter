package com.example.converter.roman_arabic_converter;

import org.springframework.stereotype.Service;

@Service
public class ConverterToRoman {

    public String convert(int arabicNumber) {
        if (arabicNumber <= 0)
            throw new BadArabicNumberException("The number must be greater than 0");
        return countRomanValue(arabicNumber);
    }

    private String countRomanValue(Integer arabicNumber) {
        return switch (arabicNumber) {
            case 0 -> "";
            case Integer i when i > 0 && i <= 3 -> appendMax3(arabicNumber);
            case Integer i when i > 3 && i <= 8 -> appendMax8(arabicNumber);
            case Integer i when i > 8 && i <= 39 -> appendMax39(arabicNumber);
            case Integer i when i > 39 && i <= 89 -> appendMax89(arabicNumber);
            case Integer i when i > 89 && i <= 399 -> appendMax399(arabicNumber);
            case Integer i when i > 399 && i <= 899 -> appendMax899(arabicNumber);
            case Integer i when i > 899 && i <= 3999 -> appendMax3999(arabicNumber);
            default -> throw new IllegalStateException("Unexpected value: " + arabicNumber);
        };
    }

    private String appendMax3(Integer arabicNumber) {
        return "I".repeat(arabicNumber);
    }

    private String appendMax8(Integer arabicNumber) {
        if (arabicNumber == 4) {
            return "IV";
        } else {
            return 'V' + countRomanValue(arabicNumber - 5);
        }
    }

    private String appendMax39(Integer arabicNumber) {
        if (arabicNumber == 9) {
            return "IX";
        } else {
            return "X".repeat(arabicNumber / 10) + countRomanValue(arabicNumber - arabicNumber / 10 * 10);
        }
    }

    private String appendMax89(Integer arabicNumber) {
        if (arabicNumber / 10 == 4) {
            return "XL" + countRomanValue(arabicNumber - 40);
        } else {
            return "L" + countRomanValue(arabicNumber - 50);
        }
    }

    private String appendMax399(Integer arabicNumber) {
        if (arabicNumber / 10 == 9) {
            return "XC" + countRomanValue(arabicNumber - 90);
        } else {
            return "C".repeat(arabicNumber / 100) + countRomanValue(arabicNumber - arabicNumber / 100 * 100);
        }
    }

    private String appendMax899(Integer arabicNumber) {
        if (arabicNumber / 100 == 4) {
            return "CD" + countRomanValue(arabicNumber - 400);
        } else {
            return "D" + countRomanValue(arabicNumber - 500);
        }
    }

    private String appendMax3999(Integer arabicNumber) {
        if (arabicNumber / 100 == 9) {
            return "CM" + countRomanValue(arabicNumber - 900);
        } else {
            return "M".repeat(arabicNumber / 1000) + countRomanValue(arabicNumber - arabicNumber / 1000 * 1000);
        }
    }

    static class BadArabicNumberException extends RuntimeException {
        public BadArabicNumberException(String message) {
            super(message);
        }
    }
}
