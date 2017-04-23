package com.apodoba.service;

import java.util.HashMap;
import java.util.Map;

import com.apodoba.utils.Utils;

/**
 * This class stores information about how many times word was called
 * 
 * @author arina
 *
 */
public class WordCallFrequencyService {
	
	private Map<String, Integer> wordsWithCounts = new HashMap<String, Integer>();

	private WordCallFrequencyService() {}
	
	public static class WordCallFrequencyHelper{
		private static final WordCallFrequencyService WORD_CALL_FREQUENCY = new WordCallFrequencyService();
	}
	
	public static WordCallFrequencyService getInstance(){
		return WordCallFrequencyHelper.WORD_CALL_FREQUENCY;
	}
	
	public void increaseFrequencyForWord(String word){
		synchronized (wordsWithCounts) {
			Integer currenctFrequency = wordsWithCounts.get(word.toLowerCase());
			Integer increasedFrequency = currenctFrequency != null ? currenctFrequency + Utils.INCREASE_VALUE : Utils.INCREASE_VALUE;
			wordsWithCounts.put(word, increasedFrequency);
		}
	}
	
	public Integer getFrequencyForWord(String word){
		synchronized (wordsWithCounts) {
			Integer frequency = wordsWithCounts.get(word.toLowerCase());
			return frequency != null ? frequency : Utils.DEFAULT_WORD_FREQUENCY;
		}
	}
}
