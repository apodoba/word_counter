package com.slice.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.slice.utils.Utils;

/**
 * This class read resource files, calculate and store words frequency in all files
 * 
 * @author arina
 *
 */
public class WordFileFrequencyService {
	
	private static final Logger LOGGER = Logger.getLogger(WordFileFrequencyService.class);

	private Map<String, Integer> wordsWithFrequency = new HashMap<String, Integer>();

	private WordFileFrequencyService(){
		try {
			
			LOGGER.info("Started to parse resource files");

			ClassLoader classloader = getClass().getClassLoader();
			wordsWithFrequency = Files.list(Paths.get(classloader.getResource("txtfiles").toURI())).parallel()
				.filter(Files::isReadable)
				.map(path -> getFrequencyFromFile(path))
				.parallel()
				// for this particular example foreach merges results faster than reduce 
				.reduce(new HashMap<String, Integer>(), (map1, map2) -> mergeWordFrequencyBetweenFiles(map1, map2));

			LOGGER.info("Finished parsing ");
			
		} catch (URISyntaxException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	/**
	 * Combine word frequency for each file
	 * @param frequencyFromFile
	 */
	private Map<String, Integer> mergeWordFrequencyBetweenFiles(Map<String, Integer> frequencyFromFile1, Map<String, Integer> frequencyFromFile2){
		frequencyFromFile1.forEach((key, value) -> frequencyFromFile2.merge(key, value, (value1, value2) -> value1 + value2));
		return frequencyFromFile2;
	}

	/**
	 * Get word frequency from separate file
	 * @param filePath
	 * @return
	 */
	private Map<String, Integer> getFrequencyFromFile(Path filePath){
		Map<String, Integer> fileWordsFrequency = new HashMap<String, Integer>();
		try {
			Files.readAllLines(filePath).stream()
				.map(String::toLowerCase)
				.map(line -> line.replaceAll("[^\\w\\d\\s]", "").split("\\s"))
				.flatMap(Arrays::stream)
				.filter(word -> !word.isEmpty())
				.forEach(word ->{
					if(!fileWordsFrequency.containsKey(word)){
						fileWordsFrequency.put(word, Utils.INCREASE_VALUE);
					}else {
						fileWordsFrequency.put(word, fileWordsFrequency.get(word) + 1);
					}
				});
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return fileWordsFrequency;
	}

	public static class WordFileFrequencyHelper {
		private static final WordFileFrequencyService WORD_FILE_FREQUENCY = new WordFileFrequencyService();
	}

	public static WordFileFrequencyService getInstance() {
		return WordFileFrequencyHelper.WORD_FILE_FREQUENCY;
	}

	public Integer getFrequencyForWord(String word) {
		Integer frequency = wordsWithFrequency.get(word.toLowerCase());
		return frequency != null ? frequency : Utils.DEFAULT_WORD_FREQUENCY;
	}
	
}
