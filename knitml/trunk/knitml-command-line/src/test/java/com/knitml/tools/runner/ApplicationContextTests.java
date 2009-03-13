package com.knitml.tools.runner;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.knitml.validation.context.KnittingContext;

public class ApplicationContextTests extends RunnerTests {

	@Test
	public void verifyCorrectScopeOfBeansInValidationContext() throws Exception {
		KnittingContext knittingContext1 = knittingContextFactory.createKnittingContext();
		KnittingContext knittingContext2 = knittingContextFactory.createKnittingContext();

		// these are prototype beans
		assertThat(knittingContext1, not(knittingContext2));
		assertThat(knittingContext1.getEngine(), not(knittingContext2
				.getEngine()));
		assertThat(knittingContext1.getPatternRepository(),
				not(knittingContext2.getPatternRepository()));
		assertThat(knittingContext1.getKnittingFactory(),
				not(knittingContext2.getKnittingFactory()));
	}

}
