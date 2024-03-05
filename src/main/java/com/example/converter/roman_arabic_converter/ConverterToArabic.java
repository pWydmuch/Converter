package com.example.converter.roman_arabic_converter;

import org.springframework.stereotype.Service;

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
            check('I', 3);
            if (getNextRomanChar() == 'V' || getNextRomanChar() == 'X')
                return -1;
            else
                return 1;
        }

        private int countV() {
            check('V', 1);
            return 5;
        }

        private int countX() {
            check('X', 5);
            if (getNextRomanChar() == 'L' || getNextRomanChar() == 'C')
                return -10;
            else
                return 10;
        }

        private int countL() {
            check('X', 3);
            return 50;
        }

        private int countC() {
            check('C', 6);
            if (getNextRomanChar() == 'D' || getNextRomanChar() == 'M')
                return -100;
            else
                return 100;
        }

        private int countD() {
            check('D', 5);
            return 500;
        }

        private int countM() {
            check('M', 6);
            return 1000;
        }

        private boolean isNextCharIncorrect(int numberOfFirstCharToCheck) {
            char[] romanChars = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
            for (int i = numberOfFirstCharToCheck; i < romanChars.length; i++) {
                if (getNextRomanChar(currentCharIndex) == romanChars[i])
                    return true;
            }
            return false;
        }

        private boolean isOrderOfCharsWrong(char romanChar) {
            return isTooManyTheSameConsecutiveChars(romanChar) ||
                    isTwoTheSameCharsBeforeChar(romanChar) ||
                    isCharBetweenTwoTheSameChars(romanChar);
        }

        private boolean isTooManyTheSameConsecutiveChars(char romanChar) {
            return ((romanChar == 'I' || romanChar == 'X' || romanChar == 'C' || romanChar == 'M')
                    && romanChar == getNextRomanChar()
                    && romanChar == getNextRomanChar(currentCharIndex + 1)
                    && romanChar == getNextRomanChar(currentCharIndex + 2));// if 4 consecutive numerals are the same
        }

        private boolean isCharBetweenTwoTheSameChars(char romanChar) { // prevents from situations like these IVI, CDC,XCX,
            //TODO redundacja z następną metodą, tam jest getPrev, ale to powinno dać się załatwić Function
            return checkOtherChars(currentCharIndex, romanChar) ||
                    ((romanChar == 'V' || romanChar == 'L' || romanChar == 'D') && romanChar == getNextRomanChar(currentCharIndex + 1))
                    // isNextCharWrong() prevents possibility of occurring e.g. VV
                    //        // but it doesn't prevent situation such as VIV
                    ;
        }

        private boolean isTwoTheSameCharsBeforeChar(char romanChar) { // checks how many the same numerals are before the certain numeral, there can't be more than one in a row
            return checkOtherChars(currentCharIndex - 1, romanChar);
        }

        private boolean checkOtherChars(int index2, char romanChar) {
            return (romanChar == 'V' && getPrevRomanChar() == 'I' && getNextRomanChar(index2) == 'I') ||
                    (romanChar == 'X' && getPrevRomanChar() == 'I' && getNextRomanChar(index2) == 'I') ||
                    (romanChar == 'L' && getPrevRomanChar() == 'X' && getNextRomanChar(index2) == 'X') ||
                    (romanChar == 'C' && getPrevRomanChar() == 'X' && getNextRomanChar(index2) == 'X') ||
                    (romanChar == 'D' && getPrevRomanChar() == 'C' && getNextRomanChar(index2) == 'C') ||
                    (romanChar == 'M' && getPrevRomanChar() == 'C' && getNextRomanChar(index2) == 'C');
        }

        private void check(char romanChar, int numberOfFirst) {
            if (isNextCharIncorrect(numberOfFirst) || isOrderOfCharsWrong(romanChar)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"" + romanChar + "\" is used incorrectly");
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
    }

    static class BadRomanNumberException extends RuntimeException {
        public BadRomanNumberException(String message) {
            super(message);
        }
    }
}