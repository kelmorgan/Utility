package com.fbn.start;
import com.fbn.api.newgen.CompleteWorkItem;
import com.fbn.api.newgen.CreateWorkItem;
import com.fbn.utils.ConstantsI;



public class startUtility implements ConstantsI{
	
	  public static void main(String[] args) {
	  //	String winame = CreateWorkItem.createWorkItem("<CP_UTILITYFLAG>Y<CP_UTILITYFLAG>");
		  CompleteWorkItem.completeWorkItem(wiName);
	  }
}
