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
import java.util.stream.Collectors;

public class DamerauLevenshteinTest {

	private static final PreDict preDict = new PreDict(new NoopPreDictCustomizing(new PreDictSettings()
			.accuracyLevel(PreDict.AccuracyLevel.maximum)
			.deletionWeight(1)
			.insertionWeight(1)
			.transpositionWeight(1)
			.replaceWeight(1)));

	@Test
	public void sanity1() {
		Assert.assertEquals(1, preDict.cxpDamerauLevenshtein("ab", "abc"), 0.00001);
		Assert.assertEquals(2, preDict.cxpDamerauLevenshtein("ab", "abcd"), 0.00001);
		Assert.assertEquals(2, preDict.cxpDamerauLevenshtein("ab", ""), 0.00001);
		Assert.assertEquals(1, preDict.cxpDamerauLevenshtein("ab", "ba"), 0.00001);
		Assert.assertEquals(2, preDict.cxpDamerauLevenshtein("ab", "bac"), 0.00001);
	}

	@Test
	@Ignore
	public void edge1() {
		Assert.assertEquals(3, preDict.cxpDamerauLevenshtein("ab", "bcda"), 0.00001);
	}
}
