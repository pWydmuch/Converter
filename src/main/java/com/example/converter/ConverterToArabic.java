package com.example.converter;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ConverterToArabic {

    public int convert(String romanNumber) {
        var romanNumberUpperCase = romanNumber.toUpperCase();
        return IntStream.range(0, romanNumber.length())
                .map(i -> new DigitValueResolver(romanNumberUpperCase, i).countValueOfChar())
                .sum();
    }

    private static class DigitValueResolver {
        private final String romanNumber;
        private final int currentDigitIndex;

        public DigitValueResolver(String romanNumber, int currentDigitIndex) {
            this.romanNumber = romanNumber;
            this.currentDigitIndex = currentDigitIndex;
        }

        private static final Character[] ROMAN_DIGITS = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};

        private int countValueOfChar() {
            var romanDigit = romanNumber.charAt(currentDigitIndex);
            return switch (romanDigit) {
                case 'I' -> countI();
                case 'V' -> countV();
                case 'X' -> countX();
                case 'L' -> countL();
                case 'C' -> countC();
                case 'D' -> countD();
                case 'M' -> countM();
                default -> throw new BadRomanNumberException("You can use roman digits only");
            };
        }

        private int countI() {
            checkIfCorrect('I', 'L');
            return isNextEqualTo('V')|| isNextEqualTo('X') ? -1 : 1;
        }

        private int countV() {
            checkIfCorrect('V', 'V');
            return 5;
        }

        private int countX() {
            checkIfCorrect('X', 'D');
            return isNextEqualTo('L') || isNextEqualTo('C') ? -10 : 10;
        }

        private int countL() {
            checkIfCorrect('L', 'L');
            return 50;
        }

        private int countC() {
            checkIfCorrect('C');
            return isNextEqualTo('D') || isNextEqualTo('M') ? -100 : 100;
        }

        private int countD() {
            checkIfCorrect('D', 'D');
            return 500;
        }

        private int countM() {
            checkIfCorrect('M');
            return 1000;
        }

        private void checkIfCorrect(char romanDigit, char lowestNextInvalid) {
            if (isNextDigitIncorrect(lowestNextInvalid) || isOrderOfDigitsIncorrect(romanDigit)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"" + romanDigit + "\" is used incorrectly");
        }

        private void checkIfCorrect(char romanDigit) {
            if (isOrderOfDigitsIncorrect(romanDigit)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"" + romanDigit + "\" is used incorrectly");
        }

        private boolean isNextDigitIncorrect(char lowestNextInvalid) {
            return Arrays.stream(ROMAN_DIGITS)
                    .dropWhile(digit -> !digit.equals(lowestNextInvalid))
                    .anyMatch(this::isNextEqualTo);
        }

        private boolean isOrderOfDigitsIncorrect(char romanDigit) {
            return isTooManyTheSameConsecutiveDigits(romanDigit) ||
                    isTwoTheSameDigitsBeforeDigit(romanDigit) ||
                    isDigitBetweenTwoTheSameDigits(romanDigit);
        }

        private boolean isTooManyTheSameConsecutiveDigits(char romanDigit) {
            return (romanDigit == 'I' || romanDigit == 'X' || romanDigit == 'C' || romanDigit == 'M')
                    && isNextEqualTo(romanDigit)
                    && isNextEqualTo(currentDigitIndex + 1, romanDigit)
                    && isNextEqualTo(currentDigitIndex + 2, romanDigit);// if 4 consecutive numerals are the same
        }

        private boolean isDigitBetweenTwoTheSameDigits(char romanDigit) { // prevents from situations like these IVI, CDC,XCX,
            return checkIfSurroundingsCorrect(romanDigit, this::isSameDigitOnBothSides) ||
                    (romanDigit == 'V' || romanDigit == 'L' || romanDigit == 'D') &&
                            isNextEqualTo(currentDigitIndex + 1, romanDigit);
            // isNextCharWrong() prevents possibility of occurring e.g. VV
            //        // but it doesn't prevent situation such as VIV
        }

        private boolean isTwoTheSameDigitsBeforeDigit(char romanDigit) { // checks how many the same numerals are before the certain numeral, there can't be more than one in a row
            return checkIfSurroundingsCorrect(romanDigit, this::isTwoSameBefore);
        }

        private boolean checkIfSurroundingsCorrect(char romanDigit, Predicate<Character> checker) {
            return romanDigit == 'V' && checker.test('I') ||
                    romanDigit == 'X' && checker.test('I') ||
                    romanDigit == 'L' && checker.test('X') ||
                    romanDigit == 'C' && checker.test('X') ||
                    romanDigit == 'D' && checker.test('C') ||
                    romanDigit == 'M' && checker.test('C');
        }

        private boolean isTwoSameBefore(char romanDigit) {
            return isPrevEqualTo(currentDigitIndex - 1, romanDigit) && isPrevEqualTo(romanDigit);
        }

        private boolean isSameDigitOnBothSides(char romanDigit) {
            return isPrevEqualTo(romanDigit) && isNextEqualTo(romanDigit);
        }

        private boolean isNextEqualTo(char romanDigit) {
            return isNextEqualTo(currentDigitIndex, romanDigit);
        }

        private boolean isPrevEqualTo(char romanDigit) {
            return isPrevEqualTo(currentDigitIndex, romanDigit);
        }

        private boolean isNextEqualTo(int index, char romanDigit) {
            return (index + 1) < romanNumber.length() && romanNumber.charAt(index + 1) == romanDigit;
        }

        private boolean isPrevEqualTo(int index, char romanDigit) {
            return (index - 1) >= 0 && romanNumber.charAt(index - 1) == romanDigit;
        }
    }

    static class BadRomanNumberException extends RuntimeException {
        public BadRomanNumberException(String message) {
            super(message);
        }
    }
}