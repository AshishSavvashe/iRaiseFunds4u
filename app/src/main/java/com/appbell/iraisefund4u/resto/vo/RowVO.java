package com.appbell.iraisefund4u.resto.vo;

import java.util.HashMap;

/**
 * Holder of Row data in map format;
 */

public class RowVO extends HashMap<String, String> {

	public static final long serialVersionUID = 1111111111;

	public void addColVal(String name, String val) {
		put(name, val);
	}
}
