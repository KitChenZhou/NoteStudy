package com.youli.feiyu.fynotification.utility;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;


public class PreferenceHelper {
	
	private static String IDENTIFY = "com.feiyu.youli.fynotification";

	public static void removeData(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static void putString(String key, String value, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		return sharedPref.getString(key, "");
	}

	public static void putSet(String key, Set<String> values, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putStringSet(key, values);
		editor.commit();
	}

	public static Set<String> getSet(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		return sharedPref.getStringSet(key, new HashSet<String>());
	}

	public static void putInt(String key, int value, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		return sharedPref.getInt(key, 0);
	}

	public static void putBoolean(String key, boolean value, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBoolean(String key, Context context) {
		SharedPreferences sharedPref = context.getSharedPreferences(IDENTIFY, Context.MODE_PRIVATE);
		return sharedPref.getBoolean(key, false);
	}
}
