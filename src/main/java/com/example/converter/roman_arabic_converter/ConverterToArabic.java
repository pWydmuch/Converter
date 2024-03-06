package com.example.converter.roman_arabic_converter;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Service
public class ConverterToArabic {

    public int convert(String romanNumber) {
        String romanNumberUpperCase = romanNumber.toUpperCase();
        return IntStream.range(0, romanNumber.length())
                .mapToObj(i -> new ConverterHelper(romanNumberUpperCase, i))
                .mapToInt(ConverterHelper::countValueOfChar)
                .sum();
    }

    private record ConverterHelper(String romanNumber, int currentCharIndex) {

        private static final Character[] ROMAN_DIGITS = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};

        private int countValueOfChar() {
            char romanDigit = romanNumber.charAt(currentCharIndex);
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
            if (getNextRomanChar() == 'V' || getNextRomanChar() == 'X')
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
            if (getNextRomanChar() == 'L' || getNextRomanChar() == 'C')
                return -10;
            else
                return 10;
        }

        private int countL() {
            checkIfCorrect('L','L');
            return 50;
        }

        private int countC() {
            checkIfCorrect('C');
            if (getNextRomanChar() == 'D' || getNextRomanChar() == 'M')
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

        private void checkIfCorrect(char romanChar, char lowestNextInvalid) {
            if (isNextCharIncorrect(lowestNextInvalid) || isOrderOfCharsIncorrect(romanChar)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"" + romanChar + "\" is used incorrectly");
        }

        private void checkIfCorrect(char romanChar) {
            if (isOrderOfCharsIncorrect(romanChar)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"" + romanChar + "\" is used incorrectly");
        }

        private boolean isNextCharIncorrect(char lowestNextInvalid) {
            return Arrays.stream(ROMAN_DIGITS)
                    .dropWhile(e -> !e.equals(lowestNextInvalid))
                    .anyMatch(e -> getNextRomanChar(currentCharIndex) == e);
        }

        private boolean isOrderOfCharsIncorrect(char romanChar) {
            return isTooManyTheSameConsecutiveChars(romanChar) ||
                    isTwoTheSameCharsBeforeChar(romanChar) ||
                    isCharBetweenTwoTheSameChars(romanChar);
        }

        private boolean isTooManyTheSameConsecutiveChars(char romanChar) {
            return (romanChar == 'I' || romanChar == 'X' || romanChar == 'C' || romanChar == 'M')
                    && romanChar == getNextRomanChar()
                    && romanChar == getNextRomanChar(currentCharIndex + 1)
                    && romanChar == getNextRomanChar(currentCharIndex + 2);// if 4 consecutive numerals are the same
        }

        private boolean isCharBetweenTwoTheSameChars(char romanChar) { // prevents from situations like these IVI, CDC,XCX,
            return checkIfSurroundingsCorrect(romanChar, this::isSameCharOnBothSides) ||
                    (romanChar == 'V' || romanChar == 'L' || romanChar == 'D') && romanChar == getNextRomanChar(currentCharIndex + 1);
            // isNextCharWrong() prevents possibility of occurring e.g. VV
            //        // but it doesn't prevent situation such as VIV
        }

        private boolean isTwoTheSameCharsBeforeChar(char romanChar) { // checks how many the same numerals are before the certain numeral, there can't be more than one in a row
            return checkIfSurroundingsCorrect(romanChar, this::isTwoSameBefore);
        }

        private boolean checkIfSurroundingsCorrect(char romanChar, Predicate<Character> checker) {
            return romanChar == 'V' && checker.test('I') ||
                    romanChar == 'X' && checker.test('I') ||
                    romanChar == 'L' && checker.test('X') ||
                    romanChar == 'C' && checker.test('X') ||
                    romanChar == 'D' && checker.test('C') ||
                    romanChar == 'M' && checker.test('C');
        }

        private boolean isTwoSameBefore(char romanChar) {
            return getPrevRomanChar(currentCharIndex - 1) == romanChar && getPrevRomanChar() == romanChar;
        }

        private boolean isSameCharOnBothSides(char romanChar) {
            return getPrevRomanChar() == romanChar && getNextRomanChar() == romanChar;
        }


        private char getNextRomanChar(int index) {
            if ((index + 1) < romanNumber.length())
                return romanNumber.charAt(index + 1);
            else
                return '0';
        }

        private char getNextRomanChar() {
            if ((currentCharIndex + 1) < romanNumber.length())
                return romanNumber.charAt(currentCharIndex + 1);
            else
                return '0';
        }

        private char getPrevRomanChar() {
            if ((currentCharIndex - 1) >= 0)
                return romanNumber.charAt(currentCharIndex - 1);
            else
                return '0'; //any char different from roman digits, it won't be considered anyway
        }

        private char getPrevRomanChar(int index) {
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