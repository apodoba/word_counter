package com.apodoba.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

public class Utils {

	public static final String POST_METHOD = "POST";
	public static final String CALL_FREQUENCY_PARAMETER = "callFrequency";
	public static final String FILE_FREQUENCY_PARAMETER = "fileFrequency";
	public static final String ERROR_RESPONSE = "error";
	public static final String ERROR_MESSAGE = "Parameter 'word' not found";

	public static final String WORD_PARAMETER = "word";
	public static final Integer DEFAULT_WORD_FREQUENCY = 0;
	public static final Integer INCREASE_VALUE = 1;

	public static String getWordFromRequest(HttpServletRequest request) throws IOException {
		String word = request.getParameter("word");
		return word != null ? word : StringUtils.EMPTY;
	}

	public static String getWordFrequencyResponse(int callFrequency, int fileFrequency) throws IOException {
		JSONObject responseObj = new JSONObject();
		responseObj.put(CALL_FREQUENCY_PARAMETER, callFrequency);
		responseObj.put(FILE_FREQUENCY_PARAMETER, fileFrequency);

		return responseObj.toString();
	}

	public static String getErrorResponse() throws IOException {
		JSONObject responseObj = new JSONObject();
		responseObj.put(ERROR_RESPONSE, ERROR_MESSAGE);

		return responseObj.toString();
	}
}
