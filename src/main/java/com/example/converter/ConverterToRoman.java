package com.example.converter;

public class ConverterToRoman {
//TODO use redis for last 5 queries
//and overall cache for most frequently used numbers for all user so that no need for invoking business logic
// eg also to work around cold start for certain numbers (hard code some values)
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
                : "X".repeat(timesXFitsIntoArabicNumber(arabicNumber, 10)) + countRomanValue(getNumberConsistingOfNRightMostDigits(arabicNumber, 1));
    }

    private String appendMax89(Integer arabicNumber) {
        return timesXFitsIntoArabicNumber(arabicNumber,10) == 4
                ? "XL" + countRomanValue(arabicNumber - 40)
                : "L" + countRomanValue(arabicNumber - 50);
    }


    private String appendMax399(Integer arabicNumber) {
        return timesXFitsIntoArabicNumber(arabicNumber, 10) == 9
                ? "XC" + countRomanValue(arabicNumber - 90)
                : "C".repeat(timesXFitsIntoArabicNumber(arabicNumber, 100)) + countRomanValue(getNumberConsistingOfNRightMostDigits(arabicNumber, 2));
    }

    private String appendMax899(Integer arabicNumber) {
        return timesXFitsIntoArabicNumber(arabicNumber, 100) == 4
                ? "CD" + countRomanValue(arabicNumber - 400)
                : "D" + countRomanValue(arabicNumber - 500);
    }

    private String appendMax3999(Integer arabicNumber) {
        return timesXFitsIntoArabicNumber(arabicNumber, 100) == 9
                ? "CM" + countRomanValue(arabicNumber - 900)
                : "M".repeat(timesXFitsIntoArabicNumber(arabicNumber, 1000)) + countRomanValue(getNumberConsistingOfNRightMostDigits(arabicNumber, 3));
    }

    private static int timesXFitsIntoArabicNumber(Integer arabicNumber, int x) {
        return arabicNumber / x;
    }

    /**
     (123, 1) returns 3 (last digit)
     (123, 2) returns 23 (last two digits)
     (123, 3) returns 123 (last three digits)
     */
    private int getNumberConsistingOfNRightMostDigits(int number, int howManyRightMostDigits) {
        return number % (int) Math.pow(10, howManyRightMostDigits);
    }

    static class BadArabicNumberException extends RuntimeException {
        public BadArabicNumberException(String message) {
            super(message);
        }
    }
}
