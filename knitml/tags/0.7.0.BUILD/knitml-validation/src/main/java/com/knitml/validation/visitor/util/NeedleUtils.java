package com.knitml.validation.visitor.util;

import com.knitml.engine.Needle;
import com.knitml.validation.context.KnittingContext;

public class NeedleUtils {
	
	public static Needle lookupNeedle(String idRef, KnittingContext context) {
		if (idRef == null) {
			return null;
		}
		return context.getPatternRepository().getNeedle(idRef);
	}
}
