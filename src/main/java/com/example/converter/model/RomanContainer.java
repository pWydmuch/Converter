package com.example.converter.model;

public class RomanContainer {

    private String roman;
    private int arabicEquiv;

    public RomanContainer(String roman) {
        this.roman = roman;
    }

    public String getRoman() {
        return roman;
    }

    public void setRoman(String roman) {
        this.roman = roman;
    }

    public int getArabicEquiv() {
        return arabicEquiv;
    }

    public void setArabicEquiv(int arabicEquiv) {
        this.arabicEquiv = arabicEquiv;
    }

}
