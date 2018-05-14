package de.cxp.predict;

import de.cxp.predict.api.PreDictSettings;
import de.cxp.predict.customizing.CommunityCustomization;
import org.junit.Assert;
import org.junit.Test;

public class CompoundSegmentationTest {
	private PreDict newPreDict() {
		return new PreDict(new CommunityCustomization(
				new PreDictSettings()
						.accuracyLevel(PreDict.AccuracyLevel.topHit)
						.deletionWeight(1)
						.transpositionWeight(1)
						.insertionWeight(1)
						.replaceWeight(1)
						.editDistanceMax(2)));
	}

	@Test
	public void sanity() {
		PreDict preDict = newPreDict();
		preDict.indexWord("123");
		preDict.indexWord("abc");

		Assert.assertTrue(preDict.segment("123abc", 2, 10).correctedString.equals("123 abc"));
	}

	@Test
	public void sanity2() {
		PreDict preDict = newPreDict();
		preDict.indexWord("123");
//		preDict.indexWord("abc");

		Assert.assertEquals("123 abc", preDict.segment("123abc", 2, 3).segmentedString);
	}
}
