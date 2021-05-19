package com.fbn.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;

import com.fbn.api.newgen.controller.Controller;
import org.apache.log4j.Logger;

public class Commons implements ConstantsI{
    public static final Logger logger = LogGen.getLoggerInstance("UtilityLogs");
	public static Set<Map<String,String>> resultSet;
    public static boolean isLeapYear (){
        return LocalDate.now().isLeapYear();
    }
    public static boolean isDateEqual (String date1, String date2){
        return LocalDate.parse(date1).isEqual(LocalDate.parse(date2));
    }
    public static boolean checkClosedDate(String date){
        return LocalDateTime.now().isAfter(LocalDateTime.parse(date,DateTimeFormatter.ofPattern(dbDateTimeFormat)));       
    }
    public static boolean isMatured(String date){
        return LocalDate.now().isEqual(LocalDate.parse(date)) || LocalDate.now().isAfter(LocalDate.parse(date));
    }
    public static boolean isEmpty(String data){
    	return data.isEmpty();
    }
    public static boolean is7DaysToMaturity(String date){
        return ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(date)) == 7;
    }
    public static boolean isDaysToMaturity(String date, int days){
        return ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(date)) == days;
    }
    public static String monthsFromNow(int numberOfMonths){
        return LocalDate.now().minusMonths(numberOfMonths).toString();
    }
    public static String getCurrentDate(){
        return LocalDate.now().toString();
    }
    public static String getCurrentDateTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dbDateTimeFormat));
    }
    public static String getUsersMailsInGroup(String groupName){
        StringBuilder username= new StringBuilder();
        String domain = "@firstbanknigeria.com";
        try {
        	resultSet = new Controller().getRecords(Query.getUsersInGroup(groupName));
        	for (Map<String ,String> result : resultSet) username.append(result.get("USERNAME")).append(domain).append(",");
        }catch (Exception e){
            return empty;
        }
        return username.toString();
    }
  }
