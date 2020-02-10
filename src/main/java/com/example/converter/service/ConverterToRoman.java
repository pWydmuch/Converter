package com.example.converter.service;

import com.example.converter.exceptions.BadArabicNumberException;
import com.example.converter.model.ArabicContainer;
import org.springframework.stereotype.Service;


@Service
public class ConverterToRoman {


    public String convert(ArabicContainer arabicContainer) throws BadArabicNumberException {

        if (arabicContainer.getArabicNumber() == 0)
            throw new BadArabicNumberException("The number can't be 0");

        countRomanValue(arabicContainer);
        return arabicContainer.getRomanEquiv().toString();
    }


    private void countRomanValue(ArabicContainer arabicContainer) throws BadArabicNumberException {

        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();

        if (arabicNumber > 0 && arabicNumber <= 3)
            appendMax3(arabicContainer);
        else if (arabicNumber > 3 && arabicNumber <= 8)
            appendMax8( arabicContainer);
        else if (arabicNumber > 8 && arabicNumber <= 39)
            appendMax39( arabicContainer);
        else if (arabicNumber > 39 && arabicNumber <= 89)
            appendMax89(arabicContainer);
        else if (arabicNumber > 89 && arabicNumber <= 399)
            appendMax399(arabicContainer);
        else if (arabicNumber > 399 && arabicNumber <= 899)
            appendMax899( arabicContainer);
        else if (arabicNumber > 899 && arabicNumber <= 3999)
            appendMax3999(arabicContainer);
        if (arabicNumber < 0 || arabicNumber >= 4000) {
            throw new BadArabicNumberException("The number must be between 1 and 3999");
        }

    }

    private void appendMax3(ArabicContainer arabicContainer) {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        for (int i = 0; i < arabicNumber; i++)
            romanEquiv.append('I');
    }


    private void appendMax8(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber == 4) {
            romanEquiv.append('I');
            romanEquiv.append('V');
        }
        if (arabicNumber >= 5) {
            romanEquiv.append('V');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 5);
            countRomanValue(arabicContainer);
        }

    }

    private void appendMax39(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber == 9) {
            romanEquiv.append('I');
            romanEquiv.append('X');
        } else {
            for (int i = 0; i < arabicNumber / 10; i++)
                romanEquiv.append('X');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - arabicNumber / 10 * 10);
            countRomanValue(arabicContainer);
        }
    }

    private void appendMax89(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber / 10 == 4) {
            romanEquiv.append('X');
            romanEquiv.append('L');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 40);
            countRomanValue(arabicContainer);
        } else {
            romanEquiv.append('L');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 50);
            countRomanValue(arabicContainer);
        }

    }

    private void appendMax399(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber / 10 == 9) {
            romanEquiv.append('X');
            romanEquiv.append('C');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 90);
            countRomanValue(arabicContainer);
        } else {
            for (int i = 0; i < arabicNumber / 100; i++)
                romanEquiv.append('C');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - arabicNumber / 100 * 100);
            countRomanValue(arabicContainer);
        }
    }

    private void appendMax899(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber / 100 == 4) {
            romanEquiv.append('C');
            romanEquiv.append('D');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 400);
            countRomanValue(arabicContainer);
        } else {
            romanEquiv.append('D');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 500);
            countRomanValue(arabicContainer);
        }
    }

    private void appendMax3999(ArabicContainer arabicContainer) throws BadArabicNumberException {
        StringBuilder romanEquiv = arabicContainer.getRomanEquiv();
        int arabicNumber = arabicContainer.getCurrentArabicNumberValue();
        if (arabicNumber / 100 == 9) {
            romanEquiv.append('C');
            romanEquiv.append('M');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - 900);
            countRomanValue(arabicContainer);
        } else {
            for (int i = 0; i < arabicNumber / 1000; i++)
                romanEquiv.append('M');
            arabicContainer.setCurrentArabicNumberValue(arabicNumber - arabicNumber / 1000 * 1000);
            countRomanValue(arabicContainer);
        }
    }


}
