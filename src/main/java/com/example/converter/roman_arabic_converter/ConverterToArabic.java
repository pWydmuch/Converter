package com.example.converter.roman_arabic_converter;

import org.springframework.stereotype.Service;

import java.util.function.BiFunction;

@Service
public class ConverterToArabic {

    public int convert(String romanNumber) {
        romanNumber = romanNumber.toUpperCase();
        int arabic = 0;
        for (int romanCharNumber = 0; romanCharNumber < romanNumber.length(); romanCharNumber++) {
            arabic += countValueOfChar(romanCharNumber, romanNumber);
        }
        return arabic;
    }

    //TODO tu można chyba użyć dekoratatora, żeby nie było tyle powtarzanego kodu
    private int countValueOfChar(int index, String romanNumber) {
        char romanChar = romanNumber.charAt(index);
        return switch (romanChar) {
            case 'I' -> countI(index, romanNumber);
            case 'V' -> countV(index, romanNumber);
            case 'X' -> countX(index, romanNumber);
            case 'L' -> countL(index, romanNumber);
            case 'C' -> countC(index, romanNumber);
            case 'D' -> countD(index, romanNumber);
            case 'M' -> countM(index, romanNumber);
            default -> throw new BadRomanNumberException("You can use roman digits only");
        };
    }

    private int countI(int index, String romanNumber) {
        check(index, romanNumber, 'I', 3);
        if (getNextRomanChar(index, romanNumber) == 'V' || getNextRomanChar(index, romanNumber) == 'X')
            return -1;
        else
            return 1;
    }

    private int countV(int index, String romanNumber) {
        check(index, romanNumber, 'V', 1);
        return 5;
    }

    private int countX(int index, String romanNumber) {
        check(index, romanNumber, 'X', 5);
        if (getNextRomanChar(index, romanNumber) == 'L' || getNextRomanChar(index, romanNumber) == 'C')
            return -10;
        else
            return 10;
    }

    private int countL(int index, String romanNumber) {
        check(index, romanNumber, 'X', 3);
        return 50;
    }

    private int countC(int index, String romanNumber) {
        check(index, romanNumber, 'C', 6);
        if (getNextRomanChar(index, romanNumber) == 'D' || getNextRomanChar(index, romanNumber) == 'M')
            return -100;
        else
            return 100;
    }

    private int countD(int index, String romanNumber) {
        check(index, romanNumber, 'D', 5);
        return 500;
    }

    private int countM(int index, String romanNumber) {
        check(index, romanNumber, 'M', 6);
        return 1000;
    }

    private void check(int index, String romanNumber, char romanChar, int numberOfFirst) {
        if (isNextCharIncorrect(numberOfFirst, index, romanNumber) || isOrderOfCharsWrong(index, romanChar, romanNumber)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
            throw new BadRomanNumberException("Char \"" + romanChar + "\" is used incorrectly");
    }

    private char getNextRomanChar(int index, String romanNumber) {
        if ((index + 1) < romanNumber.length())
            return romanNumber.charAt(index + 1);
        else
            return '0';
    }

    private boolean isNextCharIncorrect(int numberOfFirstCharToCheck, int index, String romanNumber) {
        char[] romanChars = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
        for (int i = numberOfFirstCharToCheck; i < romanChars.length; i++) {
            if (getNextRomanChar(index, romanNumber) == romanChars[i])
                return true;
        }
        return false;
    }

    private boolean isOrderOfCharsWrong(int index, char romanChar, String romanNumber) {
        return isTooManyTheSameConsecutiveChars(index, romanChar, romanNumber) ||
                isTwoTheSameCharsBeforeChar(index, romanChar, romanNumber) ||
                isCharBetweenTwoTheSameChars(index, romanChar, romanNumber);
    }

    private boolean isTooManyTheSameConsecutiveChars(int index, char romanChar, String romanNumber) {
        return ((romanChar == 'I' || romanChar == 'X' || romanChar == 'C' || romanChar == 'M')
                && romanChar == getNextRomanChar(index, romanNumber)
                && romanChar == getNextRomanChar(index + 1, romanNumber)
                && romanChar == getNextRomanChar(index + 2, romanNumber));// if 4 consecutive numerals are the same
    }

    private boolean isCharBetweenTwoTheSameChars(int index, char romanChar, String romanNumber) { // prevents from situations like these IVI, CDC,XCX,
        //TODO redundacja z następną metodą, tam jest getPrev, ale to powinno dać się załatwić Function
        return checkOtherChars(index, index, romanChar, romanNumber, this::getNextRomanChar) ||
                ((romanChar == 'V' || romanChar == 'L' || romanChar == 'D') && romanChar == getNextRomanChar(index + 1, romanNumber))
                // isNextCharWrong() prevents possibility of occurring e.g. VV
//        // but it doesn't prevent situation such as VIV
                ;
    }

    private boolean isTwoTheSameCharsBeforeChar(int index, char romanChar, String romanNumber) { // checks how many the same numerals are before the cartain numeral, there can't be more than one in a row
        return checkOtherChars(index, index - 1, romanChar, romanNumber, this::getNextRomanChar);
    }

    private boolean checkOtherChars(int index, int index2, char romanChar, String romanNumber, BiFunction<Integer, String, Character> func) {
        return (romanChar == 'V' && getPrevRomanChar(index, romanNumber) == 'I' && func.apply(index2, romanNumber) == 'I') ||
                (romanChar == 'X' && getPrevRomanChar(index, romanNumber) == 'I' && func.apply(index2, romanNumber) == 'I') ||
                (romanChar == 'L' && getPrevRomanChar(index, romanNumber) == 'X' && func.apply(index2, romanNumber) == 'X') ||
                (romanChar == 'C' && getPrevRomanChar(index, romanNumber) == 'X' && func.apply(index2, romanNumber) == 'X') ||
                (romanChar == 'D' && getPrevRomanChar(index, romanNumber) == 'C' && func.apply(index2, romanNumber) == 'C') ||
                (romanChar == 'M' && getPrevRomanChar(index, romanNumber) == 'C' && func.apply(index2, romanNumber) == 'C');
    }

    private char getPrevRomanChar(int index, String romanNumber) {
        if ((index - 1) >= 0)
            return romanNumber.charAt(index - 1);
        else
            return '0'; //any char different from roman digits, it won't be considered anyway
    }

    static class BadRomanNumberException extends RuntimeException {
        public BadRomanNumberException(String message) {
            super(message);
        }
    }
}