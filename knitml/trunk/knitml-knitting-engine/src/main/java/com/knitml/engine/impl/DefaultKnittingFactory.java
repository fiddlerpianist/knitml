package com.knitml.engine.impl;

import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Marker;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.settings.MarkerBehavior;

public class DefaultKnittingFactory implements KnittingFactory {

	private final static String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; //$NON-NLS-1$
	private int currentCharIndex = 0;
	private int numberOfChars = 1;

	public Needle createNeedle(String name, NeedleStyle needleType) {
		if (needleType == null) {
			needleType = NeedleStyle.STRAIGHT;
		}
		DefaultNeedle needle = new DefaultNeedle(name, needleType, this);
		return needle;
	}

	public Marker createMarker() {
		return new DefaultMarker();
	}
	
	public Marker createMarker(String name, MarkerBehavior markerBehavior) {
		return new DefaultMarker(markerBehavior);
	}

	public Stitch createStitch(String name) {
		return new DefaultStitch(name);
	}

	public Stitch createStitch() {
		char charForName = CHARS.charAt(currentCharIndex % CHARS.length());
		StringBuffer sb = new StringBuffer(numberOfChars);
		for (int i = 0; i < numberOfChars; i++) {
			sb.append(charForName);
		}
		Stitch stitch = new DefaultStitch(sb.toString());
		if (currentCharIndex == CHARS.length() - 1) {
			currentCharIndex = 0;
			numberOfChars++;
		} else {
			currentCharIndex++;
		}
		return stitch;
	}

}
