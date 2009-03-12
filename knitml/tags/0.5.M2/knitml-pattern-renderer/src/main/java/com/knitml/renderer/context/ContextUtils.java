package com.knitml.renderer.context;

import com.knitml.core.model.Identifiable;

public class ContextUtils {
	
	public static String deriveLabel(Identifiable element, PatternRepository repository) {
		return repository.getPatternMessage(element.getMessageKey(), element.getLabel());
	}
	
	public static void setLastExpressedRowNumber(int newRowNumber, RenderingContext context) {
		int oldRowNumber = context.getPatternState().getLastExpressedRowNumber();
		if (newRowNumber < oldRowNumber) {
			context.getPatternRepository().clearLocalInstructions();
		}
		context.getPatternState().setLastExpressedRowNumber(newRowNumber);
	}

	public static void resetLastExpressedRowNumber(RenderingContext context) {
		context.getPatternRepository().clearLocalInstructions();
		context.getPatternState().resetLastExpressedRowNumber();
	}

}
