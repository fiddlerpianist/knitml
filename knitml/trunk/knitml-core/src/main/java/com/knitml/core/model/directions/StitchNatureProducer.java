package com.knitml.core.model.directions;

/**
 * An interface which designates that the inline operation knows, at any point
 * of usage, how many stitches it uses on the needle and how many stitches it
 * causes to be increased / decreased.
 * 
 * @author Jonathan Whitall
 * 
 */
public interface StitchNatureProducer {
	StitchNature getStitchNatureProduced();
}
