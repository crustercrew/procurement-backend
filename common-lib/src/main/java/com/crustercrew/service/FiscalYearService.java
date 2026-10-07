package com.crustercrew.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FiscalYearService {
    public Integer getCurrentFiscalYear(){
        return LocalDate.now().getYear();
    }
}
