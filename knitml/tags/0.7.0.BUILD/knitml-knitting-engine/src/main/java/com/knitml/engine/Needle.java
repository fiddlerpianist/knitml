package com.knitml.engine;

import java.util.List;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.operations.StitchNature;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotWorkThroughMarkerException;
import com.knitml.engine.common.NeedlesInWrongDirectionException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.settings.Direction;

/**
 * Models the interactions between the knitter and a particular knitting needle.
 * This class supports all knitting operations. Such operations include casting
 * on, knitting, purling, placing markers, slipping stitches, etc.
 * 
 * This class does not attempt to model the concept of rows, knitting style
 * (i.e. flat or in-the-round), or multiple needles. Such things are handled by
 * a client to this class, such as a
 * {@link com.knitml.engine.validation.engine.KnittingEngine}.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 */
public interface Needle extends Restorable {

	void setDirection(Direction direction);

	void knit() throws NotEnoughStitchesException;

	void purl() throws NotEnoughStitchesException;

	void slip() throws NotEnoughStitchesException;

	void reverseSlip() throws NotEnoughStitchesException;

	void placeMarker(Marker marker)
			throws CannotPutMarkerOnEndOfNeedleException;

	Marker removeMarker() throws NoMarkerFoundException;

	Stitch removeNextStitch() throws NotEnoughStitchesException;

	int getStitchesToGap();

	void turn();

	void knitTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	void increase(Increase increase);

	void increase(int numberToIncrease);

	/**
	 * <p>
	 * Casts the specified number of stitches onto this needle. If the cast on
	 * is a long-tail style cast on, the needle direction should be set to
	 * {@link Direction#FORWARDS} before calling this method. If the cast on is
	 * a knitted-on style cast on, the needle direction should be set to
	 * {@link Direction#BACKWARDS} before calling this method.
	 * </p>
	 * 
	 * <p>
	 * Cast ons which are increases mid-row (a.k.a. "backward loop") are also
	 * supported by this method. The needle direction can be either forwards or
	 * backwards in this case.
	 * </p>
	 * 
	 * @param numberOfStitches
	 *            the number of stitches to cast on
	 */
	// void castOn(int numberOfStitches);
	/**
	 * @return the total number of stitches currently on this needle
	 */
	int getTotalStitches();

	/**
	 * @return the number of stitches remaining to work on this needle
	 */
	int getStitchesRemaining();

	boolean areMarkersRemaining();

	boolean isEndOfNeedle();

	boolean isBeginningOfNeedle();

	void startAtBeginning();

	void startAtEnd();

	/**
	 * The next stitch is defined as the stitch which is the next stitch to be
	 * worked.
	 * 
	 * @return
	 */
	int getNextStitchIndex();

	/**
	 * The previous stitch is defined as the stitch which is the stitch that was
	 * just worked.
	 * 
	 * @return
	 */
	int getPreviousStitchIndex();

	Stitch peekAtNextStitch();

	int getStitchesToNextMarker() throws NoMarkerFoundException;

	void purlTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	void addStitchesToBeginning(List<Stitch> stitchesToAdd);

	void addStitchesToEnd(List<Stitch> stitchesToAdd);

	/**
	 * Adds the given stitch to the working point of the current needle. Meant
	 * primarily as an interface for needle implementations to share stitches
	 * with each other and not meant to be an API.
	 * 
	 * @return
	 */
	void addStitch(Stitch stitchToAdd);

	/**
	 * Returns the current view of stitches on the needle. The view is adjusted
	 * for either forwards or backwards needle direction, so that the sequence
	 * of stitches returned by this method will always be from left to right.
	 * 
	 * @return
	 */
	List<Stitch> getStitches();

	/**
	 * @param number
	 * @return
	 * @throws NeedlesInWrongDirectionException
	 */
	List<Stitch> removeNStitchesFromBeginning(int number)
			throws NeedlesInWrongDirectionException;

	/**
	 * @param number
	 * @return
	 * @throws NeedlesInWrongDirectionException
	 */
	List<Stitch> removeNStitchesFromEnd(int number)
			throws NeedlesInWrongDirectionException;

	boolean hasGaps();

	Direction getDirection();

	NeedleStyle getNeedleType();

	String getId();

	void passPreviousStitchOver() throws NotEnoughStitchesException;

	void knitThreeTogether() throws CannotWorkThroughMarkerException,
			NotEnoughStitchesException;

	void decrease(int numberToDecrease, StitchNature stitchNatureProduced)
			throws CannotWorkThroughMarkerException, NotEnoughStitchesException;

	void cross(int first, int next, int skip)
			throws CannotWorkThroughMarkerException, NotEnoughStitchesException;

}