package com.example.converter.model;

public class ArabicContainer {

    private int arabicNumber;
    private  StringBuilder romanEquiv ;

    private int currentArabicNumberValue;

    public ArabicContainer(int arabicNumber) {
        this.arabicNumber = arabicNumber;
        currentArabicNumberValue = arabicNumber;
        romanEquiv = new StringBuilder();
    }

    public int getArabicNumber() {
        return arabicNumber;
    }

    public void setArabicNumber(int arabicNumber) {
        this.arabicNumber = arabicNumber;
    }

    public StringBuilder getRomanEquiv() {
        return romanEquiv;
    }

    public void setRomanEquiv(StringBuilder romanEquiv) {
        this.romanEquiv = romanEquiv;
    }

    public int getCurrentArabicNumberValue() {
        return currentArabicNumberValue;
    }

    public void setCurrentArabicNumberValue(int currentArabicNumberValue) {
        this.currentArabicNumberValue = currentArabicNumberValue;
    }
}
