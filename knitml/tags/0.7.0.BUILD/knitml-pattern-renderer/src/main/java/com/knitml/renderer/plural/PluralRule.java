package com.knitml.renderer.plural;

public interface PluralRule {
	
	int getPluralForm(double numberToExamine);
	String getDescription();
	int getNumberOfForms();
	
}
