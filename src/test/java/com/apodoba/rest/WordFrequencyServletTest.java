package com.apodoba.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.apodoba.service.WordFileFrequencyService;
import com.apodoba.utils.Utils;

import junit.framework.TestCase;

public class WordFrequencyServletTest extends TestCase{
	
	private static final Logger LOGGER = Logger.getLogger(WordFileFrequencyService.class);

	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpServletResponse response;
	
	private WordFrequencyServlet wordFrequencyServlet = new WordFrequencyServlet();
	

	@Before
	protected void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		wordFrequencyServlet.init();
	}

	@Test
	public void testWordTest() throws Exception {
		// first call
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		Mockito.when(request.getParameter(Utils.WORD_PARAMETER)).thenReturn("test");
		Mockito.when(request.getMethod()).thenReturn("GET");
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		
		wordFrequencyServlet.service(request, response);
		String jsonResponse = stringWriter.getBuffer().toString().trim();
		stringWriter.close();
		
		LOGGER.info("GET first call, word = 'test': " + jsonResponse);
		
		assertEquals(130, getFileFrequency(jsonResponse));
		assertEquals(0, getCallFrequency(jsonResponse));
		
		// second call
		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		
		wordFrequencyServlet.service(request, response);

		jsonResponse = stringWriter.getBuffer().toString().trim();
		stringWriter.close();
		
		LOGGER.info("GET second call, word = 'test': " + jsonResponse);
		
		assertEquals(130, getFileFrequency(jsonResponse));
		assertEquals(1, getCallFrequency(jsonResponse));

	}
	
	@Test
	public void testWordAnd() throws Exception {
		// first call
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		
		Mockito.when(request.getParameter(Utils.WORD_PARAMETER)).thenReturn("and");
		Mockito.when(request.getMethod()).thenReturn("GET");
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		
		wordFrequencyServlet.service(request, response);
		String jsonResponse = stringWriter.getBuffer().toString().trim();
		stringWriter.close();
		
		LOGGER.info("GET first call, word = 'test': " + jsonResponse);
		
		assertEquals(150504, getFileFrequency(jsonResponse));
		assertEquals(0, getCallFrequency(jsonResponse));
		
		// second call
		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		Mockito.when(response.getWriter()).thenReturn(printWriter);
		
		wordFrequencyServlet.service(request, response);

		jsonResponse = stringWriter.getBuffer().toString().trim();
		stringWriter.close();
		
		LOGGER.info("GET second call, word = 'test': " + jsonResponse);
		
		assertEquals(150504, getFileFrequency(jsonResponse));
		assertEquals(1, getCallFrequency(jsonResponse));

	}

	private int getFileFrequency(String jsonResponse){
		JSONObject responseObj = new JSONObject(jsonResponse);
		return responseObj.getInt(Utils.FILE_FREQUENCY_PARAMETER);
	}
	
	private int getCallFrequency(String jsonResponse){
		JSONObject responseObj = new JSONObject(jsonResponse);
		return responseObj.getInt(Utils.CALL_FREQUENCY_PARAMETER);
	}
}
