package com.project.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONUtils {

	public static void main(String[] args) {

		String path = Thread.currentThread().getContextClassLoader().getResource("ZH_dump.json").getFile();
		String data = ReadFile(path);
		System.out.println(data);
	}

	public static String ReadFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
//			FileInputStream fileInputStream = new FileInputStream(Path);
			FileInputStream fileInputStream = new FileInputStream(Path);

			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
}
