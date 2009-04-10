package com.knitml.engine.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.knitml.core.common.KnittingShape;
import com.knitml.engine.Needle;
import com.knitml.engine.settings.Direction;

class DefaultEngineMemento {

	private Direction direction;
	private KnittingShape knittingShape;
	private int totalRowsCompleted;
	private int currentRowNumber;
	private int currentNeedleIndex;
	private boolean suppressDirectionSwitchingForNextRow;
	private boolean castingOn;
	private StitchCoordinate startOfRow;
	private List<Needle> activeNeedles;
	private Set<Needle> allNeedles;
	private Needle imposedNeedle;
	private Map<String, Object> needleMementos;

	public DefaultEngineMemento(Direction direction,
			KnittingShape knittingShape, int totalRowsCompleted,
			int currentRowNumber, List<Needle> activeNeedles,
			Set<Needle> allNeedles, Needle imposedNeedle,
			Map<String, Object> needleMementos, int currentNeedleIndex,
			boolean suppressDirectionSwitchingForNextRow, boolean castingOn,
			StitchCoordinate startOfRow) {
		this.direction = direction;
		this.knittingShape = knittingShape;
		this.totalRowsCompleted = totalRowsCompleted;
		this.currentRowNumber = currentRowNumber;
		this.activeNeedles = activeNeedles;
		this.allNeedles = allNeedles;
		this.imposedNeedle = imposedNeedle;
		this.needleMementos = needleMementos;
		this.currentNeedleIndex = currentNeedleIndex;
		this.suppressDirectionSwitchingForNextRow = suppressDirectionSwitchingForNextRow;
		this.castingOn = castingOn;
		this.startOfRow = startOfRow;
	}

	public List<Needle> getActiveNeedles() {
		return activeNeedles;
	}

	public Direction getDirection() {
		return direction;
	}

	public KnittingShape getKnittingShape() {
		return knittingShape;
	}

	public int getTotalRowsCompleted() {
		return totalRowsCompleted;
	}

	public int getCurrentRowNumber() {
		return currentRowNumber;
	}

	public Map<String, Object> getNeedleMementos() {
		return Collections.unmodifiableMap(needleMementos);
	}

	public int getCurrentNeedleIndex() {
		return currentNeedleIndex;
	}

	public boolean isSuppressDirectionSwitchingForNextRow() {
		return suppressDirectionSwitchingForNextRow;
	}

	public boolean isCastingOn() {
		return castingOn;
	}

	public StitchCoordinate getStartOfRow() {
		return startOfRow;
	}

	public Set<Needle> getAllNeedles() {
		return allNeedles;
	}

	public Needle getImposedNeedle() {
		return imposedNeedle;
	}

}
