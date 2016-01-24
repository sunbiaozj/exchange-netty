package com.xlmine.netty.util;

import java.util.ResourceBundle;

/**
 * 加载配置文件
 */
public class ResourceHolder {

	private static ResourceBundle resource = ResourceBundle.getBundle("conf");

	public static String getValue(String key) {
		return resource.getString(key);
	}

}
