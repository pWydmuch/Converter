package com.example.konwerter.controller;

import com.example.konwerter.POJO;
import com.example.konwerter.model.KonArabRzym;
import com.example.konwerter.model.KonRzymArab;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class KonwerterController {
    @Autowired
    KonArabRzym konArabRzym;
    @Autowired
    KonRzymArab konRzymArab;
    @GetMapping("/arabska/{rzymska}")
    public int getConverted2(@PathVariable String rzymska){
        rzymska.toUpperCase();
        return konRzymArab.doDziela(rzymska);
    }
    @GetMapping("/rzymska/{arabska}")
    public String getConverted(@PathVariable String arabska){
        int liczba = Integer.parseInt(arabska);
        String rzymska = konArabRzym.getRzymska(liczba);
        return rzymska;
    }



}
