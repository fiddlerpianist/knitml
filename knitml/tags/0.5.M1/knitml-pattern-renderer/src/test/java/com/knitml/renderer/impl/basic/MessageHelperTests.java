package com.knitml.renderer.impl.basic;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.StaticMessageSource;

import com.knitml.renderer.impl.basic.MessageHelper;
import com.knitml.renderer.plural.PluralRuleFactory;
import com.knitml.renderer.plural.impl.DefaultPluralRuleFactory;

public class MessageHelperTests {
	
	protected Locale locale = new Locale("en","US");
	protected PluralRuleFactory pluralRuleFactory = new DefaultPluralRuleFactory();
	protected StaticMessageSource messageSource;
	protected MessageHelper classUnderTest;
	
	@Before
	public void setUp() {
		messageSource = new StaticMessageSource();
		messageSource.addMessage("knitml.pattern-renderer.plural-rule", locale, "4");
		classUnderTest = new MessageHelper(messageSource, pluralRuleFactory, locale);
	}
	
	@Test
	public void simplePluralMessage() {
		messageSource.addMessage("row-statement.$$$", locale, "{0} row;{0} rowen;{0} rows");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 1), is("1 row"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 2), is("2 rowen"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 3), is("3 rows"));
	}

	@Test
	public void pluralMessageWithComplexArgs() {
		messageSource.addMessage("row-statement.$$$", locale, "use {0} for {1} row;use {0} for {1} rowen;use {0} for {1} rows");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 1, "thingy", 1), is("use thingy for 1 row"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 2, "thingy", 2), is("use thingy for 2 rowen"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 3, "thingy", 3), is("use thingy for 3 rows"));
	}
	@Test(expected=RuntimeException.class)
	public void pluralMessageWithNotEnoughTokens() {
		messageSource.addMessage("row-statement.$$$", locale, "{0} row;{0} rowen");
		classUnderTest.getPluralizedMessage("row-statement", 3);
	}
	@Test
	public void pluralMessageWithOneToken() {
		messageSource.addMessage("row-statement.$$$", locale, "{0} row(en)(s) for all");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", 3), is("3 row(en)(s) for all"));
	}

	@Test
	public void moreThanOnePluralFormDefaultOrder() {
		messageSource.addMessage("row-statement.$$0", locale, "leftwardly {0} row ;leftwardly {0} rowen ;leftwardly {0} rows ");
		messageSource.addMessage("row-statement.$$1", locale, "with {1} row in the center ;with {1} rowen in the center ;with {1} rows in the center ");
		messageSource.addMessage("row-statement.$$2", locale, "and {2} row to the right;and {2} rowen to the right;and {2} rows to the right");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {3,2,1}), is("leftwardly 3 rows with 2 rowen in the center and 1 row to the right"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {2,1,3}), is("leftwardly 2 rowen with 1 row in the center and 3 rows to the right"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {1,2,3}), is("leftwardly 1 row with 2 rowen in the center and 3 rows to the right"));
	}

	@Test
	public void moreThanOnePluralFormSpecifiedOrder() {
		messageSource.addMessage("row-statement.$$0", locale, "then leftwardly {0} row;then leftwardly {0} rowen;then leftwardly {0} rows");
		messageSource.addMessage("row-statement.$$1", locale, "with {1} row in the center, ;with {1} rowen in the center, ;with {1} rows in the center, ");
		messageSource.addMessage("row-statement.$$2", locale, "To the right, {2} row ;To the right, {2} rowen ;To the right, {2} rows ");
		messageSource.addMessage("row-statement.$$order", locale, "210");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {3,2,1}), is("To the right, 1 row with 2 rowen in the center, then leftwardly 3 rows"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {2,1,3}), is("To the right, 3 rows with 1 row in the center, then leftwardly 2 rowen"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {1,2,3}), is("To the right, 3 rows with 2 rowen in the center, then leftwardly 1 row"));
	}

	@Test
	public void moreThanOnePluralFormAndAdditionalArg() {
		messageSource.addMessage("row-statement.$$0", locale, "Use {0} instruction leftwardly {1} row ;Use {0} instruction leftwardly {1} rowen ;Use {0} instruction leftwardly {1} rows ");
		messageSource.addMessage("row-statement.$$1", locale, "with {2} row in the center ;with {2} rowen in the center ;with {2} rows in the center ");
		messageSource.addMessage("row-statement.$$2", locale, "and {3} row to the right;and {3} rowen to the right;and {3} rows to the right");
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {3,2,1}, "thingy", 3, 2, 1), is("Use thingy instruction leftwardly 3 rows with 2 rowen in the center and 1 row to the right"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {2,1,3}, "thingy", 2, 1, 3), is("Use thingy instruction leftwardly 2 rowen with 1 row in the center and 3 rows to the right"));
		assertThat(classUnderTest.getPluralizedMessage("row-statement", new int[] {1,2,3}, "thingy", 1, 2, 3), is("Use thingy instruction leftwardly 1 row with 2 rowen in the center and 3 rows to the right"));
	}
}
