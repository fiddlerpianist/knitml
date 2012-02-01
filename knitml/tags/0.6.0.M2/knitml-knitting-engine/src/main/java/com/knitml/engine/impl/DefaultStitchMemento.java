package com.knitml.engine.impl;

import com.knitml.core.model.directions.StitchNature;


class DefaultStitchMemento {

	private StitchNature history;
	
	public DefaultStitchMemento(StitchNature history) {
		this.history = history;
	}

	public StitchNature getHistory() {
		return history;
	}

}
