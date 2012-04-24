/**
 * 
 */
package com.knitml.core.model.directions.inline

import static com.knitml.core.common.CrossType.FRONT
import static com.knitml.core.common.LoopToWork.LEADING
import static com.knitml.core.common.LoopToWork.TRAILING
import static org.hamcrest.CoreMatchers.is
import static org.junit.Assert.assertThat

import org.junit.Test

import com.knitml.core.model.operations.inline.CrossStitches
import com.knitml.core.model.operations.inline.Knit
import com.knitml.core.model.operations.inline.OperationGroup
import com.knitml.core.model.operations.inline.Purl

class CanonicalizationTests {

	@Test
	void canonicalizeKnit() {
		def target = new Knit (5, null, null)
		def expected = []
		for (int i=0; i < 5; i++)
			expected << new Knit (null, LEADING, null)
		assertThat target.canonicalize(), is (expected)
	}

	@Test
	void canonicalizeCable() {
		def ops = []
		ops << new CrossStitches (2, FRONT, 2)
		ops << new Knit (2, null, null)
		ops << new Purl (2, null, null)
		def target = new OperationGroup (4, ops)
		
		ops = []
		ops << new CrossStitches (2, FRONT, 2)
		ops << new Knit (null, LEADING, null)
		ops << new Knit (null, LEADING, null)
		ops << new Purl (null, LEADING, null)
		ops << new Purl (null, LEADING, null)
		def expected = new OperationGroup (4, ops)
		def actual = target.canonicalize()
		assertThat actual.size(), is (1) 
		assertThat actual[0], is (expected)
	}
	@Test
	void canonicalizeTwistyCable() {
		def ops = []
		ops << new CrossStitches (1, FRONT, 2)
		ops << new Knit (2, TRAILING, null)
		ops << new Purl (1, null, null)
		def target = new OperationGroup (3, ops)
		
		ops = []
		ops << new CrossStitches (1, FRONT, 2)
		ops << new Knit (null, TRAILING, null)
		ops << new Knit (null, TRAILING, null)
		ops << new Purl (null, LEADING, null)
		def expected = new OperationGroup (3, ops)
		def actual = target.canonicalize()
		assertThat actual.size(), is (1) 
		assertThat actual[0], is (expected)
	}
}
