package com.knitml.transform.gauge

import static com.knitml.core.units.Units.STITCHES_PER_INCH
import static org.hamcrest.CoreMatchers.is
import static org.hamcrest.CoreMatchers.not
import static org.junit.Assert.assertThat

import javax.measure.Measure

import com.knitml.core.model.operations.block.CastOn
import com.knitml.core.model.operations.block.Row
import com.knitml.transform.gauge.event.TransformHandler
class EqualGaugeTransformerTests extends AbstractTransformerTests {
	
	void setUpGauges(TransformHandler handler) {
		handler.originalGauge = Measure.valueOf(5, STITCHES_PER_INCH)
		handler.targetGauge = Measure.valueOf(5, STITCHES_PER_INCH)
	}
	
	void validate (Row row) {
		assertThat row.information, is (null)
	}

	void validate (CastOn castOn) {
		assertThat castOn.annotation, is (null)
	}

	void validateChange (Row row, int delta) {
		assertThat row.information, is (null)
	}

}