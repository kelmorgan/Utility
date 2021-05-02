package com.fbn.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import com.fbn.utils.Query;

import com.fbn.api.newgen.controller.Controller;

public class Commons implements ConstantsI{
	
	private Set<Map<String,String>> resultSet;
	
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
        return LocalDateTime.now().isAfter(LocalDateTime.parse(date,DateTimeFormatter.ofPattern(dbDateTimeFormat)));       
    }
    public static boolean isMatured(String date){
        return LocalDate.now().isEqual(LocalDate.parse(date)) || LocalDate.now().isAfter(LocalDate.parse(date));
    }
    public static boolean isEmpty(String data){
    	return data.equalsIgnoreCase(empty);
    }
    public static boolean is7DaysToMaturity(String date){
        return ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.parse(date)) == 7;
    }
    
    public String getUsersMailsInGroup(String groupName){
        String username= "";
        try {
        	resultSet = new Controller().getRecords(Query.getUsersInGroup(groupName));
        	for (Map<String ,String> result : resultSet){
                username = result.get("USERNAME");
        	}
        }catch (Exception e){
            
            return null;
        }
      
        return username.trim();
    }

  }
