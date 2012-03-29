package com.knitml.renderer.chart;

import static com.knitml.core.common.CrossType.BACK;
import static com.knitml.core.common.CrossType.FRONT;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.DecreaseType;
import com.knitml.core.common.IncreaseType;
import com.knitml.core.common.LoopToWork;
import com.knitml.core.common.SlipDirection;
import com.knitml.core.common.Wise;
import com.knitml.core.common.YarnPosition;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Decrease;
import com.knitml.core.model.operations.inline.DoubleDecrease;
import com.knitml.core.model.operations.inline.Increase;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.core.model.operations.inline.Purl;
import com.knitml.core.model.operations.inline.Slip;

class OperationBuilder {

	protected List<DiscreteInlineOperation> operations = new ArrayList<DiscreteInlineOperation>();
	protected int size;

	public static OperationBuilder group(int size) {
		return new OperationBuilder(size);
	}

	public OperationBuilder(int size) {
		this.size = size;
	}

	public OperationBuilder lc(int x, int y) {
		CrossStitches op = new CrossStitches(x, FRONT, y);
		operations.add(op);
		return this;
	}

	public OperationBuilder lc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, FRONT, BACK);
		operations.add(op);
		return this;
	}

	public OperationBuilder rc(int x, int y) {
		CrossStitches op = new CrossStitches(y, BACK, x);
		operations.add(op);
		return this;
	}

	public OperationBuilder rc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(y, m, x, BACK, BACK);
		operations.add(op);
		return this;
	}

	public OperationBuilder lrc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, FRONT, FRONT);
		operations.add(op);
		return this;
	}

	public OperationBuilder rrc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(y, m, x, BACK, FRONT);
		operations.add(op);
		return this;
	}

	public OperationBuilder k(int i) {
		Knit op = new Knit(i, null, null);
		operations.add(op);
		return this;
	}

	public OperationBuilder k_tbl(int i) {
		Knit op = new Knit(i, LoopToWork.TRAILING, null);
		operations.add(op);
		return this;
	}

	public OperationBuilder p(int i) {
		Purl op = new Purl(i, null, null);
		operations.add(op);
		return this;
	}

	public OperationBuilder p_tbl(int i) {
		Purl op = new Purl(i, LoopToWork.TRAILING, null);
		operations.add(op);
		return this;
	}

	public OperationBuilder yo(int i) {
		Increase op = new Increase(i, IncreaseType.YO);
		operations.add(op);
		return this;
	}

	public OperationBuilder m1(int i) {
		Increase op = new Increase(i, IncreaseType.M1);
		operations.add(op);
		return this;
	}

	public OperationGroup build() {
		return new OperationGroup(size, operations).canonicalize().get(0);
	}

	// static single operation helper methods

	public static DiscreteInlineOperation knit() {
		return knit(LoopToWork.LEADING);
	}

	public static DiscreteInlineOperation knit(LoopToWork loop) {
		return new Knit(null, loop, null).canonicalize().get(0);
	}

	public static DiscreteInlineOperation purl() {
		return purl(LoopToWork.LEADING);
	}

	public static DiscreteInlineOperation purl(LoopToWork loop) {
		return new Purl(null, loop, null).canonicalize().get(0);
	}

	public static DiscreteInlineOperation slip() {
		return slip(Wise.PURLWISE, YarnPosition.BACK);
	}

	public static DiscreteInlineOperation slip(YarnPosition position) {
		return slip(Wise.PURLWISE, position);
	}

	public static DiscreteInlineOperation slip_kw() {
		return slip(Wise.KNITWISE, YarnPosition.BACK);
	}

	public static DiscreteInlineOperation slip_kw(YarnPosition position) {
		return slip(Wise.KNITWISE, position);
	}

	public static DiscreteInlineOperation slip(Wise wise, YarnPosition position) {
		return new Slip(null, wise, position, SlipDirection.FORWARD)
				.canonicalize().get(0);
	}

	public static DiscreteInlineOperation decrease() {
		return new Decrease().canonicalize().get(0);
	}

	public static DiscreteInlineOperation decrease(DecreaseType type) {
		switch (type) {
		case K3TOG:
		case SSSK:
		case SK2P:
		case CDD:
		case P3TOG:
			return new DoubleDecrease(null, type, null).canonicalize().get(0);
		default:
			return new Decrease(null, type, null).canonicalize().get(0);
		}
	}

	public static DiscreteInlineOperation increase(IncreaseType type) {
		return new Increase(null, type, null).canonicalize().get(0);
	}
}
