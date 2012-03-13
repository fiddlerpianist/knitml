package com.knitml.engine.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.NullArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.IncreaseType;
import com.knitml.core.common.KnittingShape;
import com.knitml.core.common.NeedleStyle;
import com.knitml.core.common.SlipDirection;
import com.knitml.core.common.Wise;
import com.knitml.core.common.YarnPosition;
import com.knitml.core.model.operations.block.CastOn;
import com.knitml.core.model.operations.inline.BindOff;
import com.knitml.core.model.operations.inline.BindOffAll;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.InlineCastOn;
import com.knitml.core.model.operations.inline.InlinePickUpStitches;
import com.knitml.core.model.operations.inline.Slip;
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
import com.knitml.engine.common.WrongKnittingShapeException;
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
	private boolean longRow = false;
	private boolean workingIntoStitch = false;

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
		Needle needle = this.knittingFactory.createNeedle("default", //$NON-NLS-1$
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
				this.startOfRow, this.longRow, this.workingIntoStitch);
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
					Messages.getString("DefaultKnittingEngine.1")); //$NON-NLS-1$
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
		this.longRow = memento.isLongRow();
		this.workingIntoStitch = memento.isWorkingIntoStitch();
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
					Messages.getString("DefaultKnittingEngine.2")); //$NON-NLS-1$
		}
		if (getDirection() == Direction.BACKWARDS) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.3")); //$NON-NLS-1$
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.4")); //$NON-NLS-1$
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
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.5")); //$NON-NLS-1$
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.6")); //$NON-NLS-1$
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
			throw new NullArgumentException("newNeedles"); //$NON-NLS-1$
		}
		if (intendedKnittingShape == null) {
			throw new NullArgumentException("intendedKnittingShape"); //$NON-NLS-1$
		}
		if (!isBetweenRows()) {
			throw new NotBetweenRowsException(
					Messages.getString("DefaultKnittingEngine.9")); //$NON-NLS-1$
		}
		if (intendedKnittingShape == KnittingShape.FLAT
				&& newNeedles.size() > 1) {
			log
					.info("A request was made to knit flat using more than one needle"); //$NON-NLS-1$
		}
		int singlePointedCount = 0;
		int dpnCount = 0;
		int circularCount = 0;

		Needle lastNeedle = null;
		for (Needle needle : newNeedles) {
			if (needle == null) {
				throw new NullArgumentException(
						Messages.getString("DefaultKnittingEngine.11")); //$NON-NLS-1$
			}
			NeedleStyle needleType = needle.getNeedleType();
			if (needleType == NeedleStyle.STRAIGHT) {
				singlePointedCount++;
			} else if (needleType == NeedleStyle.DPN) {
				dpnCount++;
			} else if (needleType == NeedleStyle.CIRCULAR) {
				circularCount++;
			} else {
				throw new IllegalArgumentException(MessageFormat
						.format(Messages.getString("DefaultKnittingEngine.7"), //$NON-NLS-1$
								needleType));
			}
			lastNeedle = needle;
		}
		if (intendedKnittingShape == KnittingShape.FLAT
				&& singlePointedCount > 0
				&& lastNeedle.getNeedleType() != NeedleStyle.STRAIGHT) {
			throw new WrongNeedleTypeException(
					Messages.getString("DefaultKnittingEngine.14")); //$NON-NLS-1$
		}
		if (singlePointedCount > 1) {
			throw new WrongNeedleTypeException(
					Messages.getString("DefaultKnittingEngine.15")); //$NON-NLS-1$
		}
		if (singlePointedCount > 0
				&& intendedKnittingShape == KnittingShape.ROUND) {
			throw new WrongNeedleTypeException(
					Messages.getString("DefaultKnittingEngine.16")); //$NON-NLS-1$
		}
		if (intendedKnittingShape == KnittingShape.ROUND && circularCount == 0
				&& dpnCount < 2) {
			throw new WrongNeedleTypeException(
					Messages.getString("DefaultKnittingEngine.17")); //$NON-NLS-1$
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
		startNewRow(false);
	}

	public void startNewLongRow() throws NotEndOfRowException,
			NoActiveNeedlesException, NotEnoughStitchesException,
			WrongKnittingShapeException {
		startNewRow(true);
	}

	protected void startNewRow(boolean longRow) throws NotEndOfRowException,
			NoActiveNeedlesException, NotEnoughStitchesException,
			WrongKnittingShapeException {

		if (!hasNeedles()) {
			throw new NoActiveNeedlesException(Messages.getString("DefaultKnittingEngine.18")); //$NON-NLS-1$
		}
		if (!isBetweenRows() && !isEndOfRow()) {
			throw new NotEndOfRowException(
					Messages.getString("DefaultKnittingEngine.19"), getStitchesRemainingInRow()); //$NON-NLS-1$
		}

		if (longRow && getKnittingShape() != KnittingShape.ROUND) {
			throw new WrongKnittingShapeException(
					Messages.getString("DefaultKnittingEngine.20")); //$NON-NLS-1$
		}
		// if (getTotalNumberOfStitchesInRow() == 0) {
		// throw new NotEnoughStitchesException(
		// "You cannot start a row with 0 stitches in it; use cast-on or pick-up-stitches to add stitches to the work");
		// }
		currentRowNumber++;
		this.longRow = longRow;

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
			throw new NotEndOfRowException(
					Messages.getString("DefaultKnittingEngine.19"), getStitchesRemainingInRow()); //$NON-NLS-1$
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.23")); //$NON-NLS-1$
		}

		totalRowsCompleted++;
		longRow = false;

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
					Messages.getString("DefaultKnittingEngine.8")); //$NON-NLS-1$
		}
		if (isBetweenRows()) {
			throw new CannotAdvanceNeedleException(
					Messages.getString("DefaultKnittingEngine.26")); //$NON-NLS-1$
		}

		// never advance the needle if imposed upon; always impose upon the
		// needle at currentNeedleIndex
		if (isImposedUpon()) {
			return;
		}

		// if we are knitting a long row and there aren't any more stitches
		// left,
		// don't attempt to advance the needle. All "extra" stitches will be
		// worked
		// onto the last needle.
		if (isLongRow() && isEndOfRow()) {
			return;
		}

		// if we're not at the end of a row but we're at the end of a needle,
		// advance to the next one
		if (!isEndOfRow() && getCurrentNeedle().isEndOfNeedle()) {
			try {
				currentNeedleIndex = getNextNeedleIndex();
				// assert there is a needle at this index
				getCurrentNeedle();
			} catch (NoActiveNeedlesException ex) {
				throw new CannotAdvanceNeedleException();
			}
		}
	}

	private void retreatNeedleIfNecessary() throws CannotRetreatNeedleException {
		if (isBetweenRows()) {
			throw new CannotRetreatNeedleException(
					Messages.getString("DefaultKnittingEngine.27")); //$NON-NLS-1$
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
					Messages.getString("DefaultKnittingEngine.28")); //$NON-NLS-1$
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
					Messages.getString("DefaultKnittingEngine.29")); //$NON-NLS-1$
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
					Messages.getString("DefaultKnittingEngine.30")); //$NON-NLS-1$
		}
		if (isImposedUpon()) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.31")); //$NON-NLS-1$
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
			throw new RuntimeException(Messages.getString("DefaultKnittingEngine.32"), ex); //$NON-NLS-1$
		}
	}

	/**
	 * @see com.knitml.engine.validation.engine.KnittingEngine#advanceNeedle()
	 */
	public void advanceNeedle() throws CannotAdvanceNeedleException {
		if (!getCurrentNeedle().isEndOfNeedle()) {
			throw new CannotAdvanceNeedleException(
					Messages.getString("DefaultKnittingEngine.33")); //$NON-NLS-1$
		}
		if (isImposedUpon()) {
			throw new CannotAdvanceNeedleException(
					Messages.getString("DefaultKnittingEngine.34")); //$NON-NLS-1$
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
					.info(Messages.getString("DefaultKnittingEngine.35")); //$NON-NLS-1$
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
					.info(Messages.getString("DefaultKnittingEngine.36")); //$NON-NLS-1$
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
							Messages.getString("DefaultKnittingEngine.37"), //$NON-NLS-1$
							ex);
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.38"), //$NON-NLS-1$
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
		log.info(Messages.getString("DefaultKnittingEngine.39")); //$NON-NLS-1$
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
					Messages.getString("DefaultKnittingEngine.40")); //$NON-NLS-1$
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
					Messages.getString("DefaultKnittingEngine.12")); //$NON-NLS-1$
		}

		// end validation section

		// now begin processing
		// FIXME we have to start processing the row at the start of row
		// (defined by StitchCoordinate)
		for (int i = 0; i < stitchArray.length; i++) {
			int stitchesOnCurrentNeedle = activeNeedles.get(i)
					.getTotalStitches();
			int targetNumberOfStitches = stitchArray[i];
			if (targetNumberOfStitches < 0) {
				throw new IllegalArgumentException(
						Messages.getString("DefaultKnittingEngine.44")); //$NON-NLS-1$
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
				throw new RuntimeException(Messages.getString("DefaultKnittingEngine.INTERNAL_ERROR"), ex); //$NON-NLS-1$
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
		// if we are transferring backwards...
		if (fromNeedleIndex < toNeedleIndex && !isLongRow()) {
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
			assert fromNeedleIndex > toNeedleIndex || isLongRow();
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
			throw new NotEnoughStitchesException(1, 0);
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
		if (this.workingIntoStitch) {
			increase(new Increase(1, IncreaseType.M1));
		} else {
			prepareNeedleForOperation(1);
			getCurrentNeedle().knit();
			imposeStitchesIfNecessary(1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#knit(boolean)
	 */
	public Stitch peekAtNextStitch() throws NotEnoughStitchesException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException(1, 0);
		}
		return getCurrentNeedle().peekAtNextStitch();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#purl(boolean)
	 */
	public void purl(boolean throughBackLoop) throws NotEnoughStitchesException {
		if (this.workingIntoStitch) {
			increase(new Increase(1, IncreaseType.M1P));
		} else {
			prepareNeedleForOperation(1);
			getCurrentNeedle().purl();
			imposeStitchesIfNecessary(1);
		}
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
					prepareNeedleForOperation(1);
					getCurrentNeedle().slip();
				}
				imposeStitchesIfNecessary(numberOfTimes);
			} catch (CannotAdvanceNeedleException ex) {
				throw new NotEnoughStitchesException(1, 0);
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
			throw new NotEnoughStitchesException(1, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.KnittingEngine#slip(boolean)
	 */
	public void crossStitches(CrossStitches crossStitches)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		int count = crossStitches.getFirst() + crossStitches.getNext();
		prepareNeedleForOperation(count);
		// if (count > getStitchesRemainingOnCurrentNeedle()) {
		// throw new NotEnoughStitchesException(
		// "All stitches involved in a cross operation must be on the same needle, and there are not enough stitches on the current needle to perform the operation");
		// }
		getCurrentNeedle().cross(crossStitches.getFirst(),
				crossStitches.getNext(), crossStitches.getSkip() == null ? 0 : crossStitches.getSkip());
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
		placeMarker(knittingFactory.createMarker());
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
		prepareNeedleForOperation(2);
		getCurrentNeedle().knitTwoTogether();
		imposeStitchesIfNecessary(1);
	}

	private void prepareNeedleForOperation(int stitchesRequired)
			throws NotEnoughStitchesException {
		try {
			advanceNeedleIfNecessary();
		} catch (CannotAdvanceNeedleException ex) {
			throw new NotEnoughStitchesException(stitchesRequired, 0);
		}

		int stitchesOnCurrentNeedle = getCurrentNeedle().getStitchesRemaining();
		if (stitchesRequired > stitchesOnCurrentNeedle) {
			int nextNeedleIndex = getNextNeedleIndex();
			if (nextNeedleIndex == -1) {
				throw new NotEnoughStitchesException(stitchesRequired, stitchesOnCurrentNeedle);
			}
			transferStitches(nextNeedleIndex, getCurrentNeedleIndex(),
					stitchesRequired - stitchesOnCurrentNeedle);
			// the above operation puts the stitches at the end of the needle.
			// We want the cursor to be where it was right before this operation
			for (int i = 0; i < stitchesRequired; i++) {
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
		prepareNeedleForOperation(2);
		// TODO add "through back loop"
		getCurrentNeedle().purlTwoTogether();
		imposeStitchesIfNecessary(1);
	}

	private void resetCurrentNeedleIndex() {
		if (isImposedUpon()) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.49")); //$NON-NLS-1$
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
			throw new NoActiveNeedlesException(Messages.getString("DefaultKnittingEngine.50")); //$NON-NLS-1$
		}

		if (getTotalNumberOfStitchesInRow() != 0 && !castingOn) {
			throw new StitchesAlreadyOnNeedleException(
					Messages.getString("DefaultKnittingEngine.51")); //$NON-NLS-1$
		}

		// all cast ons are done in the forwards direction
		direction = Direction.FORWARDS;
		setNeedlesInCurrentDirection();
		resetCurrentNeedleIndex();

		if (countAsRow) {
			// customized "startNewRow" without resetting anything (because it's
			// already been done)
			currentRowNumber++;
		}

		// we aren't imposed upon here because you can be imposed upon
		// and have your needle index reset
		// TODO would the StitchNature always be KNIT in this case?
		getCurrentNeedle().increase(new Increase(numberOfStitches));

		if (countAsRow) {
			try {
				endRow();
			} catch (NotEndOfRowException ex) {
				// this shouldn't happen
				throw new RuntimeException(Messages.getString("DefaultKnittingEngine.0"), //$NON-NLS-1$
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
					throw new RuntimeException("Shouldn't happen", ex); //$NON-NLS-1$
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
					throw new RuntimeException("Shouldn't happen", ex); //$NON-NLS-1$
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
		prepareNeedleForOperation(3);
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
		doIncrease(increase);
		if (increase.getAdvanceCount() > 0) {
			for (int i = 0; i < increase.getAdvanceCount(); i++) {
				slip();
			}
		}
	}

	private void doIncrease(Increase increase)
			throws NotEnoughStitchesException {
		getCurrentNeedle().increase(increase);
		imposeStitchesIfNecessary(increase.getNumberOfTimes() == null ? 1
				: increase.getNumberOfTimes());
	}

	public void startWorkingIntoNextStitch() throws NotEnoughStitchesException {
		prepareNeedleForOperation(1);
		getCurrentNeedle().removeNextStitch();
		this.workingIntoStitch = true;
	}

	public void endWorkingIntoNextStitch() {
		this.workingIntoStitch = false;
	}

	public void pickUpStitches(InlinePickUpStitches pickUpStitches) {
		int numberToIncrease = pickUpStitches.getNumberOfTimes() == null ? 1
				: pickUpStitches.getNumberOfTimes();
		doIncrease(new Increase(numberToIncrease));
	}

	public void castOn(InlineCastOn castOn) {
		int numberToIncrease = castOn.getNumberOfStitches() == null ? 1
				: castOn.getNumberOfStitches();
		doIncrease(new Increase(numberToIncrease));
	}

	public void imposeNeedle(Needle needleToImpose) {
		if (this.currentNeedleIndex == -1) {
			throw new NoActiveNeedlesException(
					Messages.getString("DefaultKnittingEngine.55")); //$NON-NLS-1$
		}
		if (this.imposedNeedle != null) {
			throw new IllegalStateException(
					Messages.getString("DefaultKnittingEngine.56")); //$NON-NLS-1$
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
			// if it's past the front end...
			if (candidateIndex >= this.activeNeedles.size()) {
				// if it's a long row, return the 0th needle, else return -1
				// (not found)
				return isLongRow() ? 0 : -1;
			}
		} else { // Direction == BACKWARDS
			// if it's past the back end...
			candidateIndex = getCurrentNeedleIndex() - 1;
			if (candidateIndex == -1) {
				// if it's a long row, return the last needle, else return -1
				// (not found)
				return isLongRow() ? this.activeNeedles.size() - 1 : -1;
			}
		}
		return candidateIndex;
	}

	protected boolean isLongRow() {
		return longRow;
	}

	public boolean isWorkingIntoStitch() {
		return workingIntoStitch;
	}

}
