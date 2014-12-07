package com.phoenix.common;

public class ThisThreadLocal {

	private static ThreadLocal<Object> tl=new ThreadLocal<Object>();
	public static Object get()
	{
		return tl.get();
	}
	public static void set(Object obj){
		tl.set(obj);
	}
}

