package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;
import com.knitml.renderer.plural.PluralRuleFactory;

public class DefaultPluralRuleFactory implements PluralRuleFactory {

	public PluralRule createPluralRule(int pluralRuleCode) {
		try {
			Class pluralClass = Class.forName(this.getClass().getPackage()
					.getName()
					+ ".PluralRule" + pluralRuleCode);
			return (PluralRule) pluralClass.newInstance();
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("No plural rule class for rule code "
					+ pluralRuleCode);
		} catch (InstantiationException ex) {
			throw new RuntimeException("Unexpected exception", ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException("Unexpected exception", ex);
		}
	}

}
