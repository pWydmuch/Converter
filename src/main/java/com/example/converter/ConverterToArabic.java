package com.example.converter;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Service
public class ConverterToArabic {

    public int convert(String romanNumber) {
        var romanNumberUpperCase = romanNumber.toUpperCase();
        return IntStream.range(0, romanNumber.length())
                .map(i -> new DigitValueResolver(romanNumberUpperCase, i).countValueOfChar())
                .sum();
    }

    private record DigitValueResolver(String romanNumber, int currentDigitIndex) {

        private static final Character[] ROMAN_DIGITS = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};

        int countValueOfChar() {
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
            if (getNextRomanDigit() == 'V' || getNextRomanDigit() == 'X')
                return -1;
            else
                return 1;
        }

        private int countV() {
            checkIfCorrect('V', 'V');
            return 5;
        }

        private int countX() {
            checkIfCorrect('X', 'D');
            if (getNextRomanDigit() == 'L' || getNextRomanDigit() == 'C')
                return -10;
            else
                return 10;
        }

        private int countL() {
            checkIfCorrect('L', 'L');
            return 50;
        }

        private int countC() {
            checkIfCorrect('C');
            if (getNextRomanDigit() == 'D' || getNextRomanDigit() == 'M')
                return -100;
            else
                return 100;
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
                    .anyMatch(digit -> getNextRomanDigit() == digit);
        }

        private boolean isOrderOfDigitsIncorrect(char romanDigit) {
            return isTooManyTheSameConsecutiveDigits(romanDigit) ||
                    isTwoTheSameDigitsBeforeDigit(romanDigit) ||
                    isDigitBetweenTwoTheSameDigits(romanDigit);
        }

        private boolean isTooManyTheSameConsecutiveDigits(char romanDigit) {
            return (romanDigit == 'I' || romanDigit == 'X' || romanDigit == 'C' || romanDigit == 'M')
                    && romanDigit == getNextRomanDigit()
                    && romanDigit == getNextRomanDigit(currentDigitIndex + 1)
                    && romanDigit == getNextRomanDigit(currentDigitIndex + 2);// if 4 consecutive numerals are the same
        }

        private boolean isDigitBetweenTwoTheSameDigits(char romanDigit) { // prevents from situations like these IVI, CDC,XCX,
            return checkIfSurroundingsCorrect(romanDigit, this::isSameDigitOnBothSides) ||
                    (romanDigit == 'V' || romanDigit == 'L' || romanDigit == 'D') && romanDigit == getNextRomanDigit(currentDigitIndex + 1);
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
            return getPrevRomanDigit(currentDigitIndex - 1) == romanDigit && getPrevRomanDigit() == romanDigit;
        }

        private boolean isSameDigitOnBothSides(char romanDigit) {
            return getPrevRomanDigit() == romanDigit && getNextRomanDigit() == romanDigit;
        }

        private char getNextRomanDigit(int index) {
            if ((index + 1) < romanNumber.length())
                return romanNumber.charAt(index + 1);
            else
                return '0';
        }

        private char getNextRomanDigit() {
            if ((currentDigitIndex + 1) < romanNumber.length())
                return romanNumber.charAt(currentDigitIndex + 1);
            else
                return '0';
        }

        private char getPrevRomanDigit() {
            if ((currentDigitIndex - 1) >= 0)
                return romanNumber.charAt(currentDigitIndex - 1);
            else
                return '0'; //any char different from roman digits, it won't be considered anyway
        }

        private char getPrevRomanDigit(int index) {
            if ((index - 1) >= 0)
                return romanNumber.charAt(index - 1);
            else
                return '0'; //any char different from roman digits, it won't be considered anyway
        }
    }

    static class BadRomanNumberException extends RuntimeException {
        public BadRomanNumberException(String message) {
            super(message);
        }
    }
}