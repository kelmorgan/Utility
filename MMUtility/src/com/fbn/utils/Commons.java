package com.fbn.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Commons implements ConstantsI{
    public static boolean isLeapYear (){
        return LocalDate.now().isLeapYear();
    }
    public static boolean isDateEqual (String date1, String date2){
        return LocalDate.parse(date1).isEqual(LocalDate.parse(date2));
    }
    public static boolean compareDate(String startDate, String endDate){
        return  LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern(dbDateTimeFormat)).isBefore(LocalDateTime.parse(startDate,DateTimeFormatter.ofPattern(dbDateTimeFormat)));
    }
    public static boolean compareDate(String date){
        return LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern(dbDateTimeFormat))).isAfter(LocalDateTime.parse(date,DateTimeFormatter.ofPattern(dbDateTimeFormat)));
    }
}
