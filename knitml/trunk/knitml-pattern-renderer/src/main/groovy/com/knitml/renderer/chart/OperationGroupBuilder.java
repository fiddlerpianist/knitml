package com.knitml.renderer.chart;

import static com.knitml.core.common.CrossType.BACK;
import static com.knitml.core.common.CrossType.FRONT;

import java.util.ArrayList;
import java.util.List;

import com.knitml.core.common.LoopToWork;
import com.knitml.core.model.operations.DiscreteInlineOperation;
import com.knitml.core.model.operations.inline.CrossStitches;
import com.knitml.core.model.operations.inline.Knit;
import com.knitml.core.model.operations.inline.OperationGroup;
import com.knitml.core.model.operations.inline.Purl;

public class OperationGroupBuilder {

	protected List<DiscreteInlineOperation> operations = new ArrayList<DiscreteInlineOperation>();
	protected int size;
	
	public static OperationGroupBuilder group(int size) {
		return new OperationGroupBuilder(size);
	}
	
	public OperationGroupBuilder(int size) {
		this.size = size;
	}
	
	public OperationGroupBuilder lc(int x, int y) {
		CrossStitches op = new CrossStitches(x, FRONT, y);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder lc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, FRONT, BACK);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder rc(int x, int y) {
		CrossStitches op = new CrossStitches(x, BACK, y);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder rc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, BACK, BACK);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder lrc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, FRONT, FRONT);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder rrc(int x, int m, int y) {
		CrossStitches op = new CrossStitches(x, m, y, BACK, FRONT);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder k(int i) {
		Knit op = new Knit(i, null, null);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder k_tbl(int i) {
		Knit op = new Knit(i, LoopToWork.TRAILING, null);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder p(int i) {
		Purl op = new Purl(i, null, null);
		operations.add(op);
		return this;
	}
	public OperationGroupBuilder p_tbl(int i) {
		Purl op = new Purl(i, LoopToWork.TRAILING, null);
		operations.add(op);
		return this;
	}
	
	public OperationGroup build() {
		return new OperationGroup(size, operations).canonicalize().get(0);
	}
}
