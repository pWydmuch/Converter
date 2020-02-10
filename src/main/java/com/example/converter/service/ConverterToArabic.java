package com.example.converter.service;

import com.example.converter.exceptions.BadRomanNumberException;
import com.example.converter.model.RomanContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConverterToArabic {


    public int convert(RomanContainer romanContainer) throws BadRomanNumberException {

        for (int romanCharNumber = 0; romanCharNumber < romanContainer.getRoman().length(); romanCharNumber++) {
            countValueOfChar(romanCharNumber, romanContainer);
        }
        return romanContainer.getArabicEquiv();
    }

    private void countValueOfChar(int index, RomanContainer romanContainer) throws BadRomanNumberException {

        char romanChar = romanContainer.getRoman().charAt(index);
        int arabicEquiv = romanContainer.getArabicEquiv();
        String romanNumber = romanContainer.getRoman();

        if (romanChar == 'I') {
            if (isNextCharWrong(3, index, romanNumber) || isOrderOfCharsWrong(index, 'I', romanNumber)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                throw new BadRomanNumberException("Char \"I\" is used wrong");
            else if (getNextRomanChar(index, romanNumber) == 'V' || getNextRomanChar(index, romanNumber) == 'X')
                romanContainer.setArabicEquiv(arabicEquiv - 1);
            else
                romanContainer.setArabicEquiv(arabicEquiv + 1);

        } else if (romanChar == 'V') { // Write else if-s because I need an else block at the end of this method which assigns true to czyBlad

            if (isNextCharWrong(1, index, romanNumber) || isOrderOfCharsWrong(index, 'V', romanNumber)) // there's  need to check isTooManyTheSameConsecutiveChars() because althought there can't be// two or more V there can be for example VIV what's incorrect
                throw new BadRomanNumberException("Char \"V\" is used wrong");
            else
                romanContainer.setArabicEquiv(arabicEquiv + 5);

        } else if (romanChar == 'X') {

            if (isNextCharWrong(5, index, romanNumber) || isOrderOfCharsWrong(index, 'X', romanNumber))
                throw new BadRomanNumberException("Char \"X\" is used wrong");
            if (getNextRomanChar(index, romanNumber) == 'L' || getNextRomanChar(index, romanNumber) == 'C')
                romanContainer.setArabicEquiv(arabicEquiv - 10);
            else
                romanContainer.setArabicEquiv(arabicEquiv + 10);


        } else if (romanChar == 'L')
            if (isNextCharWrong(3, index, romanNumber) || isOrderOfCharsWrong(index, 'L', romanNumber))
                throw new BadRomanNumberException("Char \"L\" is used wrong");
            else
                romanContainer.setArabicEquiv(arabicEquiv + 50);

        else if (romanChar == 'C') {
            if (isOrderOfCharsWrong(index, 'C', romanNumber))      // there's no need for isNextCharWrong() because C can stand before every roman numeral including C and so can M
                throw new BadRomanNumberException("Char \"C\" is used wrong");
            if (getNextRomanChar(index, romanNumber) == 'D' || getNextRomanChar(index, romanNumber) == 'M')
                romanContainer.setArabicEquiv(arabicEquiv - 100);
            else
                romanContainer.setArabicEquiv(arabicEquiv + 100);

        } else if (romanChar == 'D')
            if (isNextCharWrong(5, index, romanNumber) || isOrderOfCharsWrong(index, 'D', romanNumber))
                throw new BadRomanNumberException("Char \"D\" is used wrong");
            else
                romanContainer.setArabicEquiv(arabicEquiv + 500);

        else if (romanChar == 'M') {
            if (isOrderOfCharsWrong(index, 'M', romanNumber))
                throw new BadRomanNumberException("Char \"M\" is used wrong");
            else
                romanContainer.setArabicEquiv(arabicEquiv + 1000);

        } else
            throw new BadRomanNumberException("You can use roman digits only ");

    }

    private char getPrevRomanChar(int index, String romanNumber) {
        if ((index - 1) >= 0)
            return romanNumber.charAt(index - 1);
        else
            return '0';
    }


    private char getNextRomanChar(int index, String romanNumber) {
        if ((index + 1) < romanNumber.length())
            return romanNumber.charAt(index + 1);
        else
            return '0'; // any char, it won't be considered anyway, it's needed only because this method
        // has to return sth

        //moze w takim wypadku lepiej throw new RuntimeException("Should not happen");

    }


    private boolean isNextCharWrong(int indexOdFirstCharToCheck, int index, String romanNumber) {
        char[] romanChars = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
        for (int i = indexOdFirstCharToCheck; i < romanChars.length; i++) {
            if (getNextRomanChar(index, romanNumber) == romanChars[i])
                return true; // sth wrong happened
            //Perhpas  it would look better if method returned true when there's no wrong, but then i would have to change condition statements in if-s which calls the method
        }
        return false; // everything's all right
    }

    private boolean isOrderOfCharsWrong(int index, char romanChar, String romanNumber) {
        return isTooManyTheSameConsecutiveChars(index, romanChar, romanNumber) ||
                isTwoTheSameCharsBeforeChar(index, romanChar, romanNumber) ||
                isCharBetweenTwoTheSameChars(index, romanChar, romanNumber);
    }

    private boolean isTooManyTheSameConsecutiveChars(int index, char romanChar, String romanNumber) {

        if ((romanChar == 'I' || romanChar == 'X' || romanChar == 'C' || romanChar == 'M')
                && romanChar == getNextRomanChar(index, romanNumber)
                && romanChar == getNextRomanChar(index + 1, romanNumber)
                && romanChar == getNextRomanChar(index + 2, romanNumber))// if 4 consecutive numerals are the same
            return true;

        if ((romanChar == 'V' || romanChar == 'L' || romanChar == 'D')
                && romanChar == getNextRomanChar(index + 1, romanNumber))
            return true; // isNextCharWrong() exludes possibility of occuring e.g. VV
        // but it doesn't exlude situation such as VIV

        return false;

    }

    private boolean isCharBetweenTwoTheSameChars(int index, char romanChar, String romanNumber) { // prevents from situations like these IVI, CDC,XCX,
        //TODO redundacja z następną metodą, tam jest getPrev, ale to powinno dać się załatwić Function
        return  (romanChar == 'V' && getPrevRomanChar(index, romanNumber) == 'I' && getNextRomanChar(index, romanNumber) == 'I') ||
                (romanChar == 'X' && getPrevRomanChar(index, romanNumber) == 'I' && getNextRomanChar(index, romanNumber) == 'I') ||
                (romanChar == 'L' && getPrevRomanChar(index, romanNumber) == 'X' && getNextRomanChar(index, romanNumber) == 'X') ||
                (romanChar == 'C' && getPrevRomanChar(index, romanNumber) == 'X' && getNextRomanChar(index, romanNumber) == 'X') ||
                (romanChar == 'D' && getPrevRomanChar(index, romanNumber) == 'C' && getNextRomanChar(index, romanNumber) == 'C') ||
                (romanChar == 'M' && getPrevRomanChar(index, romanNumber) == 'C' && getNextRomanChar(index, romanNumber) == 'C');
    }

    private boolean isTwoTheSameCharsBeforeChar(int index, char romanChar, String romanNumber) { // checks how many the same numerals are before the cartain numeral, there can't be more than one in a row
        return  (romanChar == 'V' && getPrevRomanChar(index, romanNumber) == 'I' && getPrevRomanChar(index - 1, romanNumber) == 'I') ||
                (romanChar == 'X' && getPrevRomanChar(index, romanNumber) == 'I' && getPrevRomanChar(index - 1, romanNumber) == 'I') ||
                (romanChar == 'L' && getPrevRomanChar(index, romanNumber) == 'X' && getPrevRomanChar(index - 1, romanNumber) == 'X') ||
                (romanChar == 'C' && getPrevRomanChar(index, romanNumber) == 'X' && getPrevRomanChar(index - 1, romanNumber) == 'X') ||
                (romanChar == 'D' && getPrevRomanChar(index, romanNumber) == 'C' && getPrevRomanChar(index - 1, romanNumber) == 'C') ||
                (romanChar == 'M' && getPrevRomanChar(index, romanNumber) == 'C' && getPrevRomanChar(index - 1, romanNumber) == 'C');
    }

//    private boolean isTwoTest(int index, char romanChar, String romanNumber){
//        Map<Character, Character> chars = new HashMap<>();
//        chars.put('V','I');
//        chars.put('X','I');
//        chars.put('L','X');
//        chars.put('C','X');
//        chars.put('D','C');
//        chars.put('M','V');
//
//        List<Boolean> wyniki = chars.entrySet().
//                stream()
//                .map(entry -> cod(romanChar,entry.getKey(),entry.getValue(),romanNumber,index))
//                .collect(Collectors.toList());
//
//        Boolean aBoolean = wyniki.stream().findFirst(wynik -> wynik == true).get();
//        return aBoolean;
//
//    }

    private boolean cod(char romanChar, char baseChar ,char prevChar, String romanNumber, int index){
        return romanChar == baseChar
                && getPrevRomanChar(index, romanNumber) == prevChar
                && getPrevRomanChar(index - 1, romanNumber) == prevChar;
    }
}





