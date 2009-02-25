/**
 * 
 */
package com.knitml.engine.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.knitml.engine.Stitch;
import com.knitml.engine.common.NeedlesInWrongDirectionException;

/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class FlatNeedleBackwardsTests extends FlatNeedleTests {

	@Override
	public void onSetUp() {
		nextRow();
	}

	@Test(expected = NeedlesInWrongDirectionException.class)
	public void removeNStitchesFromBeginning() throws Exception {
		needle.removeNStitchesFromBeginning(5);
	}

	@Test(expected = NeedlesInWrongDirectionException.class)
	public void removeNStitchesFromEnd() throws Exception {
		needle.removeNStitchesFromEnd(5);
	}

	@Test(expected = NeedlesInWrongDirectionException.class)
	public void addStitchesToBeginning() throws Exception {
		needle.addStitchesToBeginning(null);
	}

	@Test(expected = NeedlesInWrongDirectionException.class)
	public void addStitchesToEnd() throws Exception {
		needle.addStitchesToEnd(null);
	}
	
	@Test
	public void castOnInBackwardsDirection() throws Exception {
		assertTrue(needle.isBeginningOfNeedle());
		knit(5);
		needle.increase(5);
		knit(5);
		assertTrue(needle.isEndOfNeedle());
	}

	@Override
	public void crossStitches() throws Exception {
		knit(3);
		needle.cross(2,3);
		knit(7);
		assertThat(needle.isEndOfNeedle(), is (true));
		
		List<Stitch> stitches = needle.getStitches();
		List<String> stitchNames = new ArrayList<String>(stitches.size());
		for (Stitch stitch : stitches) {
			stitchNames.add(stitch.getId());
		}
		String[] expectedStitchArray = new String[] { "J","I","H","E","D","C","G","F","B","A" };
		assertThat(stitchNames.toArray(new String[0]), is (expectedStitchArray));
	}
	
	
}
