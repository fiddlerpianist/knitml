package com.knitml.renderer.impl.charting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.knitml.core.model.directions.inline.Repeat;

class RepeatSet implements Iterable<Object> {

	private Repeat repeat;
	private List<Object> operations = new ArrayList<Object>();
	
	public RepeatSet(Repeat repeat) {
		this.repeat = repeat;
	}
	
	public Repeat getRepeat() {
		return this.repeat;
	}

	public int size() {
		return operations.size();
	}

	public Object getOperation(int index) {
		return operations.get(index);
	}

	public void addOperation(ChartElement element) {
		operations.add(element);
	}

	public void addOperation(RepeatSet holder) {
		operations.add(holder);
	}

	public Iterator<Object> iterator() {
		return operations.iterator();
	}


}
