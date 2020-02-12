package com.example.converter.services;

import com.example.converter.exceptions.BadRomanNumberException;
import com.example.converter.model.RomanContainer;
import org.springframework.stereotype.Service;

import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class ConverterToArabic {


    public int convert(RomanContainer romanContainer) throws BadRomanNumberException {

        for (int romanCharNumber = 0; romanCharNumber < romanContainer.getRoman().length(); romanCharNumber++) {
            countValueOfChar(romanCharNumber, romanContainer);
        }
        return romanContainer.getArabicEquiv();
    }

    //TODO tu można chyba użyć dekoratatora, żeby nie było tyle powtarzanego kodu
    private void countValueOfChar(int index, RomanContainer romanContainer) throws BadRomanNumberException {

        char romanChar = romanContainer.getRoman().charAt(index);
        int arabicEquiv = romanContainer.getArabicEquiv();
        String romanNumber = romanContainer.getRoman();
        switch(romanChar) {
            case 'I':
                if (isNextCharWrong(3, index, romanNumber) || isOrderOfCharsWrong(index, 'I', romanNumber)) // isNextCharWrong() checks in front of which roman numerals this numeral mustn't stand// 3 and 6 in arguments field are equivalents of L and M
                    throw new BadRomanNumberException("Char \"I\" is used wrong");
                else if (getNextRomanChar(index, romanNumber) == 'V' || getNextRomanChar(index, romanNumber) == 'X')
                    romanContainer.setArabicEquiv(arabicEquiv - 1);
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 1);
                break;
            case 'V' :

                if (isNextCharWrong(1, index, romanNumber) || isOrderOfCharsWrong(index, 'V', romanNumber)) // there's  need to check isTooManyTheSameConsecutiveChars() because althought there can't be// two or more V there can be for example VIV what's incorrect
                    throw new BadRomanNumberException("Char \"V\" is used wrong");
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 5);
                break;
            case 'X' :

                if (isNextCharWrong(5, index, romanNumber) || isOrderOfCharsWrong(index, 'X', romanNumber))
                    throw new BadRomanNumberException("Char \"X\" is used wrong");
                if (getNextRomanChar(index, romanNumber) == 'L' || getNextRomanChar(index, romanNumber) == 'C')
                    romanContainer.setArabicEquiv(arabicEquiv - 10);
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 10);
                break;
            case 'L' :
                if (isNextCharWrong(3, index, romanNumber) || isOrderOfCharsWrong(index, 'L', romanNumber))
                    throw new BadRomanNumberException("Char \"L\" is used wrong");
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 50);
                break;
            case 'C' :
                if (isOrderOfCharsWrong(index, 'C', romanNumber))      // there's no need for isNextCharWrong() because C can stand before every roman numeral including C and so can M
                    throw new BadRomanNumberException("Char \"C\" is used wrong");
                if (getNextRomanChar(index, romanNumber) == 'D' || getNextRomanChar(index, romanNumber) == 'M')
                    romanContainer.setArabicEquiv(arabicEquiv - 100);
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 100);
                break;
            case 'D':
                if (isNextCharWrong(5, index, romanNumber) || isOrderOfCharsWrong(index, 'D', romanNumber))
                    throw new BadRomanNumberException("Char \"D\" is used wrong");
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 500);
                break;
            case 'M' :
                if (isOrderOfCharsWrong(index, 'M', romanNumber))
                    throw new BadRomanNumberException("Char \"M\" is used wrong");
                else
                    romanContainer.setArabicEquiv(arabicEquiv + 1000);
                break;
             default:
                throw new BadRomanNumberException("You can use roman digits only ");
        }
    }

    private char getPrevRomanChar(int index, String romanNumber) {
        if ((index - 1) >= 0)
            return romanNumber.charAt(index - 1);
        else
            return '0'; //any char different from roman digits, it won't be consider anyway
    }


    private char getNextRomanChar(int index, String romanNumber) {
        if ((index + 1) < romanNumber.length())
            return romanNumber.charAt(index + 1);
        else
            return '0';
    }


    private boolean isNextCharWrong(int numberOfFirstCharToCheck, int index, String romanNumber) {
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
        return  checkOtherChars(index,index,romanChar, romanNumber, this::getNextRomanChar) ||
                ((romanChar == 'V' || romanChar == 'L' || romanChar == 'D') && romanChar == getNextRomanChar(index + 1, romanNumber))
                // isNextCharWrong() prevents possibility of occurring e.g. VV
//        // but it doesn't prevent situation such as VIV
                ;
    }

    private boolean isTwoTheSameCharsBeforeChar(int index, char romanChar, String romanNumber) { // checks how many the same numerals are before the cartain numeral, there can't be more than one in a row
        return  checkOtherChars(index, index-1, romanChar, romanNumber, this::getNextRomanChar);


    }

    private boolean checkOtherChars(int index, int index2, char romanChar, String romanNumber, BiFunction<Integer,String,Character> func){
        return  (romanChar == 'V' && getPrevRomanChar(index, romanNumber) == 'I' && func.apply(index2, romanNumber)  == 'I') ||
                (romanChar == 'X' && getPrevRomanChar(index, romanNumber) == 'I' && func.apply(index2, romanNumber) == 'I') ||
                (romanChar == 'L' && getPrevRomanChar(index, romanNumber) == 'X' && func.apply(index2, romanNumber) == 'X') ||
                (romanChar == 'C' && getPrevRomanChar(index, romanNumber) == 'X' && func.apply(index2, romanNumber) == 'X') ||
                (romanChar == 'D' && getPrevRomanChar(index, romanNumber) == 'C' && func.apply(index2, romanNumber) == 'C') ||
                (romanChar == 'M' && getPrevRomanChar(index, romanNumber) == 'C' && func.apply(index2, romanNumber) == 'C');
    }


}





