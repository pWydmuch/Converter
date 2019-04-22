package com.example.konwerter.model;

import org.springframework.stereotype.Service;

@Service
public class KonArabRzym {

    private static boolean czyPierwszyObieg; //  potrzebne zeby tylko po wprowadzeniu 0 przy podawaniu liczby
    // robil sie ostatni if w sprawdzwartosc()
    // bez tego po wprowadzeniu np 500 tez sie zrobi, bo po ktoryms wywolaniu
    // sprawdzwartosc()
    // tez bedzie 0

    private static StringBuilder rzymska ;
    private static boolean czyBlad;

    static{
        czyPierwszyObieg = true;
        rzymska = new StringBuilder();
        czyBlad = false;
    }

    private KonArabRzym() {

//        sprawdzWartosc(liczba);
    }

    //------------------------------------------------------------------
    public static boolean getCzyBlad(){
        return czyBlad;
    }
    public  static String getRzymska (int liczba) {

        if(rzymska == null) rzymska = new StringBuilder();
        sprawdzWartosc(liczba);
        String roma = rzymska.toString();
        czyBlad= false;
        czyPierwszyObieg = true;
        rzymska = null;
        return roma;
    }
    //-------------------------------------------------------------------------------
    private static void sprawdzWartosc(int liczba) {

        if (liczba > 0 && liczba <= 3)
            wypiszDo3(liczba);
        else if (liczba > 3 && liczba <= 8)
            wypiszDo8(liczba);
        else if (liczba > 8 && liczba <= 39)
            wypiszDo39(liczba);
        else if (liczba > 39 && liczba <= 89)
            wypiszDo89(liczba);
        else if (liczba > 89 && liczba <= 399)
            wypiszDo399(liczba);
        else if (liczba > 399 && liczba <= 899)
            wypiszDo899(liczba);
        else if (liczba > 899 && liczba <= 3999)
            wypiszDo3999(liczba);
        if (czyPierwszyObieg && liczba == 0 || (liczba < 0 || liczba >= 4000)) {
            czyBlad=true;
        }

    }
    // ******************************************-----WYPISYWACZE----*************************************

    private static void wypiszDo3(int liczba) {
        for (int licznik = 0; licznik < liczba; licznik++)
            drukujI();
        czyPierwszyObieg = false;
    }

    // --------------------------------------------------------------------
    private static void wypiszDo8(int liczba) {
        if (liczba == 4) {
            drukujI();
            drukujV();
        }
        if (liczba >= 5) {
            drukujV();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 5);
        }

    }

    // ------------------------------------------------------------------------
    private static void wypiszDo39(int liczba) {
        if (liczba == 9) {
            drukujI();
            drukujX();
        } else {
            for (int licznik = 0; licznik < liczba / 10; licznik++)
                drukujX();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - liczba / 10 * 10);
        }
    }

    // ---------------------------------------------------------------------------
    private static void wypiszDo89(int liczba) {
        if (liczba / 10 == 4) {
            drukujX();
            drukujL();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 40);
        } else {
            drukujL();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 50);
        }

    }

    // -----------------------------------------------------
    private static void wypiszDo399(int liczba) {
        if (liczba / 10 == 9) {
            drukujX();
            drukujC();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 90);
        } else {
            for (int licznik = 0; licznik < liczba / 100; licznik++)
                drukujC();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - liczba / 100 * 100);
        }
    }

    // ----------------------------------------------------------------------------
    private static void wypiszDo899(int liczba) {
        if (liczba / 100 == 4) {
            drukujC();
            drukujD();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 400);
        } else {
            drukujD();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 500);
        }
    }

    // -------------------------------------------------------------------------------
    private static void wypiszDo3999(int liczba) {
        if (liczba / 100 == 9) {
            drukujC();
            drukujM();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - 900);
        } else {
            for (int licznik = 0; licznik < liczba / 1000; licznik++)
                drukujM();
            czyPierwszyObieg = false;
            sprawdzWartosc(liczba - liczba / 1000 * 1000);
        }
    }

    // *************************************-------DRUKARZE------*******************************************

    private static void drukujI() {
        rzymska.append('I');
    }

    private static void drukujV() {
        rzymska.append('V');
    }

    private static void drukujX() {
        rzymska.append('X');
    }

    private static void drukujL() {
        rzymska.append('L');
    }

    private static void drukujC() {
        rzymska.append('C');
    }

    private static void drukujD() {
        rzymska.append('D');
    }

    private static void drukujM() {
        rzymska.append('M');
    }








}
