package com.knitml.renderer.plural.impl;

import com.knitml.renderer.plural.PluralRule;

public class PluralRule0 implements PluralRule {

	public String getDescription() {
		return "Asian (Chinese, Japanese, Korean, Vietnamese), Turkic/Altaic (Turkish), Thai";
	}

	public int getNumberOfForms() {
		return 1;
	}
	
	public int getPluralForm(double numberToExamine) {
		return 0;
	}


}
