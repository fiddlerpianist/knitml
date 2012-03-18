package com.knitml.tools.runner;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.context.KnittingContextFactory;
import com.knitml.validation.context.impl.DefaultKnittingContextFactory;

public class ApplicationContextTests extends RunnerTests {

	protected KnittingContextFactory knittingContextFactory = new DefaultKnittingContextFactory();
	
	@Test
	public void verifyCorrectScopeOfBeansInValidationContext() throws Exception {
		KnittingContext knittingContext1 = knittingContextFactory.create();
		KnittingContext knittingContext2 = knittingContextFactory.create();

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
