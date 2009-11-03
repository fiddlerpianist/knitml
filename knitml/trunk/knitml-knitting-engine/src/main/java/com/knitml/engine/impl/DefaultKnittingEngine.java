package com.knitml.engine.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.NeedleStyle;
import com.knitml.core.common.SlipDirection;
import com.knitml.core.common.Wise;
import com.knitml.core.common.YarnPosition;
import com.knitml.core.model.directions.block.CastOn;
import com.knitml.core.model.directions.inline.BindOff;
import com.knitml.core.model.directions.inline.BindOffAll;
import com.knitml.core.model.directions.inline.CrossStitches;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.core.model.directions.inline.IncreaseIntoNextStitch;
import com.knitml.core.model.directions.inline.InlineCastOn;
import com.knitml.core.model.directions.inline.InlinePickUpStitches;
import com.knitml.core.model.directions.inline.Slip;
import com.knitml.engine.KnittingEngine;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Marker;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.CannotAdvanceNeedleException;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotRetreatNeedleException;
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
import com.knitml.engine.common.WrongNeedleTypeException;
import com.knitml.engine.common.WrongNumberOfNeedlesException;
import com.knitml.engine.settings.Direction;

/**
 * <p>
 * The default implementation of a {@link KnittingEngine}. The needle / stitch
 * construct is delegated to {@link Needle} implementations.
 * 
 * <p>
 * NOTE: adding state to this class (in the form of instance variables) requires
 * that state to be accounted for in {@link DefaultEngineMemento}.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 * 
 */
public class DefaultKnittingEngine implements KnittingEngine {

	private Direction direction = Direction.FORWARDS;
	private KnittingShape knittingShape = KnittingShape.FLAT;
	private int totalRowsCompleted = 0;
	private int currentRowNumber = 0;

	// collections of needles
	private List<Needle> activeNeedles = new ArrayList<Needle>();
	private Set<Needle> allNeedles = new HashSet<Needle>();
	private Needle imposedNeedle = null;

	// start "between" rows
	private int currentNeedleIndex = -1;

	// under certain conditions you don't want to switch directions when a
	// startNewRow() is called.
	// has no effect if knitting in the round, since direction isn't switched
	// for every row.
	private boolean suppressDirectionSwitchingForNextRow = false;
	private boolean castingOn = false;
	private KnittingFactory knittingFactory;

	/**
	 * Indicates the first stitch of a row (when knitting forwards)
	 */
	private final StitchCoordinate STITCH_0_0 = new StitchCoordinate(0, 0);
	private StitchCoordinate startOfRow = STITCH_0_0;

	private static final Logger log = LoggerFactory
			.getLogger(DefaultKnittingEngine.class);

	public DefaultKnittingEngine(KnittingFactory knittingFactory) {
		this.knittingFactory = knittingFactory;
		Needle needle = this.knittingFactory.createNeedle("default",
				NeedleStyle.CIRCULAR);
		activeNeedles.add(needle);
		allNeedles.add(needle);
	}

	public Object save() {
		Map<String, Object> needleMementos = new LinkedHashMap<String, Object>();
		for (Needle needle : this.activeNeedles) {
			Object needleMemento = needle.save();
			needleMementos.put(needle.getId(), needleMemento);
		}
		List<Needle> needlesCopy = new ArrayList<Needle>(this.activeNeedles);
		DefaultEngineMemento engineMemento = new DefaultEngineMemento(
				this.direction, this.knittingShape, this.totalRowsCompleted,
				this.currentRowNumber, needlesCopy, this.allNeedles,
				this.imposedNeedle, needleMementos, this.currentNeedleIndex,
				this.suppressDirectionSwitchingForNextRow, this.castingOn,
				this.startOfRow);
		return engineMemento;
	}

	/**
	 * Be advised that this will only restore the needles which are currently on
	 * the needle. If the engine's memento was saved at a point where more
	 * needles were currently active, the needles which are no longer active
	 * will not be reset. The caller must realize this behavior.
	 * 
	 * @see com.knitml.engine.Restorable#restore(java.lang.Object)
	 */
	public void restore(Object mementoObj) {
		if (!(mementoObj instanceof DefaultEngineMemento)) {
			throw new IllegalArgumentException(
					"Type to restore must be of type DefaultEngineMemento");
		}
		DefaultEngineMemento memento = (DefaultEngineMemento) mementoObj;
		this.direction = memento.getDirection();
		this.knittingShape = memento.getKnittingShape();
		this.totalRowsCompleted = memento.getTotalRowsCompleted();
		this.currentRowNumber = memento.getCurrentRowNumber();
		this.activeNeedles = memento.getActiveNeedles();
		this.allNeedles = memento.getAllNeedles();
		this.imposedNeedle = memento.getImposedNeedle();
		this.currentNeedleIndex = memento.getCurrentNeedleIndex();
		this.suppressDirectionSwitchingForNextRow = memento
				.isSuppressDirectionSwitchingForNextRow();
		this.castingOn = memento.isCastingOn();
		this.startOfRow = memento.getStartOfRow();
		for (Needle needle : this.activeNeedles) {
			Object needleMemento = memento.getNeedleMementos().get(
					needle.getId());
			needle.restore(needleMemento);
		}
	}

	public void useNeedles(List<Needle> newNeedles)
			throws WrongNeedleTypeException, NotBetweenRowsException {
		useNeedles(newNeedles, this.knittingShape);
	}

	public void useNeedles(List<Needle> newNeedles,
			KnittingShape intendedKnittingShape)
			throws WrongNeedleTypeException, NotBetweenRowsException {
		if (!isBetweenRows()) {
			throw new NotBetweenRowsException(
					"Must be between rows to designate new needles to use");
		}
		if (getDirection() == Direction.BACKWARDS) {
			throw new IllegalStateException(
					"Cannot use different needles while the work is being worked on the wrong side (implementation limitation)");
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot use different needles while the engine is being imposed upon by another needle");
		}
		validateNewNeedleConfiguration(newNeedles, intendedKnittingShape);
		// we must define this variable here because we are altering the state
		// of the engine,
		// which could affect the original value returned from isEndOfRow()
		// boolean endOfRow = isEndOfRow();
		doUseNeedles(newNeedles);
		// adjustNeedleAndStitchCursorsToBeginningOrEnd(endOfRow);
	}

	public void transferStitchesToNeedle(Needle targetNeedle,
			int numberToTransfer) {
		if (isBetweenRows()) {
			throw new NotEnoughStitchesException(
					"Must be within a row to transfer active stitches to a needle");
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot transfer stitches to a new needle while the engine is being imposed upon by another needle");
		}
		List<Stitch> stitchesToAdd = new ArrayList<Stitch>();
		for (int i = 0; i < numberToTransfer; i++) {
			advanceNeedleIfNecessary();
			Stitch stitch = getCurrentNeedle().removeNextStitch();
			stitchesToAdd.add(stitch);
		}
		targetNeedle.addStitchesToBeginning(stitchesToAdd);
	}

	private void doUseNeedles(List<Needle> newNeedles) {
		// make sure we account for all needles
		allNeedles.addAll(newNeedles);

		// FIXME the needles should probably be by-value instead of
		// by-reference.
		// The reason is because of the memento concept. If I were to revert
		// back to a previous state, it would only revert the copy of the list,
		// but retain references to the needles
		List<Needle> newNeedleList = new ArrayList<Needle>(newNeedles.size());
		newNeedleList.addAll(newNeedles);
		this.activeNeedles = newNeedleList;
	}

	private void validateNewNeedleConfiguration(List<Needle> newNeedles,
			KnittingShape intendedKnittingShape)
			throws WrongNeedleTypeException, NotBetweenRowsException,
			IllegalArgumentException {
		// perform null checks
		if (newNeedles == null) {
			throw new NullArgumentException("newNeedles");
		}
		if (intendedKnittingShape == null) {
			throw new NullArgumentException("intendedKnittingShape");
		}
		if (!isBetweenRows()) {
			throw new NotBetweenRowsException(
					"Must be at the end or beginning of row");
		}
		if (intendedKnittingShape == KnittingShape.FLAT
				&& newNeedles.size() > 1) {
			log
					.info("A request was made to knit flat using more than one needle");
		}
		int singlePointedCount = 0;
		int dpnCount = 0;
		int circularCount = 0;

		Needle lastNeedle = null;
		for (Needle needle : newNeedles) {
			if (needle == null) {
				throw new NullArgumentException(
						"Each needle in the list of needles provided must not be null");
			}
			NeedleStyle needleType = needle.getNeedleType();
			if (needleType == NeedleStyle.STRAIGHT) {
				singlePointedCount++;
			} else if (needleType == NeedleStyle.DPN) {
				dpnCount++;
			} else if (needleType == NeedleStyle.CIRCULAR) {
				circularCount++;
			} else {
				throw new IllegalArgumentException("NeedleType " + needleType
						+ " not currently supported by this engine");
			}
			lastNeedle = needle;
		}
		if (intendedKnittingShape == KnittingShape.FLAT
				&& singlePointedCount > 0
				&& lastNeedle.getNeedleType() != NeedleStyle.STRAIGHT) {
			throw new WrongNeedleTypeException(
					"Cannot knit with a single-pointed needle between other needles");
		}
		if (singlePointedCount > 1) {
			throw new WrongNeedleTypeException(
					"Cannot knit a row off of more than one single-pointed needle");
		}
		if (singlePointedCount > 0
				&& intendedKnittingShape == KnittingShape.ROUND) {
			throw new WrongNeedleTypeException(
					"Cannot knit in the round with a single-pointed needle");
		}
		if (intendedKnittingShape == KnittingShape.ROUND && circularCount == 0
				&& dpnCount < 2) {
			throw new WrongNeedleTypeException(
					"You need to have at least 2 dpns (or a circular needle) to knit in the round");
		}
	}

	private void adjustNeedleAndStitchCursorsToBeginningOrEnd(boolean endOfRow) {
		for (Needle needle : this.activeNeedles) {
			// adjust stitch cursors to be at appropriate place in row
			if (endOfRow) {
				needle.startAtEnd();
			} else {
				needle.startAtBeginning();
			}
			needle.setDirection(getDirection());
		}
		// adjust current needle to be at appropriate place in row
		if (endOfRow) {
			currentNeedleIndex = activeNeedles.size() - 1;
		} else {
			currentNeedleIndex = 0;
		}
	}

	public void resetRowNumber() throws NotEndOfRowException {
		currentRowNumber = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#startNewRow()
	 */
	public void startNewRow() throws NotEndOfRowException,
			NoActiveNeedlesException, NotEnoughStitchesException {

		if (!hasNeedles()) {
			throw new NoActiveNeedlesException("No needles currently in use");
		}
		if (!isBetweenRows() && !isEndOfRow()) {
			throw new NotEndOfRowException(
					"Cannot start a new row until you reach the end of the current row");
		}
		// if (getTotalNumberOfStitchesInRow() == 0) {
		// throw new NotEnoughStitchesException(
		// "You cannot start a row with 0 stitches in it; use cast-on or pick-up-stitches to add stitches to the work");
		// }
		currentRowNumber++;
		switchDirectionIfNecessary();
		if (isBetweenRows()) {
			resetCurrentNeedleIndex();
		}
		// do this last so that it does not affect the call to isBetweenRows()
		this.castingOn = false;
	}

	private boolean hasNeedles() {
		return activeNeedles.size() > 0;
	}

	public void endRow() throws NotEndOfRowException {
		if (!isEndOfRow()) {
			throw new NotEndOfRowException();
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot end a row while the engine is being imposed upon by another needle");
		}

		totalRowsCompleted++;

		// this is how we indicate we are between rows
		currentNeedleIndex = -1;
	}

	public void incrementCurrentRowNumber() {
		currentRowNumber++;
	}

	private void advanceNeedleIfNecessary() throws CannotAdvanceNeedleException {
		// FIXME remove this when we support row starts not at (0,0)
		if (!startOfRow.equals(STITCH_0_0)) {
			throw new IllegalStateException(
					"The start of the row has been moved from the first stitch of the first needle. "
							+ "You must call arrangeStitchesOnNeedle() before attempting to knit.");
		}
		if (isBetweenRows()) {
			throw new CannotAdvanceNeedleException(
					"The engine is not currently knitting a row, therefore you cannot advance the needle");
		}

		// never advance the needle if imposed upon; always impose upon the
		// needle at currentNeedleIndex
		if (isImposedUpon()) {
			return;
		}

		// if we're not at the end of a row but we're at the end of a needle,
		// advance to the next one
		if (!isEndOfRow() && getCurrentNeedle().isEndOfNeedle()) {
			try {
				if (getDirection() == Direction.FORWARDS) {
					currentNeedleIndex++;
				} else {
					currentNeedleIndex--;
				}
				// assert there is a needle at this index
				getCurrentNeedle();
			} catch (IndexOutOfBoundsException ex) {
				throw new CannotAdvanceNeedleException();
			}
		}
	}

	private void retreatNeedleIfNecessary() throws CannotRetreatNeedleException {
		if (isBetweenRows()) {
			throw new CannotRetreatNeedleException(
					"The engine is not currently knitting a row, therefore you cannot retreat the needle");
		}
		// never retreat the needle if imposed upon; always impose upon the
		// needle at currentNeedleIndex
		if (isImposedUpon()) {
			return;
		}

		// only retreat needle if we're at the beginning of the needle
		if (getCurrentNeedle().isBeginningOfNeedle()) {
			int targetNeedleIndex = this.currentNeedleIndex;
			if (getDirection() == Direction.FORWARDS) {
				targetNeedleIndex--;
			} else {
				targetNeedleIndex++;
			}
			// ensure the targetNeedleIndex is not out-of-bounds
			if (targetNeedleIndex == -1
					|| targetNeedleIndex == this.activeNeedles.size()) {
				throw new CannotRetreatNeedleException();
			}
			this.currentNeedleIndex = targetNeedleIndex;
		}
	}

	private void switchDirectionIfNecessary() {
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot switch direction while the engine is being imposed upon by another needle");
		}

		if (suppressDirectionSwitchingForNextRow) {
			resetStitchCursorOnNeedles();
			suppressDirectionSwitchingForNextRow = false;
			return;
		}

		if (getDirection() == Direction.BACKWARDS) {
			toggleDirection();
		} else if (getDirection() == Direction.FORWARDS
				&& knittingShape == KnittingShape.FLAT) {
			toggleDirection();
		} else {
			// knitting in the round forwards
			currentNeedleIndex = 0;
		}
		resetStitchCursorOnNeedles();
	}

	private void resetStitchCursorOnNeedles() {
		for (Needle needle : activeNeedles) {
			needle.startAtBeginning();
		}
	}

	private void setNeedlesInCurrentDirection() {
		for (Needle needle : activeNeedles) {
			needle.setDirection(getDirection());
		}
	}

	public boolean isBetweenRows() {
		return currentNeedleIndex == -1 || castingOn;
	}

	public boolean isBeginningOfRow() {
		Needle currentNeedle = getCurrentNeedle();
		boolean noStitchesWorked = currentNeedle.getStitchesRemaining() == currentNeedle
				.getTotalStitches();
		if (noStitchesWorked) {
			return isFirstNeedle(currentNeedle);
		}
		return false;
	}

	public boolean isEndOfRow() {
		if (!isStartOfRowFirstStitchOfFirstNeedle()) {
			// FIXME remove when we support row starts mid-needle
			return true;
		}
		if (isBetweenRows()) {
			return false;
		}
		Needle currentNeedle = getCurrentNeedle();
		return isLastNeedle(currentNeedle)
				&& currentNeedle.getStitchesRemaining() == 0;
	}

	private boolean isFirstNeedle(Needle currentNeedle) {
		if (getDirection() == Direction.FORWARDS) {
			return activeNeedles.indexOf(currentNeedle) == 0;
		} else {
			return activeNeedles.indexOf(currentNeedle) == activeNeedles.size() - 1;
		}
	}

	private boolean isLastNeedle(Needle currentNeedle) {
		if (getDirection() == Direction.FORWARDS) {
			return activeNeedles.indexOf(currentNeedle) == activeNeedles.size() - 1;
		} else {
			return activeNeedles.indexOf(currentNeedle) == 0;
		}
	}

	protected Needle getCurrentNeedle() throws NoActiveNeedlesException {
		if (currentNeedleIndex == -1) {
			throw new NoActiveNeedlesException();
		}
		if (isImposedUpon()) {
			return imposedNeedle;
		} else {
			return activeNeedles.get(currentNeedleIndex);
		}
	}

	/**
	 * @see com.knitml.engine.validation.engine.KnittingEngine#turn()
	 */
	public void turn() {
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot turn the work when the engine is being imposed upon by another needle");
		}
		if (getStitchesRemainingOnCurrentNeedle() > 0) {
			getCurrentNeedle().turn();
		}
		toggleDirection();
	}

	/**
	 * TODO While this method more-or-less works, it does not rearrange the
	 * physical stitches that it should. There should be a facility for
	 * rearranging the stitches starting at the start of the row, rather than
	 * just by needle.
	 * 
	 * @see com.knitml.engine.validation.engine.KnittingEngine#designateEndOfRow()
	 */
	public void designateEndOfRow() throws NeedlesInWrongDirectionException {
		// must be knitting with the work facing forwards
		if (!(/* this.shape == KnittingShape.ROUND && */getDirection() == Direction.FORWARDS)) {
			throw new NeedlesInWrongDirectionException(
					"Must be knitting with the right side facing to declare the start of the row");
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot designate the end of a row while the engine is being imposed upon by another needle");
		}

		// This method probably won't be called when the work is already at the
		// beginning or end of a row, but there is nothing prohibiting the
		// caller from doing so, so we have to handle it
		if (isEndOfRow()) {
			// nothing to do here (optimization)
			return;
		} else if (isBeginningOfRow()) {
			// since we're declaring it's the end of a row, move stitch cursors
			// to end
			adjustNeedleAndStitchCursorsToBeginningOrEnd(true);
			return;
		}

		// Now we know that we need to adjust the row start
		int newNeedleIndex = this.currentNeedleIndex;
		int newStitchIndex;
		if (getCurrentNeedle().isEndOfNeedle()) {
			if (newNeedleIndex == this.activeNeedles.size() - 1) {
				newNeedleIndex = 0;
			} else {
				newNeedleIndex++;
			}
			newStitchIndex = 0;
		} else { // not end of the needle
			newStitchIndex = getCurrentNeedle().getNextStitchIndex();
		}

		this.startOfRow = new StitchCoordinate(0, newStitchIndex);

		// this will force the needle with the split end-of-row to be the first
		// needle in the needle list
		if (newNeedleIndex > 0) {
			List<Needle> newNeedleConfiguration = new ArrayList<Needle>();
			for (int i = newNeedleIndex; i < this.activeNeedles.size(); i++) {
				newNeedleConfiguration.add(activeNeedles.get(i));
			}
			for (int i = 0; i < newNeedleIndex; i++) {
				newNeedleConfiguration.add(activeNeedles.get(i));
			}
			// since we don't need validation nor do we want the stitch cursors
			// reset,
			// we call doUseNeedles() directly.
			doUseNeedles(newNeedleConfiguration);
		}
		try {
			this.endRow();
		} catch (NotEndOfRowException ex) {
			throw new RuntimeException("Unexpected exception occurred", ex);
		}
	}

	/**
	 * @see com.knitml.engine.validation.engine.KnittingEngine#advanceNeedle()
	 */
	public void advanceNeedle() throws CannotAdvanceNeedleException {
		if (!getCurrentNeedle().isEndOfNeedle()) {
			throw new CannotAdvanceNeedleException(
					"Must be at the end of needle to advance");
		}
		if (isImposedUpon()) {
			throw new CannotAdvanceNeedleException(
					"Cannot advance past a needle being imposed upon");
		}
		// it will be necessary to advance needle
		advanceNeedleIfNecessary();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#declareRoundKnitting
	 * ()
	 */
	public void declareRoundKnitting() throws WrongNeedleTypeException,
			NotBetweenRowsException, IllegalArgumentException {
		if (knittingShape == KnittingShape.ROUND) {
			log
					.info("Request made to change to round knitting but engine was already knitting round");
			return;
		}
		validateNewNeedleConfiguration(this.activeNeedles, KnittingShape.ROUND);
		// boolean endOfRow = isEndOfRow();

		if (getDirection() == Direction.BACKWARDS) {
			toggleDirection();
			// adjustNeedleAndStitchCursorsToBeginningOrEnd(endOfRow);
		} else {
			// TODO anything needed here for a forwards direction?
		}
		knittingShape = KnittingShape.ROUND;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#declareFlatKnitting
	 * ()
	 */
	public void declareFlatKnitting(Direction nextRowDirection)
			throws NotBetweenRowsException {
		if (knittingShape == KnittingShape.FLAT) {
			log
					.info("Request made to change to flat knitting but engine was already knitting flat");
			return;
		}
		try {
			validateNewNeedleConfiguration(this.activeNeedles,
					KnittingShape.FLAT);
		} catch (WrongNeedleTypeException ex) {
			// we shouldn't get here, since we were previously knitting in the
			// round, and a valid needle configuration in the round
			// should be valid flat
			log
					.warn(
							"An unexpected condition occurred while declaring flat knitting",
							ex);
			throw new IllegalStateException(
					"An unexpected internal error occurred while declaring flat knitting",
					ex);
		}

		knittingShape = KnittingShape.FLAT;
		if (nextRowDirection != null) {
			// toggle the direction from what is specified so that when
			// startNewRow() is called,
			// knitting will occur in the intended direction
			if (nextRowDirection == Direction.FORWARDS) {
				// we want the next row to be in the forward direction as well
				suppressDirectionSwitchingForNextRow = true;
			}
		}
		log.info("Changed engine to flat knitting");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.KnittingEngine#
	 * getTotalNumberOfStitchesInRow()
	 */
	public int getTotalNumberOfStitchesInRow() {
		if (isImposedUpon()) {
			return getTotalNumberOfStitchesOnCurrentNeedle();
		}

		int total = 0;
		for (Needle needle : activeNeedles) {
			total += needle.getTotalStitches();
		}
		return total;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.KnittingEngine#
	 * getStitchesRemainingInRow()
	 */
	public int getStitchesRemainingInRow() {
		if (isImposedUpon()) {
			return getStitchesRemainingOnCurrentNeedle();
		}

		int remaining = 0;
		for (Needle needle : activeNeedles) {
			remaining += needle.getStitchesRemaining();
		}
		return remaining;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.KnittingEngine#
	 * getTotalNumberOfStitchesOnCurrentNeedle()
	 */
	public int getTotalNumberOfStitchesOnCurrentNeedle() {
		return getCurrentNeedle().getTotalStitches();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.KnittingEngine#
	 * getStitchesRemainingOnCurrentNeedle()
	 */
	public int getStitchesRemainingOnCurrentNeedle() {
		return getCurrentNeedle().getStitchesRemaining();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.KnittingEngine#
	 * arrangeStitchesOnNeedles(int[])
	 */
	public void arrangeStitchesOnNeedles(int[] stitchArray)
			throws NotBetweenRowsException, WrongNumberOfNeedlesException,
			IllegalArgumentException {
		if (!isBetweenRows()) {
			throw new NotBetweenRowsException(
					"Must be at the beginning or end of a row to perform this operation");
		}

		Direction originalDirection = getDirection();
		if (originalDirection == Direction.BACKWARDS) {
			toggleDirection();
		}

		assert stitchArray != null;
		if (stitchArray.length != activeNeedles.size()) {
			throw new WrongNumberOfNeedlesException(stitchArray.length,
					activeNeedles.size());
		}
		int totalStitches = 0;
		for (int i = 0; i < stitchArray.length; i++) {
			totalStitches += stitchArray[i];
		}
		if (totalStitches != getTotalNumberOfStitchesInRow()) {
			throw new IllegalArgumentException(
					"The total number of stitches specified by the integer array"
							+ " must match the total number of stitches"
							+ " currently on the needles");
		}

		// end validation section

		// now begin processing
		// FIXME we have to start processing the row at the start of row
		// (defined by StitchCoordinate)
		for (int i = 0; i < stitchArray.length; i++) {
			int stitchesOnCurrentNeedle = activeNeedles.get(i)
					.getTotalStitches();
			int targetNumberOfStitches = stitchArray[i];
			if (targetNumberOfStitches <= 0) {
				throw new IllegalArgumentException(
						"Each needle must contain a positive number of stitches");
			}
			try {
				if (stitchesOnCurrentNeedle > targetNumberOfStitches) {
					transferStitches(i, i + 1, stitchesOnCurrentNeedle
							- targetNumberOfStitches);
				} else if (stitchesOnCurrentNeedle < targetNumberOfStitches) {
					transferStitches(i + 1, i, targetNumberOfStitches
							- stitchesOnCurrentNeedle);
				}
			} catch (NeedlesInWrongDirectionException ex) {
				// should not happen
				throw new RuntimeException("An internal error occurred", ex);
			}
		}
		// FIXME when we support the start of a row not at (0,0)
		this.startOfRow = STITCH_0_0;
		if (originalDirection == Direction.BACKWARDS) {
			toggleDirection();
		}
	}

	private void transferStitches(int fromNeedleIndex, int toNeedleIndex,
			final int numberOfStitchesToTransfer)
			throws NeedlesInWrongDirectionException {
		Needle fromNeedle = activeNeedles.get(fromNeedleIndex);
		Needle toNeedle = activeNeedles.get(toNeedleIndex);
		int numberOfStitchesTransferred = Math.min(numberOfStitchesToTransfer,
				fromNeedle.getTotalStitches());
		if (fromNeedleIndex < toNeedleIndex) {
			List<Stitch> stitchesTransferred = fromNeedle
					.removeNStitchesFromEnd(numberOfStitchesTransferred);
			toNeedle.addStitchesToBeginning(stitchesTransferred);
			// if there are still stitches to transfer, try to get the rest from
			// the previous needle
			if (numberOfStitchesToTransfer > numberOfStitchesTransferred) {
				// recursive call
				// TODO Can we prove that we logically cannot reach this code?
				transferStitches(--fromNeedleIndex, toNeedleIndex,
						numberOfStitchesToTransfer
								- numberOfStitchesTransferred);
			}
		} else {
			assert fromNeedleIndex > toNeedleIndex;
			List<Stitch> stitchesTransferred = fromNeedle
					.removeNStitchesFromBeginning(numberOfStitchesTransferred);
			toNeedle.addStitchesToEnd(stitchesTransferred);
			// if there are still stitches to transfer, try to get the rest from
			// the next needle
			if (numberOfStitchesToTransfer > numberOfStitchesTransferred) {
				// recursive call
				transferStitches(++fromNeedleIndex, toNeedleIndex,
						numberOfStitchesToTransfer
								- numberOfStitchesTransferred);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#getNumberOfNeedles
	 * ()
	 */
	public int getNumberOfNeedles() {
		if (isImposedUpon()) {
			return 1;
		}
		return activeNeedles.size();
	}

	public void fastenOff() throws NotEnoughStitchesException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		getCurrentNeedle().removeNextStitch();
	}

	public void fastenOff(int numberToFastenOff)
			throws NotEnoughStitchesException {
		for (int i = 0; i < numberToFastenOff; i++) {
			fastenOff();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit(boolean)
	 */
	public void knit(boolean throughBackLoop) throws NotEnoughStitchesException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		getCurrentNeedle().knit();
		imposeStitchesIfNecessary(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#purl(boolean)
	 */
	public void purl(boolean throughBackLoop) throws NotEnoughStitchesException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		getCurrentNeedle().purl();
		imposeStitchesIfNecessary(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#slip(boolean)
	 */
	public void slip(Slip slip) throws NotEnoughStitchesException {
		int numberOfTimes = slip.getNumberOfTimes() == null ? 1 : slip
				.getNumberOfTimes();
		if (slip.getDirection() == SlipDirection.REVERSE) {
			reverseSlip(numberOfTimes);
		} else {
			try {
				for (int i = 0; i < numberOfTimes; i++) {
					advanceNeedleIfNecessary();
					getCurrentNeedle().slip();
				}
				imposeStitchesIfNecessary(numberOfTimes);
			} catch (CannotAdvanceNeedleException ex) {
				throw new NotEnoughStitchesException();
			}
		}
	}

	public void reverseSlip() throws NotEnoughStitchesException {
		reverseSlip(1);
	}

	public void reverseSlip(int numberToSlip) throws NotEnoughStitchesException {
		try {
			for (int i = 0; i < numberToSlip; i++) {
				retreatNeedleIfNecessary();
				getCurrentNeedle().reverseSlip();
			}
		} catch (CannotRetreatNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#slip(boolean)
	 */
	public void crossStitches(CrossStitches crossStitches)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		int count = crossStitches.getFirst() + crossStitches.getNext();
		if (count > getStitchesRemainingOnCurrentNeedle()) {
			throw new NotEnoughStitchesException(
					"All stitches involved in a cross operation must be on the same needle, and there are not enough stitches on the current needle to perform the operation");
		}
		getCurrentNeedle().cross(crossStitches.getFirst(),
				crossStitches.getNext());
	}

	private void toggleDirection() {
		if (direction == Direction.FORWARDS) {
			direction = Direction.BACKWARDS;
		} else {
			direction = Direction.FORWARDS;
		}
		setNeedlesInCurrentDirection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#placeMarker()
	 */
	public void placeMarker() throws CannotPutMarkerOnEndOfNeedleException {
		placeMarker(new DefaultMarker());
	}

	public void placeMarker(Marker marker)
			throws CannotPutMarkerOnEndOfNeedleException {
		getCurrentNeedle().placeMarker(marker);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#removeMarker()
	 */
	public Marker removeMarker() throws NoMarkerFoundException {
		return getCurrentNeedle().removeMarker();
	}

	/**
	 * @see com.knitml.validation.validation.engine.KnittingEngine#
	 *      getStitchesToNextMarker()
	 */
	public int getStitchesToNextMarker() throws NoMarkerFoundException {
		if (isImposedUpon()) {
			// only search the imposing needle
			return getCurrentNeedle().getStitchesToNextMarker();
		}

		int inspectedNeedleIndex = currentNeedleIndex;
		int count = 0;
		try {
			while (!(activeNeedles.get(inspectedNeedleIndex))
					.areMarkersRemaining()) {
				count += getCurrentNeedle().getStitchesRemaining();
				if (getDirection() == Direction.FORWARDS) {
					inspectedNeedleIndex++;
				} else {
					inspectedNeedleIndex--;
				}
			}
			return count
					+ activeNeedles.get(inspectedNeedleIndex)
							.getStitchesToNextMarker();
		} catch (IndexOutOfBoundsException ex) {
			throw new NoMarkerFoundException();
		}
	}

	public int getStitchesToGap() throws NoGapFoundException {
		if (isImposedUpon()) {
			// only search the imposing needle
			return getCurrentNeedle().getStitchesToGap();
		}

		int inspectedNeedleIndex = currentNeedleIndex;
		int count = 0;
		try {
			while (!(activeNeedles.get(inspectedNeedleIndex)).hasGaps()) {
				count += getCurrentNeedle().getStitchesRemaining();
				if (getDirection() == Direction.FORWARDS) {
					inspectedNeedleIndex++;
				} else {
					inspectedNeedleIndex--;
				}
			}
			return count
					+ activeNeedles.get(inspectedNeedleIndex)
							.getStitchesToGap();
		} catch (IndexOutOfBoundsException ex) {
			throw new NoGapFoundException();
		}
	}

	private boolean isStartOfRowFirstStitchOfFirstNeedle() {
		return STITCH_0_0.equals(this.startOfRow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#getDirection()
	 */
	public Direction getDirection() {
		return direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#knitTwoTogether
	 * (boolean)
	 */
	public void knitTwoTogether(boolean throughBackLoop)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		prepareCurrentNeedleForDecrease(2);
		getCurrentNeedle().knitTwoTogether();
		imposeStitchesIfNecessary(1);
	}

	private void prepareCurrentNeedleForDecrease(int numberOfStitchesInvolved) {
		int stitchesOnCurrentNeedle = getCurrentNeedle().getStitchesRemaining();
		if (stitchesOnCurrentNeedle < numberOfStitchesInvolved) {
			transferStitches(getNextNeedleIndex(), getCurrentNeedleIndex(),
					numberOfStitchesInvolved - stitchesOnCurrentNeedle);
			// the above operation puts the stitches at the end of the needle. We want
			// the cursor to be where it was right before this operation
			for (int i = 0; i < numberOfStitchesInvolved; i++) {
				getCurrentNeedle().reverseSlip();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#purlTwoTogether
	 * (boolean)
	 */
	public void purlTwoTogether(boolean throughBackLoop)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		prepareCurrentNeedleForDecrease(2);
		// TODO add "through back loop"
		getCurrentNeedle().purlTwoTogether();
		imposeStitchesIfNecessary(1);
	}

	private void resetCurrentNeedleIndex() {
		if (isImposedUpon()) {
			throw new IllegalStateException(
					"Cannot change the needle index while the engine is being imposed upon by another needle");
		}
		if (!castingOn) {
			assert currentNeedleIndex == -1;
		}
		if (getDirection() == Direction.FORWARDS) {
			currentNeedleIndex = 0;
		} else {
			currentNeedleIndex = activeNeedles.size() - 1;
		}
	}

	/**
	 * @see com.knitml.validation.validation.engine.KnittingEngine#castOn(int,
	 *      boolean)
	 */
	public void castOn(CastOn castOn) throws NoActiveNeedlesException,
			StitchesAlreadyOnNeedleException {
		int numberOfStitches = castOn.getNumberOfStitches();
		boolean countAsRow = castOn.isCountAsRow();

		if (!hasNeedles()) {
			throw new NoActiveNeedlesException("No needles currently in use");
		}

		if (getTotalNumberOfStitchesInRow() != 0 && !castingOn) {
			throw new StitchesAlreadyOnNeedleException(
					"You cannot cast on stitches when there are active stitches on the needle. Use inline-cast-on instead to do this within a row.");
		}

		// all cast ons are done in the forwards direction
		direction = Direction.FORWARDS;

		resetCurrentNeedleIndex();
		if (countAsRow) {
			// customized "startNewRow" without resetting anything (because it's
			// already been done)
			currentRowNumber++;
		}

		// we aren't imposed upon here because you can be imposed upon
		// and have your needle index reset
		getCurrentNeedle().increase(numberOfStitches);

		if (countAsRow) {
			try {
				endRow();
			} catch (NotEndOfRowException ex) {
				// this shouldn't happen
				throw new RuntimeException("Unexpected exception occurred: ",
						ex);
			}
		} else {
			// set castingOn to true to indicate we might be adding more
			// stitches as part of this cast on
			castingOn = true;
			// We're not counting this as a row. Therefore we want the cursor to
			// point to the first stitch cast on (in the forwards direction) as
			// the next stitch to work.
			// currentNeedleIndex = -1;
			// And we don't want to switch directions next time startNextRow()
			// is called.
			suppressDirectionSwitchingForNextRow = true;
		}
	}

	public void castOn(int numberOfStitches) throws NoActiveNeedlesException,
			StitchesAlreadyOnNeedleException {
		castOn(numberOfStitches, false);
	}

	public void castOn(int numberOfStitches, boolean countAsRow)
			throws NoActiveNeedlesException, StitchesAlreadyOnNeedleException {
		CastOn castOn = new CastOn();
		castOn.setNumberOfStitches(numberOfStitches);
		castOn.setCountAsRow(countAsRow);
		castOn(castOn);
	}

	public void bindOff(BindOff bindOff) throws NotEnoughStitchesException {
		int numberOfIterations = bindOff.getNumberOfStitches();
		for (int i = 0; i < numberOfIterations; i++) {
			doBindOffOne(bindOff.getType(), i);
		}
		if (getStitchesRemainingInRow() == 0) {
			// last stitch on the row, so fasten it off
			reverseSlip();
			fastenOff();
		} else {
			// the last stitch to bind off
			doBindOffOne(bindOff.getType(), numberOfIterations);
			/*
			 * move one stitch back to the LH needle. This is because, in an
			 * instruction such as "k10, BO 10 sts, k10", the first k of the
			 * last k10 has actually already been worked as part of the bind off
			 * series. We cheat a bit and slip the stitch back onto the LH
			 * needle.
			 */
			reverseSlip();
		}
	}

	public void bindOffAll(BindOffAll bindOffAll) {
		int numberOfIterations = getStitchesRemainingInRow();
		boolean fastenOff = bindOffAll.isFastenOffLastStitch();
		if (numberOfIterations == 0) {
			if (getTotalNumberOfStitchesInRow() > 1) {
				// if we have hit the end of the row, pass the previous stitch
				// over the last stitch worked
				passPreviousStitchOver();
			} else {
				// otherwise there is only one stitch left on the needle, and
				// this MUST mean we should fasten off
				fastenOff = true;
			}
		} else {
			// otherwise knit or purl, binding each successive stitch off
			for (int i = 0; i < numberOfIterations; i++) {
				doBindOffOne(bindOffAll.getType(), i);
			}
		}

		// now fasten last stitch off (if requested)
		if (fastenOff) {
			reverseSlip();
			fastenOff();
		}
	}

	protected void doBindOffOne(Wise wise, int numberPerformed) {
		if (wise == Wise.PURLWISE) {
			if (isImposedUpon()) {
				// don't slip it onto the active needle
				getCurrentNeedle().purl();
			} else {
				purl();
			}
		} else {
			if (isImposedUpon()) {
				getCurrentNeedle().knit();
			} else {
				knit();
			}
		}
		if (numberPerformed > 0) {
			passPreviousStitchOver();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit()
	 */
	public void knit() throws NotEnoughStitchesException {
		knit(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit()
	 */
	public void knit(int numberToWork) throws NotEnoughStitchesException {
		for (int i = 0; i < numberToWork; i++) {
			knit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#purl()
	 */
	public void purl() throws NotEnoughStitchesException {
		purl(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit()
	 */
	public void purl(int numberToWork) throws NotEnoughStitchesException {
		for (int i = 0; i < numberToWork; i++) {
			purl();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#slip()
	 */
	public void slip() throws NotEnoughStitchesException {
		slip(new Slip(1, Wise.PURLWISE, YarnPosition.BACK,
				SlipDirection.FORWARD));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit()
	 */
	public void slip(int numberToWork) throws NotEnoughStitchesException {
		slip(new Slip(numberToWork, Wise.PURLWISE, YarnPosition.BACK,
				SlipDirection.FORWARD));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#knitTwoTogether()
	 */
	public void knitTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		knitTwoTogether(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.KnittingEngine#purlTwoTogether()
	 */
	public void purlTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		purlTwoTogether(false);
	}

	/*
	 */
	public int getCurrentRowNumber() {
		return currentRowNumber;
	}

	public void passPreviousStitchOver() throws NotEnoughStitchesException {
		if (getTotalNumberOfStitchesOnCurrentNeedle()
				- getStitchesRemainingOnCurrentNeedle() == 1
				&& !isFirstNeedle(getCurrentNeedle()) && !isImposedUpon()) {
			// this means there MAY be a previous needle with stitches on it
			if (getDirection() == Direction.FORWARDS) {
				try {
					transferStitches(currentNeedleIndex - 1,
							currentNeedleIndex, 1);
				} catch (NeedlesInWrongDirectionException ex) {
					// shouldn't happen
					throw new RuntimeException("Shouldn't happen", ex);
				}
				// issuing a transferStitches() causes the cursor to move
				// back to the start of the needle. To compensate,
				// we must slip twice to get to where we were.
				slip(2);
			} else {
				// transferStitches() can only be run when needles are in
				// the forwards direction
				toggleDirection();
				try {
					transferStitches(currentNeedleIndex + 1,
							currentNeedleIndex, 1);
				} catch (NeedlesInWrongDirectionException ex) {
					// shouldn't happen
					throw new RuntimeException("Shouldn't happen", ex);
				}
				// issuing a transferStitches() causes the cursor to move
				// back to the start of the needle. To compensate,
				// we must slip twice to get to where we were.
				toggleDirection();
				getCurrentNeedle().startAtBeginning();
				slip(2);
			}
		}

		// everything is on the current needle now, so go ahead and do it
		getCurrentNeedle().passPreviousStitchOver();
	}

	public void knitThreeTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException();
		}
		prepareCurrentNeedleForDecrease(2);
		getCurrentNeedle().knitThreeTogether();
		imposeStitchesIfNecessary(1);
	}

	public int getTotalRowsCompleted() {
		return totalRowsCompleted;
	}

	public KnittingShape getKnittingShape() {
		return knittingShape;
	}

	public void increase(Increase increase) throws NotEnoughStitchesException {
		int numberToIncrease = increase.getNumberOfTimes() == null ? 1
				: increase.getNumberOfTimes();
		doIncrease(numberToIncrease);
		if (increase.getAdvanceCount() > 0) {
			for (int i = 0; i < increase.getAdvanceCount(); i++) {
				slip();
			}
		}
	}

	private void doIncrease(int numberToIncrease)
			throws NotEnoughStitchesException {
		getCurrentNeedle().increase(numberToIncrease);
		imposeStitchesIfNecessary(numberToIncrease);
	}

	public void increase(IncreaseIntoNextStitch increase)
			throws NotEnoughStitchesException {
		doIncrease(increase.getIncreaseCount());
		slip(increase.getAdvanceCount());
	}

	public void pickUpStitches(InlinePickUpStitches pickUpStitches) {
		int numberToIncrease = pickUpStitches.getNumberOfTimes() == null ? 1
				: pickUpStitches.getNumberOfTimes();
		doIncrease(numberToIncrease);
	}

	public void castOn(InlineCastOn castOn) {
		int numberToIncrease = castOn.getNumberOfStitches() == null ? 1
				: castOn.getNumberOfStitches();
		doIncrease(numberToIncrease);
	}

	public void imposeNeedle(Needle needleToImpose) {
		if (this.currentNeedleIndex == -1) {
			throw new NoActiveNeedlesException(
					"Cannot impose a needle when no needles are active");
		}
		if (this.imposedNeedle != null) {
			throw new IllegalStateException(
					"Cannot impose a second needle onto the engine; one has already been imposed");
		}
		imposedNeedle = needleToImpose;
		allNeedles.add(imposedNeedle);
	}

	public Needle unimposeNeedle() throws NoNeedleImposedException {
		if (this.imposedNeedle == null) {
			throw new NoNeedleImposedException();
		}
		Needle result = this.imposedNeedle;
		this.imposedNeedle = null;
		return result;
	}

	protected boolean isImposedUpon() {
		return imposedNeedle != null;
	}

	protected void imposeStitchesIfNecessary(int number) {
		if (isImposedUpon()) {
			Needle targetNeedle = this.activeNeedles
					.get(this.currentNeedleIndex);

			// put the cursor back before the stitches to transfer
			for (int i = 0; i < number; i++) {
				imposedNeedle.reverseSlip();
			}
			for (int i = 0; i < number; i++) {
				Stitch stitch = imposedNeedle.removeNextStitch();
				targetNeedle.addStitch(stitch);
			}
		}
	}

	protected int getCurrentNeedleIndex() {
		return this.currentNeedleIndex;
	}

	protected int getNextNeedleIndex() {
		int candidateIndex;
		if (getDirection() == Direction.FORWARDS) {
			candidateIndex = getCurrentNeedleIndex() + 1;
		} else {
			candidateIndex = getCurrentNeedleIndex() - 1;
		}
		if (candidateIndex < this.activeNeedles.size()) {
			return candidateIndex;
		} else {
			return -1;
		}
	}

}
