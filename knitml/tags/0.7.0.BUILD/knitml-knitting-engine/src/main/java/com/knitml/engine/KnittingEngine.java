/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.knitml.engine;

import java.util.List;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.model.operations.block.CastOn;
import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.core.model.operations.inline.InlinePickUpStitches;
import com.knitml.core.model.operations.inline.MultipleDecrease;
import com.knitml.core.model.operations.inline.Slip;
import com.knitml.engine.common.CannotAdvanceNeedleException;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotWorkThroughMarkerException;
import com.knitml.engine.common.NeedlesInWrongDirectionException;
import com.knitml.engine.common.NoActiveNeedlesException;
import com.knitml.engine.common.NoGapFoundException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.common.NoNeedleImposedException;
import com.knitml.engine.common.NotBetweenRowsException;
import com.knitml.engine.common.NotEndOfRowException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.common.StitchesAlreadyOnNeedleException;
import com.knitml.engine.common.WrongKnittingShapeException;
import com.knitml.engine.common.WrongNeedleTypeException;
import com.knitml.engine.common.WrongNumberOfNeedlesException;
import com.knitml.engine.settings.Direction;

/**
 * <p>
 * Interface which provides a facility for expressing all knitting operations
 * within the KnitML specification. Implementations of this class simulate the
 * work of knitting a project. A knitting engine will change its internal state
 * as operations are performed upon it, will report to the client upon request
 * the current state of the engine, and will throw errors if the client attempts
 * an operation that is physically impossible or not allowed by the
 * specification.
 * </p>
 * 
 * <p>
 * Implementations of this interface may simply be used to validate that a
 * certain series of operations is physically possible, or they may choose to
 * keep track of the series of knitting operations for use with some type of
 * renderer.
 * </p>
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
/**
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public interface KnittingEngine extends Restorable {

	/* Inquiry methods */

	/**
	 * @return the total number of needles currently being used for the project
	 */
	int getNumberOfNeedles();

	/**
	 * @return the total number of rows completed by this engine (since time of
	 *         creation)
	 */
	int getTotalRowsCompleted();

	/**
	 * Returns whether the engine is currently positioned at the beginning of a
	 * row. This is true when the next stitch to work is the first stitch in the
	 * row and {@link #startNewRow()} has been called for this row.
	 * 
	 * @return whether the engine is at the beginning of a row
	 */
	boolean isBeginningOfRow();

	/**
	 * Returns whether the engine is currently positioned at the end of a row.
	 * This is true when there are no more stitches to work in this row but
	 * {@link #endRow()} has not yet been called for this row.
	 * 
	 * @return whether the engine is at the beginning of a row
	 */
	boolean isEndOfRow();

	/**
	 * Returns true if the engine is currently between rows ({@link #endRow()}
	 * has been called but {@link #startNewRow()} has not). If
	 * {@link #castOn(int, boolean))} was called immediately before this method
	 * and it did not count as a row, this method will return true.
	 * 
	 * @return whether the engine is currently between rows.
	 */
	boolean isBetweenRows();

	/**
	 * Returns true if the engine is currently set to knit and/or purl into the
	 * working stitch.
	 */
	boolean isWorkingIntoStitch();

	/**
	 * Returns the total number of stitches currently in this row. This amounts
	 * to a summation of the stitch count on all of the active needles at the
	 * time that this method is called. If the current row has increases or
	 * decreases, this number may not accurately reflect the number of stitches
	 * worked in this row. For instance, if the needles start out with 10
	 * stitches and the upcoming row has 2 decreases in it, this method will
	 * return 10 if called before the decreases are worked, 9 if only one
	 * decrease has been worked, and 8 if both decreases have been worked.
	 * 
	 * @return the total number of stitches in this row
	 */
	int getTotalNumberOfStitchesInRow();

	/**
	 * @return the number of stitches left to work in this row
	 */
	int getStitchesRemainingInRow();

	/**
	 * @return the number of stitches left on the current needle
	 */
	int getTotalNumberOfStitchesOnCurrentNeedle();

	/**
	 * Returns the number of stitches left to work on the current needle. If the
	 * row ends between the current position and the end of the current needle,
	 * this call is equivalent to {@link #getStitchesRemainingInRow()}.
	 * 
	 * @return the number of stitches left to work on this needle
	 */
	int getStitchesRemainingOnCurrentNeedle();

	/**
	 * @return the number of stitches to the next marker (including markers on a
	 *         subsequent needle).
	 * @throws NoMarkerFoundException
	 *             if no marker exists between the current position and the end
	 *             of the row
	 */
	int getStitchesToNextMarker() throws NoMarkerFoundException;

	/**
	 * @return the number of stitches to the next gap (only on the current
	 *         needle)
	 * @throws NoGapFoundException
	 *             if no gap exists between the current position and the end of
	 *             the needle
	 */
	int getStitchesToGap() throws NoGapFoundException;

	/**
	 * Gets the the current direction of the work. If the right side of work is
	 * facing, this should return FORWARDS. If the wrong side of the work is
	 * facing, this should return BACKWARDS. If a row was just completed and a
	 * new row has not been started, this will return the direction of the row
	 * just worked.
	 * 
	 * @return the current direction
	 */
	Direction getDirection();

	/**
	 * @return the current knitting shape of the work
	 */
	KnittingShape getKnittingShape();

	/**
	 * @return the current row number
	 */
	int getCurrentRowNumber();

	Stitch peekAtNextStitch();

	/* Manipulation methods */

	/**
	 * <p>
	 * Resets the current row number to 0. The current row number is incremented
	 * whenever {@link #startNewRow()} is called. Clients can use this number
	 * for rendering purposes and call this method whenever it suits them to
	 * meet their rendering needs.
	 * </p>
	 * 
	 * @throws NotEndOfRowException
	 *             if the engine is not at the end of a row
	 */
	void resetRowNumber() throws NotEndOfRowException;

	/**
	 * Starts a new row.
	 * 
	 * @throws NotEndOfRowException
	 *             if the engine is not at the end of a row
	 * @throws NoActiveNeedlesException
	 *             if there are currently no active needles in the work
	 * @throws NotEnoughStitchesException
	 *             if there are currently zero stitches being worked
	 */
	void startNewRow() throws NotEndOfRowException, NoActiveNeedlesException,
			NotEnoughStitchesException;

	/**
	 * Only for round knitting. Starts a new row which will end "past" where the
	 * end was previously designated. This is the opposite of
	 * {@link #designateEndOfRow()}, which stops the row short of where it was
	 * previously designated.
	 * 
	 * @throws NotEndOfRowException
	 *             if the engine is not at the end of a row
	 * @throws NoActiveNeedlesException
	 *             if there are currently no active needles in the work
	 * @throws NotEnoughStitchesException
	 *             if there are currently zero stitches being worked
	 * @throws WrongKnittingShapeException
	 *             if the engine is not currently working in the round
	 * 
	 */
	void startNewLongRow() throws NotEndOfRowException,
			NoActiveNeedlesException, NotEnoughStitchesException,
			WrongKnittingShapeException;

	/**
	 * Optional call at the end of a row which puts the engine into a state
	 * where {@link #isBetweenRows()} returns true. May only be called if the
	 * engine has the ability to end the row (i.e., there are no more stitches
	 * left in this row).
	 * 
	 * @throws NotEndOfRowException
	 *             if the engine is not at the end of a row
	 * @see {@link #isEndOfRow()}
	 */
	void endRow() throws NotEndOfRowException;

	/**
	 * Switches the knitting direction mid-row. This is different from a turn
	 * that occurs when starting a new row when knitting flat. If the intent is
	 * to start a new row, {@link #startNewRow()} should be used instead (the
	 * work will automatically be turned if knitting flat).
	 * 
	 * Turning and working back in the opposite direction will add a gap where
	 * the work was turned. The gap can then be located by calling
	 * {@link #getStitchesToGap()}.
	 * 
	 * @see Direction
	 * @see KnittingEngine#getStitchesToGap()
	 */
	void turn();

	/**
	 * Terminates the current row and increments the row count. What would have
	 * been the next stitch of that row will now be the first stitch of the new
	 * row.
	 * 
	 * Implementations may require that {@link #arrangeStitchesOnNeedles(int[])}
	 * be called immediately after this method call to define the new needle
	 * configuration. This behavior (as it currently exists) prevents rows from
	 * starting or beginning in the middle of a needle.
	 * 
	 * @throws NeedlesInWrongDirectionException
	 *             if the engine is not currently set for round knitting
	 */
	void designateEndOfRow() throws NeedlesInWrongDirectionException;

	/**
	 * <p>
	 * Moves to the next needle. Must be at the end of a needle to perform this
	 * operation. This acts as more of an assert, for if an operation is
	 * executed on the next stitch and a needle change is required, the engine
	 * will automatically advance to the next needle.
	 * </p>
	 * 
	 * @throws CannotAdvanceNeedleException
	 *             if this operation cannot be performed, either because this is
	 *             the last needle in the row or the work is not at the end of a
	 *             needle
	 */
	void advanceNeedle() throws CannotAdvanceNeedleException;

	/**
	 * Switches to knitting-in-the-round for this row (and all subsequent rows
	 * until {@link #declareFlatKnitting()} is called). Must be between rows
	 * when calling this method. This method has no effect if the work is
	 * already being knitted in the round.
	 * 
	 * @throws NotBetweenRowsException
	 *             if the engine is not at the beginning or end of a row
	 * @throws WrongNeedleTypeException
	 *             if the needles currently being used do not support round
	 *             knitting
	 */
	void declareRoundKnitting() throws WrongNeedleTypeException,
			NotBetweenRowsException;

	/**
	 * Switches to flat knitting for this row (and all subsequent rows until
	 * {@link #declareRoundKnitting()} is called). Must be at the beginning of a
	 * row when calling this method. This method has no effect if the work is
	 * already being knitted flat.
	 * 
	 * @param nextRowDirection
	 *            the direction the next row will be knitted (FORWARDS for
	 *            right-side knitting, BACKWARDS for wrong-side knitting)
	 * 
	 * @throws NotBetweenRowsException
	 *             if the engine is not at the beginning or end of a row
	 */
	void declareFlatKnitting(Direction nextRowDirection)
			throws NotBetweenRowsException;

	/**
	 * Arranges the current stitches on the needle(s) into the configuration
	 * provided by stitchArray. For example, an array of [10, 15, 20] would mean
	 * that the stitches should be arranged on the three needles: the first
	 * needle will have 10 stitches, the second 15, and the third 20.
	 * Rearranging always starts at the beginning of the row.
	 * 
	 * The engine must be already configured with the needles before this
	 * operation is attempted, or a {@link WrongNumberOfNeedlesException} will
	 * be thrown.
	 * 
	 * The engine may throw an {@link IllegalStateException} if start of the row
	 * is not at the start of a needle.
	 * 
	 * @param stitchArray
	 *            the target configuration of the stitches on the needles,
	 *            starting at the beginning of the row
	 * @throws WrongNumberOfNeedlesException
	 *             if the number of needles on the engine is not the size of the
	 *             stitchArray parameter
	 * @throws IllegalArgumentException
	 * @throws NotBetweenRowsException
	 * @throws NeedlesInWrongDirectionException
	 */
	void arrangeStitchesOnNeedles(int[] stitchArray)
			throws WrongNumberOfNeedlesException, NotBetweenRowsException,
			IllegalArgumentException, NeedlesInWrongDirectionException;

	/**
	 * Changes the needles being used to the ones provided (in the order
	 * provided) to knit subsequent rows. The new needle setup will be validated
	 * against the engine's current knitting shape, and a
	 * {@link WrongNeedleTypeException} will be thrown if the setup will not
	 * work. For example, you cannot knit-in-the-round with a single-pointed
	 * needle.
	 * 
	 * The work must be right-side facing and at the beginning or end of a row
	 * to perform this operation.
	 * 
	 * @param needlesToUse
	 * @throws NotBetweenRowsException
	 * @throws WrongNeedleTypeException
	 */
	void useNeedles(List<Needle> needlesToUse) throws WrongNeedleTypeException,
			NotBetweenRowsException;

	/**
	 * Changes the needles being used to the ones provided (in the order
	 * provided) to knit subsequent rows. The new needle setup will be validated
	 * against the intended knitting shape, and a
	 * {@link WrongNeedleTypeException} will be thrown if the setup will not
	 * work. For example, you cannot knit-in-the-round with a single-pointed
	 * needle. The work must be right-side facing and at the beginning or end of
	 * a row to perform this operation.
	 * 
	 * The Needle objects passed to the engine from the needlesToUse parameter
	 * may not be copied by-value by the engine, so do not make assumptions that
	 * the actual objects passed in will be the ones used.
	 * 
	 * @param needlesToUse
	 * @param intendedKnittingShape
	 * @throws NotBetweenRowsException
	 * @throws WrongNeedleTypeException
	 */
	void useNeedles(List<Needle> needlesToUse,
			KnittingShape intendedKnittingShape)
			throws WrongNeedleTypeException, NotBetweenRowsException;

	/**
	 * Knits the next stitch.
	 * 
	 * @param throughTrailingLoop
	 *            whether the trailing loop (rather than the leading loop) is
	 *            worked
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void knit(boolean throughTrailingLoop) throws NotEnoughStitchesException;

	/**
	 * Purls the next stitch.
	 * 
	 * @param throughTrailingLoop
	 *            whether the trailing loop (rather than the leading loop) is
	 *            worked
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void purl(boolean throughTrailingLoop) throws NotEnoughStitchesException;

	/**
	 * Fastens off the next n number of stitches (as specified by
	 * numberToFastenOff parameter).
	 * 
	 * @param numberToFastenOff
	 * @throws NotEnoughStitchesException
	 *             if the engine reaches the end of the row before fastening off
	 */
	void fastenOff(int numberToFastenOff) throws NotEnoughStitchesException;

	/**
	 * Fastens off the next stitch.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine reaches the end of the row before fastening off
	 */
	void fastenOff() throws NotEnoughStitchesException;

	/**
	 * Slips the next stitch.
	 * 
	 * @param slipSpecification
	 *            how to slip the specified number of stitches
	 * @throws NotEnoughStitchesException
	 *             if the engine reaches the end of a row before it can complete
	 *             this operation
	 */
	void slip(Slip slipSpecification) throws NotEnoughStitchesException;

	/**
	 * Crosses the specified number of stitches in front of (or behind) the
	 * specified number of following stitches.
	 * 
	 * @param crossStitchesSpecification
	 *            how to slip the specified number of stitches
	 * @throws NotEnoughStitchesException
	 *             if the engine reaches the end of a row before it can complete
	 *             this operation
	 */
	void crossStitches(CrossStitches crossStitchesSpecification)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException;

	/**
	 * Slip the previous stitch back purlwise.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the beginning of a row
	 */
	void reverseSlip() throws NotEnoughStitchesException;

	/**
	 * Slip the previous n number of stitches back purlwise.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the beginning of a row
	 */
	void reverseSlip(int numberToSlip) throws NotEnoughStitchesException;

	/**
	 * Places a marker between the stitch just worked and the next one.
	 * 
	 * @throws CannotPutMarkerOnEndOfNeedleException
	 *             if the engine is at the end of a needle
	 */
	void placeMarker() throws CannotPutMarkerOnEndOfNeedleException;

	/**
	 * Places the marker (defined by the marker parameter) between the stitch
	 * just worked and the next one.
	 * 
	 * @param marker
	 *            the marker to place
	 * @throws CannotPutMarkerOnEndOfNeedleException
	 *             if the engine is at the end of a needle
	 */
	void placeMarker(Marker marker)
			throws CannotPutMarkerOnEndOfNeedleException;

	/**
	 * Removes the marker between the stitch just worked and the next one.
	 * 
	 * @return the marker that was just removed
	 * @throws NoMarkerFoundException
	 *             if no marker exists at the the current position
	 */
	Marker removeMarker() throws NoMarkerFoundException;

	/**
	 * Knits the next two stitches together. Both stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * 
	 * @param throughTrailingLoop
	 *            indicates whether the stitches should be worked through the
	 *            trailing loop
	 * @throws NotEnoughStitchesException
	 *             if there are not at least two stitches left in the row or on
	 *             this needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the two stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void knitTwoTogether(boolean throughTrailingLoop)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException;

	/**
	 * Purls the next two stitches together. Both stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * @param throughTrailingLoop
	 *            indicates whether the stitches should be worked through the
	 *            trailing loop
	 * @throws NotEnoughStitchesException
	 *             if there are not at least two stitches left in the row or on
	 *             this needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the two stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void purlTwoTogether(boolean throughTrailingLoop)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException;

	void increase(Increase increase) throws NotEnoughStitchesException;

	void startWorkingIntoNextStitch() throws NotEnoughStitchesException;

	void endWorkingIntoNextStitch();

	/**
	 * Casts on the specified number of stitches using the parameters specified
	 * in castOn object.
	 * 
	 * @param castOn
	 *            the cast-on specification
	 * @throws StitchesAlreadyOnNeedleException
	 */
	void castOn(CastOn castOn) throws NoActiveNeedlesException,
			StitchesAlreadyOnNeedleException;

	/**
	 * Casts on the specified number of stitches using the parameters specified
	 * in castOn object. Performed while working a row.
	 * 
	 * @param castOn
	 *            the cast-on specification
	 */
	void castOn(InlineCastOn castOn);

	/**
	 * Casts on the specified number of stitches to the current needle. If the
	 * countAsRow parameter is set to true, the row count of the project will be
	 * incremented.
	 * 
	 * @param numberOfStitches
	 *            the number of stitches to cast on
	 * @param countAsRow
	 *            indicates if this cast on adds a row of stitches to the work
	 */
	void castOn(int numberOfStitches, boolean countAsRow)
			throws NoActiveNeedlesException, StitchesAlreadyOnNeedleException;

	/**
	 * Casts on the specified number of stitches to the current needle. This
	 * will not be counted as a row.
	 * 
	 * @param numberOfStitches
	 */
	void castOn(int numberOfStitches) throws NoActiveNeedlesException,
			StitchesAlreadyOnNeedleException;

	/**
	 * Picks up the specified number of stitches using the parameters specified
	 * in the pickUpStitches object.
	 * 
	 * @param pickUpStitches
	 *            the pickUpStitches specification
	 */
	void pickUpStitches(InlinePickUpStitches pickUpStitches);

	/**
	 * Knits the next stitch (through the leading loop).
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void knit() throws NotEnoughStitchesException;

	/**
	 * Knits the next n stitches (through the leading loop).
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void knit(int numberToWork) throws NotEnoughStitchesException;

	/**
	 * Purls the next stitch (through the leading loop).
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void purl() throws NotEnoughStitchesException;

	/**
	 * Purls the next n stitches (through the leading loop).
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void purl(int numberToWork) throws NotEnoughStitchesException;

	/**
	 * Slips the next stitch (purlwise).
	 * 
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void slip() throws NotEnoughStitchesException;

	/**
	 * Slips the next n stitches purlwise (as specified by the numberToWork
	 * parameter).
	 * 
	 * @param numberToWork
	 * @throws NotEnoughStitchesException
	 *             if the engine is at the end of a row
	 */
	void slip(int numberToWork) throws NotEnoughStitchesException;

	/**
	 * Knits the next two stitches together. Both stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if there are not at least two stitches left in the row or on
	 *             this needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the two stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void knitTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	/**
	 * Knits the next three stitches together. All stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if there are not at least three stitches left in the row or
	 *             on this needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the three stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void knitThreeTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	/**
	 * Works the next number of stitches together in one operation and decreases
	 * according to the values of the decrease. All stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if there are not enough stitches left in the row or on this
	 *             needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void decrease(MultipleDecrease decrease) throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	/**
	 * Purls the next two stitches together. Both stitches must be on the same
	 * needle or a {@link NotEnoughStitchesException} will be thrown.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if there are not at least two stitches left in the row or on
	 *             this needle
	 * @throws CannotWorkThroughMarkerException
	 *             if a marker sits between the two stitches to work and the
	 *             marker's behavior prohibits being worked through
	 */
	void purlTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException;

	/**
	 * Increments the current row number.
	 */
	void incrementCurrentRowNumber();

	/**
	 * Passes the previously worked stitch (i.e. 2 stitches ago) over the stitch
	 * which was <i>just</i> worked. The previous stitch can be on a different
	 * needle than the current needle, so long as the current needle is not the
	 * first needle.
	 * 
	 * @throws NotEnoughStitchesException
	 *             if this method is called and there is not a previous stitch
	 */
	void passPreviousStitchOver() throws NotEnoughStitchesException;

	/**
	 * Transfers stitches at the engine cursor (i.e. about to be worked) to the
	 * specified needle.
	 * 
	 * @param targetNeedle
	 * @param numberToTransfer
	 */
	public void transferStitchesToNeedle(Needle targetNeedle,
			int numberToTransfer);

	/**
	 * <p>
	 * Imposes the needle where the engine is currently active (right before the
	 * next stitch on the current needle to be worked). Engine operations will
	 * work the stitches on the imposing needle, one at a time, then
	 * subsequently slip them onto the needle being imposed upon (the one that
	 * was originally active).
	 * 
	 * <p>
	 * When a needle is imposed, the engine considers the imposed needle to be
	 * the current needle. No operations can change the current needle until
	 * {@link #unimposeNeedle()} is called. For instance, if the needle is
	 * imposed in the middle of Needle 1 and there were 5 stitches to be worked
	 * on that needle, those 5 stitches would not be accessible to the engine
	 * until the imposing needle was unimposed. Any attempt to access those
	 * stitches (for instance, to knit past the number of stitches on the
	 * imposing needle), will result in a {@link NotEnoughStitchesException}.
	 * 
	 * <p>
	 * Needle imposition can be used to model knitting from a stitch holder onto
	 * the active needle.
	 * 
	 * @param needleToImpose
	 */
	public void imposeNeedle(Needle needleToImpose);

	/**
	 * Remove the imposition that a needle is currently imposing upon the
	 * engine.
	 * 
	 * @return the previously imposed needle
	 * @throws NoNeedleImposedException
	 *             if no needle is currently imposing the engine
	 */
	public Needle unimposeNeedle() throws NoNeedleImposedException;

	/**
	 * Binds off the specified number of stitches from the work.
	 * 
	 * @param spec
	 *            the specification of how to bind off
	 * @throws NotEnoughStitchesException
	 *             if there are not enough stitches to perform this operation
	 */
	public void bindOff(BindOff spec) throws NotEnoughStitchesException;

	/**
	 * Binds off the rest of the stitches to work in this row.
	 * 
	 * @param spec
	 *            the specification of how to bind off all
	 */
	public void bindOffAll(BindOffAll spec);

}