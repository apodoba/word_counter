package com.apodoba.rest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.apodoba.service.WordCallFrequencyService;
import com.apodoba.service.WordFileFrequencyService;
import com.apodoba.utils.Utils;

@WebServlet(urlPatterns = "/*")
public class WordFrequencyServlet extends HttpServlet {

	private WordCallFrequencyService wordCallFrequency;
	private WordFileFrequencyService wordFileFrequency;

	/**
	 * 
	 */
	private static final long serialVersionUID = 8694792019495388528L;

	@Override
	public void init() throws ServletException {
		wordCallFrequency = WordCallFrequencyService.getInstance();
		wordFileFrequency = WordFileFrequencyService.getInstance();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String wordValue = Utils.getWordFromRequest(request);
		if (!StringUtils.isBlank(wordValue)) {
			int callFrequency = wordCallFrequency.getFrequencyForWord(wordValue);
			wordCallFrequency.increaseFrequencyForWord(wordValue);
			int fileFrequency = wordFileFrequency.getFrequencyForWord(wordValue);

			String wordResponse = Utils.getWordFrequencyResponse(callFrequency, fileFrequency);
			sendResponse(response, wordResponse);
		} else {
			sendResponse(response, Utils.getErrorResponse());
		}

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		throw new UnsupportedOperationException("doPost is not supported");
	}

	@Override
	public String getServletName() {
		return WordFrequencyServlet.class.getName();
	}

	private void sendResponse(HttpServletResponse response, String wordResponse) throws IOException {
		response.getWriter().print(wordResponse);
		response.getWriter().flush();
	}

}
