package com.monstarbill.master.commons;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.text.WordUtils;

public class CommonUtils {
	
	
	/**
	 * convert camel case text to human readable Name
	 * eg. input - "fieldName" , output - "Field Name" 
	 * @param 
	 * @return
	 */
	public static String splitCamelCaseWithCapitalize(String inputString) {
		return WordUtils.capitalize(inputString.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " "));
	}
	
	/**
	 * mentioned fields has no need to mention in any of history
	 * @return
	 */
	public static List<String> getUnusedFieldsOfHistory() {
		return Arrays.asList("createddate", "createdby", "lastmodifieddate", "lastmodifiedby", "approverPreferenceId", "approverSequenceId", "approverMaxLevel");
	}

}
