package com.example.converter;

public class ConverterToRoman {

    public String convert(int arabicNumber) {
        checkIfInRange(arabicNumber);
        return countRomanValue(arabicNumber);
    }

    private static void checkIfInRange(int arabicNumber) {
        if (arabicNumber <= 0) throw new BadArabicNumberException("The number must be greater than 0");
        if (arabicNumber >= 4000) throw new BadArabicNumberException("The number must be smaller than 4000");
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
            default -> throw new BadArabicNumberException("Unexpected value: " + arabicNumber);
        };
    }

    private String appendMax3(Integer arabicNumber) {
        return "I".repeat(arabicNumber);
    }

    private String appendMax8(Integer arabicNumber) {
        return arabicNumber == 4
                ? "IV"
                : 'V' + countRomanValue(arabicNumber - 5);
    }

    private String appendMax39(Integer arabicNumber) {
        return arabicNumber == 9
                ? "IX"
                : "X".repeat(arabicNumber / 10) + countRomanValue(arabicNumber - arabicNumber / 10 * 10);
    }

    private String appendMax89(Integer arabicNumber) {
        return arabicNumber / 10 == 4
                ? "XL" + countRomanValue(arabicNumber - 40)
                : "L" + countRomanValue(arabicNumber - 50);
    }

    private String appendMax399(Integer arabicNumber) {
        return arabicNumber / 10 == 9
                ? "XC" + countRomanValue(arabicNumber - 90)
                : "C".repeat(arabicNumber / 100) + countRomanValue(arabicNumber - arabicNumber / 100 * 100);
    }

    private String appendMax899(Integer arabicNumber) {
        return arabicNumber / 100 == 4
                ? "CD" + countRomanValue(arabicNumber - 400)
                : "D" + countRomanValue(arabicNumber - 500);
    }

    private String appendMax3999(Integer arabicNumber) {
        return arabicNumber / 100 == 9
                ? "CM" + countRomanValue(arabicNumber - 900)
                : "M".repeat(arabicNumber / 1000) + countRomanValue(arabicNumber - arabicNumber / 1000 * 1000);
    }

    static class BadArabicNumberException extends RuntimeException {
        public BadArabicNumberException(String message) {
            super(message);
        }
    }
}
