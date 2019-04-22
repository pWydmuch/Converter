package com.example.konwerter.model;

import org.springframework.stereotype.Service;

@Service
public class KonRzymArab {

    private String rzymska;
    private int liczba;
    private boolean czyNiepoprawneDane;
    private boolean czyBlad;
    //-------------------------------------------------------------------------------
    public KonRzymArab() {
        liczba = 0;
        czyBlad= false;
        czyNiepoprawneDane = false;
        rzymska = null;
    }
    //-------------------------------------------------------------------------------
    public boolean getCzyBlad() {
        return czyBlad;
    }

    public boolean getCzyNiepoprawneDane() {
        return czyNiepoprawneDane;
    }

//    public void setRzymska(String rzymska) {
//        this.rzymska = rzymska;
//    }

    //********************************************----KONWERTER------********************************************
    public int doDziela(String rzymska) {
        this.rzymska = rzymska;
        for (int licznik = 0; licznik < rzymska.length(); licznik++) {
            if (!czyBlad&& !czyNiepoprawneDane)
                oblicz(rzymska.charAt(licznik), licznik);
            else
                break;

        }
        int wynik = liczba;
        liczba = 0;
        return wynik;
    }
    //------------------------------------------------------------------------------------------------------------
    private void oblicz(char litera, int index) {

        if (litera == 'I') {
            if (sprawdzPoprawnosc(3, 6, index) || sprawdzIle(index, 'I')) // sprawdzPoprawnosc() checks in front of which roman numerals this numeral mustn't stand
                // 3 and 6 in arguments field are equivalents of L and M

                czyBlad = true;
            else if (getNextCyfra(index) == 'V' || getNextCyfra(index) == 'X')
                liczba -= 1;
            else
                liczba += 1;
        } else if (litera == 'V') { // Write else if-s because I need an else block at the end of this method which assigns true to czyBlad

            if (sprawdzPoprawnosc(1, 6, index)|| sprawdzIle(index, 'V')) // there's  need to check sprawdzIleRazy() because althought there can't be
                // two or more V there can be for example VIV what's incorrect
                czyBlad = true;
            else
                liczba += 5;
        } else if (litera == 'X') {
            if (sprawdzPoprawnosc(5, 6, index) || sprawdzIle(index, 'X'))
                czyBlad = true;
            if (getNextCyfra(index) == 'L' || getNextCyfra(index) == 'C')
                liczba -= 10;
            else
                liczba += 10;

        } else if (litera == 'L')
            if (sprawdzPoprawnosc(3, 6, index)|| sprawdzIle(index, 'L'))
                czyBlad = true;
            else
                liczba += 50;
        else if (litera == 'C') {
            if (sprawdzIle(index, 'C'))      // there's no need for sprawdzPoprawnosc() because C can stand before every roman numeral including C and so can M
                czyBlad = true;
            if (getNextCyfra(index) == 'D' || getNextCyfra(index) == 'M')
                liczba -= 100;
            else
                liczba += 100;
        } else if (litera == 'D')
            if (sprawdzPoprawnosc(5, 6, index)|| sprawdzIle(index, 'D'))
                czyBlad = true;
            else
                liczba += 500;
        else if (litera == 'M') {
            if (sprawdzIle(index, 'M'))
                czyBlad = true;
            else
                liczba += 1000;
        } else
            czyNiepoprawneDane = true;

    }
    //***********************************************----GETTERY----******************************************************
    private char getPrevCyfra(int index) {
        if ((index - 1) >= 0)
            return rzymska.charAt(index - 1);
        else
            return '0';
    }
    //--------------------------------------------------------------------------------------------------------------
    private char getNextCyfra(int index) {
        if ((index + 1) < rzymska.length())
            return rzymska.charAt(index + 1);
        else
            return '0'; // any char, it won't be considered anyway, it's needed only because this method
        // has to return sth

    }
    //********************************************----SPRAWDZACZE----*********************************************************
    private boolean sprawdzPoprawnosc(int poczatek, int koniec, int index) {
        char[] cyfry = { 'I', 'V', 'X', 'L', 'C', 'D', 'M' };
        for (int licznik = poczatek; licznik <= koniec; licznik++) {
            if (getNextCyfra(index) == cyfry[licznik])
                return true; // sth wrong happened
            //Perhpas  it would look better if method returned true when there's no wrong, but then i would have to change condition statements in if-s which calls the method
        }
        return false; // everything's all right
    }
    //----------------------------------------------------------------------------------------------------------------------------
    private boolean sprawdzIleRazy(int index, char litera) {

        if ((litera == 'I'|| litera == 'X' || litera == 'C' || litera == 'M')
                &&litera == getNextCyfra(index) && litera == getNextCyfra(index + 1) && litera == getNextCyfra(index + 2))// if 4 next numerals are the same
            return true;

        if((litera=='V'|| litera == 'L'|| litera == 'D' )&&litera == getNextCyfra(index+1)) return true; // sprawdzPoprawnosc() exludes possibility of occuring e.g. VV
        // but it doesn't exlude such a situation VIV

        if(litera == 'V'&& getPrevCyfra(index)=='I'&& getNextCyfra(index)=='I') return true; // prevents from situations like these IVI, CDC,XCX,
        if(litera == 'X'&& getPrevCyfra(index)=='I'&& getNextCyfra(index)=='I') return true;
        if(litera == 'L'&& getPrevCyfra(index)=='X'&& getNextCyfra(index)=='X') return true;
        if(litera == 'C'&& getPrevCyfra(index)=='X'&& getNextCyfra(index)=='X') return true;
        if(litera == 'D'&& getPrevCyfra(index)=='C'&& getNextCyfra(index)=='C') return true;
        if(litera == 'M'&& getPrevCyfra(index)=='C'&& getNextCyfra(index)=='C') return true;

        return false;

    }
    //---------------------------------------------------------------------------------------------------------------------
    private boolean sprawdzIlePrzed(int index, char litera) { // checks how many the same numerals are before the cartain numeral, there can't be more than one in a row

        return  (litera == 'V' && getPrevCyfra(index)=='I'&& getPrevCyfra(index-1)=='I') ||
                (litera == 'X' && getPrevCyfra(index)=='I'&& getPrevCyfra(index-1)=='I') ||
                (litera == 'L' && getPrevCyfra(index)=='X'&& getPrevCyfra(index-1)=='X') ||
                (litera == 'C' && getPrevCyfra(index)=='X'&& getPrevCyfra(index-1)=='X') ||
                (litera == 'D' && getPrevCyfra(index)=='C'&& getPrevCyfra(index-1)=='C') ||
                (litera == 'M' && getPrevCyfra(index)=='C'&& getPrevCyfra(index-1)=='C');

//        if(litera == 'V' && getPrevCyfra(index)=='I'&& getPrevCyfra(index-1)=='I') return true;
//        if(litera == 'X' && getPrevCyfra(index)=='I'&& getPrevCyfra(index-1)=='I') return true;
//        if(litera == 'L' && getPrevCyfra(index)=='X'&& getPrevCyfra(index-1)=='X') return true;
//        if(litera == 'C' && getPrevCyfra(index)=='X'&& getPrevCyfra(index-1)=='X') return true;
//        if(litera == 'D' && getPrevCyfra(index)=='C'&& getPrevCyfra(index-1)=='C') return true;
//        if(litera == 'M' && getPrevCyfra(index)=='C'&& getPrevCyfra(index-1)=='C') return true;
//
//        return false;
    }
    //------------------------------------------------------------------------------------------------------------------------
    private boolean sprawdzIle(int index, char litera) { //connects both above methods
        return sprawdzIleRazy(index, litera)|| sprawdzIlePrzed(index, litera);

    }
}





