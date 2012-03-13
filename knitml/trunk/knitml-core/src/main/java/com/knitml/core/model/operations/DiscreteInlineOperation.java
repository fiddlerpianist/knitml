package com.knitml.core.model.operations;

import java.util.List;

/**
 * An interface which designates that the inline operation knows, at any point
 * of usage, how many stitches it uses on the needle and how many stitches it
 * causes to be increased / decreased.
 * 
 * @author Jonathan Whitall
 * 
 */
public interface DiscreteInlineOperation extends InlineOperation, Canonicalizable {
	int getAdvanceCount();

	int getIncreaseCount();
	
	List<? extends DiscreteInlineOperation> canonicalize();
}
