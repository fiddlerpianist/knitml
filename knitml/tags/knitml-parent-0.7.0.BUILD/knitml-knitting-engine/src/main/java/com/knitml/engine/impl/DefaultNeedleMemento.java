package com.knitml.engine.impl;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.knitml.engine.Marker;
import com.knitml.engine.Stitch;
import com.knitml.engine.settings.Direction;

class DefaultNeedleMemento {

	private Direction direction;
	private List<Stitch> stitches;
	private int nextStitchIndex;
	private SortedMap<Integer, Marker> markers;
	private SortedMap<Integer, Marker> gaps;
	private int lastStitchIndexReturned;
	private Map<String, Object> stitchMementos;
	
	public DefaultNeedleMemento(Direction direction, List<Stitch> stitches,
			int nextStitchIndex, SortedMap<Integer, Marker> markers,
			SortedMap<Integer, Marker> gaps, int lastStitchIndexReturned, Map<String, Object> stitchMementos) {
		this.direction = direction;
		this.stitches = stitches;
		this.nextStitchIndex = nextStitchIndex;
		this.markers = markers;
		this.gaps = gaps;
		this.lastStitchIndexReturned = lastStitchIndexReturned;
		this.stitchMementos = stitchMementos;
	}

	public Direction getDirection() {
		return direction;
	}

	public List<Stitch> getStitches() {
		return stitches;
	}

	public int getNextStitchIndex() {
		return nextStitchIndex;
	}

	public SortedMap<Integer, Marker> getMarkers() {
		return markers;
	}

	public SortedMap<Integer, Marker> getGaps() {
		return gaps;
	}

	public int getLastStitchIndexReturned() {
		return lastStitchIndexReturned;
	}

	public Map<String, Object> getStitchMementos() {
		return stitchMementos;
	}
}
