package com.tmm.myre.base.utils;

import java.util.UUID;

public class UuidProvider {

	public static String getUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}
	
}