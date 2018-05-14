package de.cxp.predict;

import com.google.common.io.Resources;
import de.cxp.predict.api.PreDictSettings;
import de.cxp.predict.customizing.CommunityCustomization;
import de.cxp.predict.customizing.NoopPreDictCustomizing;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SpellCorrectionTest {

	private PreDict newPreDict() {
		return newPreDict(PreDict.AccuracyLevel.topHit, 2);
	}

	private PreDict newPreDict(PreDict.AccuracyLevel accuracyLevel, int maxDistance) {
		return new PreDict(new CommunityCustomization(
				new PreDictSettings().accuracyLevel(accuracyLevel).editDistanceMax(maxDistance)));
	}

	private void indexMultipleTimes(PreDict preDict, String word, int count) {
		for (int i = 0; i < count; i++) {
			preDict.indexWord(word);
		}
	}

	@Test
	public void sanity() {
		PreDict preDict = newPreDict();
		preDict.indexWord("testword");
		List<String> corrections = preDict.findSimilarWords("teswor");
		Assert.assertEquals("testword", corrections.get(0));
	}

	@Test
	public void sanity2() {
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.topHit, 4);
		preDict.indexWord("tstword");
		preDict.indexWord("test");
		List<String> corrections = preDict.findSimilarWords("tst");
		Assert.assertEquals("test", corrections.get(0));
	}

	@Test
	public void sanity3() {
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.maximum, 2);
		preDict.indexWord("love");
		preDict.indexWord("dove");

		List<String> result = preDict.findSimilarWords("elove");
		Assert.assertTrue(result.contains("love"));
	}

	@Test
	public void inputLessThanDistance() {
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.maximum, 3);
		List<String> spellings = Arrays.asList("love");
		spellings.forEach(preDict::indexWord);

		List<String> result = preDict.findSimilarWords("l");
		Collections.sort(result);
		Collections.sort(spellings);
		Assert.assertEquals(spellings, result);
	}

	@Test
	@Ignore
	public void breakOnTopHit() {
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.fast, 2);
		preDict.indexWord("likoer");
		preDict.indexWord("mixer");

		List<String> result = preDict.findSimilarWords("mikser");
		Assert.assertTrue(result.contains("mixer"));
	}

	@Test
	public void breakOnMaximumAccuracy() {
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.maximum, 2);
		preDict.indexWord("ticaa");
		preDict.indexWord("stick");
		preDict.indexWord("tisch");

		List<String> result = preDict.findSimilarWords("tic");
		Assert.assertTrue(result.contains("ticaa"));
	}

	@Test
	public void prefixLookup() {
		// Really only relevant if prefix compression is implemented
		PreDict preDict = newPreDict(PreDict.AccuracyLevel.maximum, 2);
		indexMultipleTimes(preDict, "pipe", 5);
		indexMultipleTimes(preDict, "pips", 10);

		List<String> result;
		result = preDict.findSimilarWords("pipe");
		Assert.assertEquals(Arrays.asList("pipe", "pips"), result);

		result = preDict.findSimilarWords("pips");
		Assert.assertEquals(Arrays.asList("pips", "pipe"), result);

		result = preDict.findSimilarWords("pip");
		Assert.assertEquals(Arrays.asList("pipe", "pips"), result);
	}
}
