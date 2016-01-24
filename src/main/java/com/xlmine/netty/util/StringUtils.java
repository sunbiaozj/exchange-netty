package com.xlmine.netty.util;

import java.util.UUID;

public class StringUtils {

	/**
	 * 生产唯一的id
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

}
