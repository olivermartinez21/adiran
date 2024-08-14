/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.utils;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
public class DateManagement {

protected DateManagement() {}
	
	static final long MILLIS_PER_DAY = 24L * 60 * 60 * 1000;
	static final long START = -2208988800000L;
	
	public static String today(String separator) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat formatter;
		switch(separator) {
		case "/":
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			return formatter.format(date);
		case ".":
			formatter = new SimpleDateFormat("dd.MM.yyyy");
			return formatter.format(date);
		case "-":
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			return formatter.format(date);
		default:
			return date.toString();
		}
	}
	
	public static java.sql.Date todayDate() {
		return convert(new Date());
	}
	
	public static java.sql.Date convert(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

	public static java.sql.Timestamp getTodayTimestamp() {
		return convertTimestamp(new Date());
	}
	
	public static java.sql.Timestamp convertTimestamp(java.util.Date uDate) {
		java.sql.Timestamp sTimestamp = new java.sql.Timestamp(uDate.getTime());
        return sTimestamp;
	}
}
