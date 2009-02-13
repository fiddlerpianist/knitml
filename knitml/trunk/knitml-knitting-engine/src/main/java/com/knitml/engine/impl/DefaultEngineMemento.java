package com.knitml.engine.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
	private StitchCoordinate startOfRow;
	private List<Needle> needles;
	private Map<String, Object> needleMementos;

	public DefaultEngineMemento(Direction direction,
			KnittingShape knittingShape, int totalRowsCompleted,
			int currentRowNumber, List<Needle> needles,
			Map<String, Object> needleMementos, int currentNeedleIndex,
			boolean suppressDirectionSwitchingForNextRow,
			StitchCoordinate startOfRow) {
		this.direction = direction;
		this.knittingShape = knittingShape;
		this.totalRowsCompleted = totalRowsCompleted;
		this.currentRowNumber = currentRowNumber;
		this.needles = needles;
		this.needleMementos = needleMementos;
		this.currentNeedleIndex = currentNeedleIndex;
		this.suppressDirectionSwitchingForNextRow = suppressDirectionSwitchingForNextRow;
		this.startOfRow = startOfRow;
	}

	public List<Needle> getNeedles() {
		return needles;
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

	public StitchCoordinate getStartOfRow() {
		return startOfRow;
	}

}
