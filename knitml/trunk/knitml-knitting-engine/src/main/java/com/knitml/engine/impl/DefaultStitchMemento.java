package com.knitml.engine.impl;

import com.knitml.engine.StitchOperation;


class DefaultStitchMemento {

	private StitchOperation history;
	
	public DefaultStitchMemento(StitchOperation history) {
		this.history = history;
	}

	public StitchOperation getHistory() {
		return history;
	}

}
