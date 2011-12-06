package com.knitml.engine.impl;

import static com.knitml.core.model.directions.StitchNature.KNIT;
import static com.knitml.core.model.directions.StitchNature.PURL;
import static com.knitml.core.model.directions.StitchNature.reverse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.knitml.core.common.NeedleStyle;
import com.knitml.core.model.directions.StitchNature;
import com.knitml.core.model.directions.inline.Increase;
import com.knitml.engine.KnittingFactory;
import com.knitml.engine.Marker;
import com.knitml.engine.Needle;
import com.knitml.engine.Stitch;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotWorkThroughMarkerException;
import com.knitml.engine.common.NeedlesInWrongDirectionException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.settings.Direction;
import com.knitml.engine.settings.MarkerBehavior;

public class DefaultNeedle implements Needle {

	private KnittingFactory knittingFactory;

	private static final Logger log = LoggerFactory
			.getLogger(DefaultNeedle.class);

	@Override
	public String toString() {
		return MessageFormat.format(Messages.getString("DefaultNeedle.TO_STRING"), getId()); //$NON-NLS-1$
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof DefaultNeedle
				&& ((DefaultNeedle) obj).getId().equals(this.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	private String id;
	private NeedleStyle needleType;
	private Direction direction = Direction.FORWARDS;
	private List<Stitch> stitches = new LinkedList<Stitch>();
	private ListIterator<Stitch> stitchCursor = stitches.listIterator();
	// the marker falls BEFORE the index specified by its key on the needles
	// (when knitting forward)
	private SortedMap<Integer, Marker> markers = new TreeMap<Integer, Marker>();

	// the gap falls BEFORE the index specified by its key on the needles
	// (when knitting forward)
	private SortedMap<Integer, Marker> gaps = new TreeMap<Integer, Marker>();

	private int lastStitchIndexReturned = -1;

	public DefaultNeedle(String id, NeedleStyle needleType,
			KnittingFactory knittingFactory) {
		this.id = id;
		this.needleType = needleType;
		this.knittingFactory = knittingFactory;
	}

	public void restore(Object mementoObj) {
		if (!(mementoObj instanceof DefaultNeedleMemento)) {
			throw new IllegalArgumentException(
					Messages.getString("DefaultNeedle.WRONG_MEMENTO_TYPE")); //$NON-NLS-1$
		}
		DefaultNeedleMemento memento = (DefaultNeedleMemento) mementoObj;
		this.direction = memento.getDirection();
		this.stitches = memento.getStitches();
		this.stitchCursor = this.stitches.listIterator(memento
				.getNextStitchIndex());
		this.markers = memento.getMarkers();
		this.gaps = memento.getGaps();
		this.lastStitchIndexReturned = memento.getLastStitchIndexReturned();
		for (Stitch stitch : this.stitches) {
			Object stitchMemento = memento.getStitchMementos().get(
					stitch.getId());
			stitch.restore(stitchMemento);
		}
	}

	public Object save() {
		Map<String, Object> stitchMementos = new LinkedHashMap<String, Object>();
		for (Stitch stitch : this.stitches) {
			Object stitchMemento = stitch.save();
			stitchMementos.put(stitch.getId(), stitchMemento);
		}
		List<Stitch> stitchesCopy = new LinkedList<Stitch>(this.stitches);
		SortedMap<Integer, Marker> markersCopy = new TreeMap<Integer, Marker>(
				this.markers);
		SortedMap<Integer, Marker> gapsCopy = new TreeMap<Integer, Marker>(
				this.gaps);
		Object memento = new DefaultNeedleMemento(this.direction, stitchesCopy,
				stitchCursor.nextIndex(), markersCopy, gapsCopy,
				this.lastStitchIndexReturned, stitchMementos);
		return memento;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#setDirection(com
	 * .knitml.validation.validation.engine.Direction)
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#knit()
	 */
	public void knit() throws NotEnoughStitchesException {
		Stitch workedStitch = advanceCursorOne();
		recordKnit(workedStitch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#purl()
	 */
	public void purl() throws NotEnoughStitchesException {
		Stitch workedStitch = advanceCursorOne();
		recordPurl(workedStitch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#slip()
	 */
	public void slip() throws NotEnoughStitchesException {
		advanceCursorOne();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#reverseSlip()
	 */
	public void reverseSlip() throws NotEnoughStitchesException {
		retreatCursorOne();
	}

	public Stitch removeNextStitch() throws NotEnoughStitchesException {
		Stitch stitchToRemove;
		if (direction == Direction.FORWARDS) {
			stitchToRemove = stitchCursor.next();
			stitchCursor.remove();
			lastStitchIndexReturned = stitchCursor.nextIndex() - 1;
		} else {
			stitchToRemove = stitchCursor.previous();
			stitchCursor.remove();
			lastStitchIndexReturned = stitchCursor.previousIndex() + 1;
		}
		adjustMarkersAfterDecrease(1);
		return stitchToRemove;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#placeMarker(com.
	 * knitml.validation.validation.engine.model.Marker)
	 */
	public void placeMarker(Marker marker)
			throws CannotPutMarkerOnEndOfNeedleException {
		placeMarker(marker, this.markers);
	}

	protected void signalGap() {
		try {
			// setting the behavior to REMOVE will take care of any decreases
			placeMarker(
					getKnittingFactory().createMarker(null,
							MarkerBehavior.REMOVE), this.gaps);
		} catch (CannotPutMarkerOnEndOfNeedleException ex) {
			log.warn(Messages
					.getString("DefaultNeedle.IGNORING_REQUEST_TO_SIGNAL_GAP_AT_END")); //$NON-NLS-1$
		}
	}

	private void placeMarker(Marker marker, SortedMap<Integer, Marker> markerMap)
			throws CannotPutMarkerOnEndOfNeedleException {
		if (marker == null) {
			throw new IllegalArgumentException(
					Messages.getString("DefaultNeedle.NO_MARKER_SUPPLIED")); //$NON-NLS-1$
		}
		if (isEndOfNeedle() || isBeginningOfNeedle()) {
			throw new CannotPutMarkerOnEndOfNeedleException();
		}
		if (direction == Direction.FORWARDS) {
			markerMap.put(stitchCursor.nextIndex(), marker);
		} else {
			markerMap.put(stitchCursor.previousIndex() + 1, marker);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#removeMarker()
	 */
	public Marker removeMarker() throws NoMarkerFoundException {
		Marker result = removeMarker(this.markers);
		if (result == null) {
			throw new NoMarkerFoundException();
		}
		return result;
	}

	protected void unsignalGap() {
		removeMarker(this.gaps);
	}

	private Marker removeMarker(SortedMap<Integer, Marker> markerMap) {
		Marker result;
		if (direction == Direction.FORWARDS) {
			result = markerMap.remove(stitchCursor.nextIndex());
		} else {
			result = markerMap.remove(stitchCursor.previousIndex() + 1);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#turn()
	 */
	public void turn() {
		if (getStitchesRemaining() > 0) {
			signalGap();
		}
		if (direction == Direction.BACKWARDS) {
			setDirection(Direction.FORWARDS);
		} else {
			setDirection(Direction.BACKWARDS);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#knitTwoTogether()
	 */
	public void knitTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		Stitch result = doKnitTwoTogether();
		recordKnit(result);
	}

	private Stitch doKnitTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		return decrease(1);
	}

	private Stitch doDecrease(int numberToDecrease, boolean betweenGap)
			throws NotEnoughStitchesException {
		Stitch resultingStitch;

		if (betweenGap) {
			int stitchesToGap = 0;
			while (getStitchesToGap() > 0) {
				slip();
				stitchesToGap++;
			}
			unsignalGap();
			for (int i = 0; i < stitchesToGap; i++) {
				reverseSlip();
			}
			assert numberToDecrease <= stitchesToGap;
		}
		if (getStitchesRemaining() < (numberToDecrease + 1)) {
			throw new NotEnoughStitchesException(
					MessageFormat.format(
							Messages.getString("DefaultNeedle.NOT_ENOUGH_STITCHES_FOR_DECREASE"), //$NON-NLS-1$
							numberToDecrease), numberToDecrease + 1,
					getStitchesRemaining());
		}
		if (direction == Direction.FORWARDS) {
			for (int i = 0; i < numberToDecrease; i++) {
				stitchCursor.next();
				stitchCursor.remove();
			}
			lastStitchIndexReturned = stitchCursor.nextIndex();
			resultingStitch = stitchCursor.next();
		} else {
			for (int i = 0; i < numberToDecrease; i++) {
				stitchCursor.previous();
				stitchCursor.remove();
			}
			lastStitchIndexReturned = stitchCursor.previousIndex();
			resultingStitch = stitchCursor.previous();
		}
		adjustMarkersAfterDecrease(numberToDecrease);
		return resultingStitch;
	}

	private boolean isMarkerBetweenNextNStitches(int numberOfStitches) {
		try {
			return areMarkersRemaining() && getStitchesToNextMarker() >= 1
					&& getStitchesToNextMarker() <= numberOfStitches - 1;
		} catch (NoMarkerFoundException ex) {
			// this should not happen under this scenario
			throw new RuntimeException(
					Messages.getString("DefaultNeedle.INTERNAL_ERROR"), //$NON-NLS-1$
					ex);
		}
	}

	private boolean isGapBetweenNextNStitches(int numberOfStitches) {
		return hasGaps() && getStitchesToGap() >= 1
				&& getStitchesToGap() <= numberOfStitches - 1;
	}

	private Stitch advanceCursorOne() throws NotEnoughStitchesException {
		try {
			if (hasGaps() && getStitchesToGap() == 0) {
				unsignalGap();
			}
			if (direction == Direction.FORWARDS) {
				lastStitchIndexReturned = stitchCursor.nextIndex();
				return stitchCursor.next();
			} else {
				lastStitchIndexReturned = stitchCursor.previousIndex();
				return stitchCursor.previous();
			}
		} catch (NoSuchElementException ex) {
			throw new NotEnoughStitchesException(
					Messages.getString("DefaultNeedle.UNEXPECTED_END_OF_ROW"), 1, 0); //$NON-NLS-1$
		}
	}

	public boolean hasGaps() {
		return gaps.size() > 0;
	}

	public boolean hasMarkers() {
		return markers.size() > 0;
	}

	private Stitch retreatCursorOne() throws NotEnoughStitchesException {
		try {
			if (direction == Direction.FORWARDS) {
				lastStitchIndexReturned = stitchCursor.previousIndex();
				return stitchCursor.previous();
			} else {
				lastStitchIndexReturned = stitchCursor.nextIndex();
				return stitchCursor.next();
			}
		} catch (NoSuchElementException ex) {
			throw new NotEnoughStitchesException(
					Messages.getString("DefaultNeedle.UNEXPECTED_BEGINNING_OF_ROW"), 1, 0); //$NON-NLS-1$
		}
	}

	protected void adjustMarkersAfterDecrease(int numberDecreased) {
		this.markers = adjustMarkersAfterDecrease(numberDecreased, this.markers);
		this.gaps = adjustMarkersAfterDecrease(numberDecreased, this.gaps);
	}

	private SortedMap<Integer, Marker> adjustMarkersAfterDecrease(
			int numberDecreased, SortedMap<Integer, Marker> markerMap) {
		if (markerMap.size() > 0) {
			SortedMap<Integer, Marker> newMarkers = new TreeMap<Integer, Marker>();
			int targetIndex;
			if (getDirection() == Direction.FORWARDS) {
				targetIndex = getNextStitchIndex() + 1;
			} else {
				// add 2 to the next stitch index because of the way markers are
				// represented internally (the index of a marker you arrive at
				// is one higher than the index of the stitch just knit, but
				// only when knitting forwards).
				targetIndex = getNextStitchIndex() + 2;
			}
			newMarkers.putAll(markerMap.headMap(targetIndex));
			for (int index : markerMap.tailMap(targetIndex).keySet()) {
				Marker marker = markerMap.get(index);
				// offset each marker's index by the number that was decreased
				newMarkers.put(index - numberDecreased, marker);
			}
			return newMarkers;
		}
		return markerMap;
	}

	protected void adjustMarkersAfterIncrease(int numberIncreased) {
		this.markers = adjustMarkersAfterIncrease(numberIncreased, this.markers);
		this.gaps = adjustMarkersAfterIncrease(numberIncreased, this.gaps);
	}

	private SortedMap<Integer, Marker> adjustMarkersAfterIncrease(
			int numberIncreased, SortedMap<Integer, Marker> markerMap) {
		if (markerMap.size() > 0) {
			SortedMap<Integer, Marker> newMarkers = new TreeMap<Integer, Marker>();
			int targetIndex;
			if (getDirection() == Direction.FORWARDS) {
				targetIndex = lastStitchIndexReturned + 1;
			} else {
				targetIndex = lastStitchIndexReturned;
			}
			newMarkers.putAll(markerMap.headMap(targetIndex));
			for (int index : markerMap.tailMap(targetIndex).keySet()) {
				Marker marker = markerMap.get(index);
				// offset each marker's index by the number that was increased
				newMarkers.put(index + numberIncreased, marker);
			}
			return newMarkers;
		}
		return markerMap;
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see com.knitml.validation.validation.engine.model.Needle#castOn(int)
	// */
	// public void castOn(int numberOfStitches) {
	// for (int i = 0; i < numberOfStitches; i++) {
	// Stitch stitch = knittingFactory.createStitch();
	// if (getDirection() == Direction.FORWARDS) {
	// stitchCursor.add(stitch);
	// } else {
	// stitchCursor.add(stitch);
	// stitchCursor.previous();
	// }
	// }
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#getTotalStitches()
	 */
	public int getTotalStitches() {
		return stitches.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#getStitchesRemaining
	 * ()
	 */
	public int getStitchesRemaining() {
		if (direction == Direction.FORWARDS) {
			return stitches.size() - stitchCursor.nextIndex();
		} else {
			return 1 + stitchCursor.previousIndex();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#areMarkersRemaining
	 * ()
	 */
	public boolean areMarkersRemaining() {
		if (markers.size() == 0) {
			return false;
		}
		int nextStitch = getNextStitchIndex();
		if (direction == Direction.FORWARDS) {
			return !(markers.tailMap(nextStitch)).isEmpty();
		} else {
			return !(markers.headMap(nextStitch + 2)).isEmpty();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#isEndOfNeedle()
	 */
	public boolean isEndOfNeedle() {
		return getStitchesRemaining() == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#isBeginningOfNeedle
	 * ()
	 */
	public boolean isBeginningOfNeedle() {
		return getStitchesRemaining() == getTotalStitches();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#startAtBeginning()
	 */
	public void startAtBeginning() {
		if (direction == Direction.FORWARDS) {
			stitchCursor = stitches.listIterator();
		} else {
			stitchCursor = stitches.listIterator(stitches.size());
		}
		lastStitchIndexReturned = -1;
	}

	public void startAtEnd() {
		if (direction == Direction.FORWARDS) {
			stitchCursor = stitches.listIterator(stitches.size());
		} else {
			stitchCursor = stitches.listIterator();
		}
		lastStitchIndexReturned = -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#getNextStitchIndex()
	 */
	public int getNextStitchIndex() {
		if (direction == Direction.FORWARDS) {
			return stitchCursor.nextIndex();
		} else {
			return stitchCursor.previousIndex();
		}
	}

	public int getPreviousStitchIndex() {
		if (direction == Direction.FORWARDS) {
			return stitchCursor.previousIndex();
		} else {
			return stitchCursor.nextIndex();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#peekAtNextStitch()
	 */
	public Stitch peekAtNextStitch() {
		Stitch stitch = null;
		if (direction == Direction.FORWARDS) {
			stitch = stitchCursor.next();
			stitchCursor.previous();
		} else {
			stitch = stitchCursor.previous();
			stitchCursor.next();
		}
		return stitch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#getStitchesToNextMarker
	 * ()
	 */
	public int getStitchesToNextMarker() throws NoMarkerFoundException {
		int result = getStitchesToNextMarker(this.markers);
		if (result == -1) {
			throw new NoMarkerFoundException();
		}
		return result;
	}

	public int getStitchesToGap() {
		return getStitchesToNextMarker(this.gaps);
	}

	private int getStitchesToNextMarker(SortedMap<Integer, Marker> markerMap) {
		int nextStitch = getNextStitchIndex();
		boolean ignoreImmediateMarker = false;
		if (lastStitchIndexReturned == nextStitch) {
			ignoreImmediateMarker = true;
		}
		int markerIndex;
		if (direction == Direction.FORWARDS) {
			try {
				if (ignoreImmediateMarker) {
					markerIndex = markerMap.tailMap(nextStitch + 1).firstKey();
				} else {
					markerIndex = markerMap.tailMap(nextStitch).firstKey();
				}
				return markerIndex - nextStitch;
			} catch (NoSuchElementException ex) {
				return -1;
			}
		} else {
			try {
				// since headMap() is exclusive of the number provided AND
				// the marker index precedes the stitch number (when knitting
				// forwards), we have to add 2 to the next stitch to achieve
				// the desired result.
				if (ignoreImmediateMarker) {
					markerIndex = markerMap.headMap(nextStitch + 1).lastKey();
				} else {
					markerIndex = markerMap.headMap(nextStitch + 2).lastKey();
				}
				return nextStitch - markerIndex + 1;
			} catch (NoSuchElementException ex) {
				return -1;
			}
		}
	}

	private Marker getNextMarker() throws NoMarkerFoundException {
		int nextStitch = getNextStitchIndex();
		Marker result = null;
		if (direction == Direction.FORWARDS) {
			result = markers.get(markers.tailMap(nextStitch).firstKey());
		} else {
			result = markers.get(markers.headMap(nextStitch + 2).lastKey());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#purlTwoTogether()
	 */
	public void purlTwoTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		Stitch result = decrease(1);
		recordPurl(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#addStitchesToBeginning
	 * (java.util.List)
	 */
	public void addStitchesToBeginning(List<Stitch> stitchesToAdd) {
		if (getDirection() == Direction.FORWARDS) {
			stitches.addAll(0, stitchesToAdd);
		} else {
			stitches.addAll(stitchesToAdd);
		}
		// reset the list iterator for the stitches
		startAtBeginning();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#addStitchesToEnd
	 * (java.util.List)
	 */
	public void addStitchesToEnd(List<Stitch> stitchesToAdd) {
		if (getDirection() == Direction.FORWARDS) {
			stitches.addAll(stitchesToAdd);
		} else {
			stitches.addAll(0, stitchesToAdd);
		}
		// reset the list iterator for the stitches
		startAtEnd();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#getStitches()
	 */
	public List<Stitch> getStitches() {
		List<Stitch> result;
		if (getDirection() == Direction.BACKWARDS) {
			result = new ArrayList<Stitch>();
			result.addAll(this.stitches);
			Collections.reverse(result);
		} else {
			result = this.stitches;
		}
		return Collections.unmodifiableList(result);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.knitml.validation.validation.engine.model.Needle#
	 * removeNStitchesFromBeginning(int)
	 */
	public List<Stitch> removeNStitchesFromBeginning(int number)
			throws NeedlesInWrongDirectionException {
		assertForwardsDirection();
		List<Stitch> result = new ArrayList<Stitch>(stitches.subList(0, number));
		stitchCursor = stitches.listIterator();
		for (int i = 0; i < number; i++) {
			stitchCursor.next();
			stitchCursor.remove();
		}
		lastStitchIndexReturned = -1;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.knitml.validation.validation.engine.model.Needle#removeNStitchesFromEnd
	 * (int)
	 */
	public List<Stitch> removeNStitchesFromEnd(int number)
			throws NeedlesInWrongDirectionException {
		assertForwardsDirection();
		List<Stitch> result = new ArrayList<Stitch>(stitches.subList(
				stitches.size() - number, stitches.size()));
		stitchCursor = stitches.listIterator(stitches.size());
		for (int i = 0; i < number; i++) {
			stitchCursor.previous();
			stitchCursor.remove();
		}
		lastStitchIndexReturned = -1;
		return result;
	}

	private void assertForwardsDirection()
			throws NeedlesInWrongDirectionException {
		if (direction == Direction.BACKWARDS) {
			throw new NeedlesInWrongDirectionException();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.knitml.validation.validation.engine.model.Needle#getDirection()
	 */
	public Direction getDirection() {
		return direction;
	}

	public NeedleStyle getNeedleType() {
		return needleType;
	}

	public String getId() {
		return id;
	}

	public KnittingFactory getKnittingFactory() {
		return knittingFactory;
	}

	public void passPreviousStitchOver() throws NotEnoughStitchesException {
		retreatCursorOne();
		retreatCursorOne();
		doDecrease(1, false);
	}

	public void knitThreeTogether() throws NotEnoughStitchesException,
			CannotWorkThroughMarkerException {
		// doing this rather than decrease(2) allows multiple markers to be
		// handled, though technically it knits the same stitch twice.
		doKnitTwoTogether();
		reverseSlip();
		doKnitTwoTogether();

		// This used to be done by the following method call:
		// decrease(2);
	}

	private Stitch decrease(int numberToDecrease)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		Stitch resultingStitch;

		boolean betweenGap = false;
		if (isGapBetweenNextNStitches(numberToDecrease + 1)) {
			betweenGap = true;
		}
		try {
			if (isMarkerBetweenNextNStitches(numberToDecrease + 1)) {
				MarkerBehavior behavior = getNextMarker()
						.getWhenWorkedThrough();
				Marker removedMarker = null;
				if (behavior == MarkerBehavior.THROW_EXCEPTION) {
					throw new CannotWorkThroughMarkerException();
				} else {
					int stitchesToMarker = 0;
					while (getStitchesToNextMarker() > 0) {
						slip();
						stitchesToMarker++;
					}
					removedMarker = removeMarker();
					for (int i = 0; i < stitchesToMarker; i++) {
						reverseSlip();
					}
				}
				if (behavior == MarkerBehavior.REMOVE) {
					log.info(Messages
							.getString("DefaultNeedle.REMOVING_MARKER_BETWEEN_DECREASE")); //$NON-NLS-1$
				} else if (behavior == MarkerBehavior.PLACE_BEFORE_STITCH_WORKED) {
					placeMarker(removedMarker);
				}
				resultingStitch = doDecrease(numberToDecrease, betweenGap);
				if (behavior == MarkerBehavior.PLACE_AFTER_STITCH_WORKED) {
					placeMarker(removedMarker);
				}
			} else {
				resultingStitch = doDecrease(numberToDecrease, betweenGap);
			}
			return resultingStitch;
		} catch (NoMarkerFoundException ex) {
			// should not happen
			throw new RuntimeException(
					Messages.getString("DefaultNeedle.INTERNAL_ERROR"), //$NON-NLS-1$
					ex);
		} catch (CannotPutMarkerOnEndOfNeedleException ex) {
			// should not happen
			throw new RuntimeException(
					Messages.getString("DefaultNeedle.INTERNAL_ERROR"), //$NON-NLS-1$
					ex);
		}
	}

	public void increase(int numberToIncrease) {
		increase(new Increase(numberToIncrease));
	}

	public void increase(Increase increase) {
		// TODO increase should know whether it was a knitted or purled increase
		int numberToIncrease = increase.getNumberOfTimes() == null ? 1
				: increase.getNumberOfTimes();
		StitchNature stitchNature = increase.getStitchNatureProduced();
		if (direction == Direction.FORWARDS) {
			for (int i = 0; i < numberToIncrease; i++) {
				Stitch stitch = knittingFactory.createStitch();
				stitch.recordNature(stitchNature);
				// this inserts the stitch before the cursor
				stitchCursor.add(stitch);
			}
			lastStitchIndexReturned = stitchCursor.previousIndex();
		} else {
			for (int i = 0; i < numberToIncrease; i++) {
				Stitch stitch = knittingFactory.createStitch();
				stitch.recordNature(reverse(stitchNature));
				stitchCursor.add(stitch);
				stitchCursor.previous();
			}
			lastStitchIndexReturned = stitchCursor.nextIndex();
		}
		adjustMarkersAfterIncrease(numberToIncrease);
	}

	public void addStitch(Stitch stitchToAdd) {
		if (direction == Direction.FORWARDS) {
			stitchCursor.add(stitchToAdd);
			lastStitchIndexReturned = stitchCursor.previousIndex();
		} else {
			stitchCursor.add(stitchToAdd);
			stitchCursor.previous();
			lastStitchIndexReturned = stitchCursor.nextIndex();
		}
		adjustMarkersAfterIncrease(1);
	}

	public void cross(int numberToCross, int numberCrossedOver)
			throws NotEnoughStitchesException, CannotWorkThroughMarkerException {
		int stitchesAffected = numberToCross + numberCrossedOver;
		if (stitchesAffected > getStitchesRemaining()) {
			throw new NotEnoughStitchesException(numberToCross
					+ numberCrossedOver, getStitchesRemaining());
		}
		try {
			if (hasMarkers() && getStitchesToNextMarker() > 0
					&& getStitchesToNextMarker() < stitchesAffected) {
				throw new CannotWorkThroughMarkerException(
						Messages.getString("DefaultNeedle.CANNOT_CROSS_STITCHES_THROUGH_MARKER")); //$NON-NLS-1$
			}
		} catch (NoMarkerFoundException ex) {
			// won't happen since we're checking whether there are markers to
			// begin with
			throw new RuntimeException(ex);
		}
		if (getDirection() == Direction.FORWARDS) {
			// gather stitches to cross into a temporary list and remove from
			// the master list
			List<Stitch> firstPart = new ArrayList<Stitch>(numberToCross);
			for (int i = 0; i < numberToCross; i++) {
				Stitch stitch = stitchCursor.next();
				firstPart.add(stitch);
				stitchCursor.remove();
			}
			// the stitches to cross over are next, so walk past them
			for (int i = 0; i < numberCrossedOver; i++) {
				stitchCursor.next();
			}
			// now add the crossed stitches
			for (Stitch stitch : firstPart) {
				stitchCursor.add(stitch);
			}
			// now iterate to before the crossing
			for (int i = 0; i < (numberToCross + numberCrossedOver); i++) {
				lastStitchIndexReturned = stitchCursor.previousIndex();
				stitchCursor.previous();
			}
		} else {
			// gather stitches to cross into a temporary list and remove from
			// the master list
			List<Stitch> firstPart = new ArrayList<Stitch>(numberToCross);
			for (int i = 0; i < numberToCross; i++) {
				Stitch stitch = stitchCursor.previous();
				// add to beginning because the insertion order would be
				// backwards otherwise
				firstPart.add(0, stitch);
				stitchCursor.remove();
			}
			// the stitches to cross over are next, so walk past them
			for (int i = 0; i < numberCrossedOver; i++) {
				stitchCursor.previous();
			}
			// now add the crossed stitches
			for (Stitch stitch : firstPart) {
				stitchCursor.add(stitch);
			}
			// now iterate to before the crossing
			for (int i = 0; i < numberCrossedOver; i++) {
				// we don't need to cross over the newly inserted stitches since
				// we're already past them (the way list iterators work upon
				// adding elements)
				lastStitchIndexReturned = stitchCursor.nextIndex();
				stitchCursor.next();
			}
		}
	}

	private void recordKnit(Stitch workedStitch) {
		if (getDirection() == Direction.BACKWARDS) {
			workedStitch.recordNature(reverse(KNIT));
		} else {
			workedStitch.recordNature(KNIT);
		}
	}

	private void recordPurl(Stitch workedStitch) {
		if (getDirection() == Direction.BACKWARDS) {
			workedStitch.recordNature(reverse(PURL));
		} else {
			workedStitch.recordNature(PURL);
		}
	}

}
