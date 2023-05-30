package com.tut.idc.library.model;

import com.tut.idc.library.web.exception.IDNumberException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class IDNumber {
    private  Integer century = 19;
    private final Integer year;
    private final Integer month;
    private final Integer day;
    private final Integer citizenship;
    private final Boolean isLeapYear;

    public IDNumber(final String IDNumber) {
        this.year = getYear(IDNumber.substring(0,2));
        this.month = getMonth(IDNumber.substring(2,4));
        this.day = getDay(IDNumber.substring(4,6));
        this.citizenship = getCitizenship(IDNumber.substring(10,11));
        this.isLeapYear = isLeapYear();
    }

    private Integer getYear(final String yearShorthand){
        final Integer yearFirstElement = Integer.valueOf(yearShorthand.substring(0,1));
        final Integer yearSecondElement= Integer.valueOf(yearShorthand.substring(1));

        if (yearFirstElement == 0) {
            if(yearSecondElement > 5) {
                throw new IDNumberException("Year", IDNumberException.NOT_VALID);
            }
            this.century = 20;
        }else if(yearFirstElement < 3 && yearFirstElement > 0){
            throw new IDNumberException("Year", IDNumberException.NOT_VALID);
        }
        return Integer.valueOf(String.valueOf(this.century).concat(yearShorthand));
    }

    private Integer getMonth(final String month) {
        Integer monthValue = Integer.valueOf(month);
        if ( monthValue == 0 || monthValue > 12) {
            throw new IDNumberException("Month", IDNumberException.NOT_VALID);
        }
        return monthValue;
    }

    private Integer getDay(final String day) {
        Integer dayValue = Integer.valueOf(day);
        if ( dayValue == 0 || dayValue > 31) {
            throw new IDNumberException("Month", IDNumberException.NOT_VALID);
        }
        return dayValue;
    }

    private Boolean isLeapYear(){
        LocalDate date = LocalDate.of(year, month, 1);
        if(month == 2){
            if(day > 29){
                throw new IDNumberException("Day", IDNumberException.NOT_VALID);
            } else if(!date.isLeapYear()){
                if(day > 28){
                    throw new IDNumberException(date.getMonth().toString() + " " + day, IDNumberException.NOT_LEAP_YEAR);
                }
            }
        }
        return date.isLeapYear();
    }

    private Integer getCitizenship(final String citizen){
        Integer citizenship = Integer.valueOf(citizen);
        if(citizenship != 0 && citizenship !=1){
            throw new IDNumberException("Invalid ID Number - Verify the third last digit");
        }
        return citizenship;
    }
}
